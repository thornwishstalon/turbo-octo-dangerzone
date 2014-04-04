package processor.pipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import main.ApplicationStatus;
import main.input.settings.ApplicationSetup;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import processor.BlockMerger;
import processor.DocParentFolderEnum;
import reader.Reader;

public class QueryPipe extends Thread {

	
	
	private ExecutorService pool;
	private boolean isRunning = false;
	private ArrayList<AbstractPipeStage> stages;
	private static Logger logger = LogManager.getLogger("ProcessPipe");
	private String topic;
	private QueryLookStage querystage;

	public QueryPipe() {

		stages = new ArrayList<AbstractPipeStage>();

	}

	public void setTopic(String topic){
		this.topic= topic.trim();
	}

	public String getTopic(){
		return topic;
	}

	private void init(PipedWriter inputStart) throws IOException {
		logger.info("init pipe");

		if (!stages.isEmpty()) {
			stages.removeAll(stages);
		}

		// int index=0;
		stages.add(new Filter(new PipedReader(inputStart), new PipedWriter()));

		stages.add(new CaseFolding(new PipedReader(stages
				.get(stages.size() - 1).getOut()), new PipedWriter()));
		// stages.add(new CaseFolding(new PipedReader(inputStart), new
		// PipedWriter()));

		if (ApplicationSetup.getInstance().getUseStopwords()) {
			stages.add(new StopWordRemoval(new PipedReader(stages.get(
					stages.size() - 1).getOut()), new PipedWriter()));
		}
		if (ApplicationSetup.getInstance().getUseStemmer()) {
			stages.add(new Stemming(new PipedReader(stages.get(
					stages.size() - 1).getOut()), new PipedWriter()));

		}
		if (ApplicationSetup.getInstance().getUseBigrams()) {
			stages.add(new BigramBufferStage(new PipedReader(stages.get(
					stages.size() - 1).getOut()), new PipedWriter()));
		}

		QueryLookStage query= new QueryLookStage(new PipedReader(stages.get(
				stages.size()-1).getOut()), new PipedWriter());
		//		indexing = new Indexing(new PipedReader(stages.get(stages.size() - 1)
		//				.getOut()), new PipedWriter(), this);
		//
		//		stages.add(indexing);
		stages.add(query);
		querystage = query;

		pool = Executors.newFixedThreadPool(stages.size()+1);

		AbstractPipeStage stage;
		for (int i = 0; i < stages.size(); i++) {
			stage = stages.get(i);
			if (i == 0) {
				stage.setWaitingFor(stages.get(stages.size() - 1));
			} else {
				stage.setWaitingFor(stages.get(i - 1));
			}

			pool.execute(stage);
		}

	}

	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void run() {
		isRunning = true;

		long start = System.currentTimeMillis();
//		ArrayList<File> documents = new ArrayList<>();
//		String path = ApplicationSetup.getInstance().getTopicFilePath();
//		System.out.println("Reading directory: " + path);
//		Reader reader = new Reader(path);
//
//		// stores all files in the arrayList
//		reader.readFiles(documents);

		PipedWriter inputFileWriter = null;
		BufferedReader br=null;
		try {
			inputFileWriter = new PipedWriter();

			init(inputFileWriter);
			
			String line;
			String[] tokens;
			
			boolean firstBlankLineFound=false;
			String pathString=ApplicationSetup.getInstance().getTopicFilePath()+"/topic"+topic;
			
			File file = new File(pathString);
			//System.out.println(path);
			 //=path.toFile(); 
			
			

			//reading file and passing tokens to next pipe stages
			br = new BufferedReader(new FileReader(file));
			firstBlankLineFound = false;

			while ((line = br.readLine()) != null) {
				if ((line.isEmpty() || line.trim().equals("")
						|| line.trim().equals("\n"))){
					firstBlankLineFound = true;
				}

				if(firstBlankLineFound){
					tokens = line.split("\\s");
					for (int i = 0; i < tokens.length; i++) {
						inputFileWriter.write(tokens[i] + "\n");
						inputFileWriter.flush();
					}
				}
			}
			
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				
				if(br!=null){
					br.close();
				}
				if (inputFileWriter != null)
					inputFileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

			long end = System.currentTimeMillis();
			System.out.println("reading files DONE in ");
			System.out.println((end - start) / 1000 + " seconds");
			
		
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			
			ApplicationStatus.getInstance().doRanking();
			ApplicationStatus.getInstance().printResults();
			
			isRunning = false;

			try {
				pool.awaitTermination(2000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	


}
