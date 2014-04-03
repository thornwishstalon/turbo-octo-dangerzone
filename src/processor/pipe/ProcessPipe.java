package processor.pipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import processor.BlockMerger;
import processor.DocParentFolderEnum;
import main.input.settings.ApplicationSetup;
import reader.Reader;

public class ProcessPipe extends Thread {

	private ExecutorService pool;
	private boolean isRunning = false;
	private ArrayList<AbstractPipeStage> stages;
	private Indexing indexing;
	private static Logger logger = LogManager.getLogger("ProcessPipe");

	public ProcessPipe() {

		stages = new ArrayList<AbstractPipeStage>();

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

		indexing = new Indexing(new PipedReader(stages.get(stages.size() - 1)
				.getOut()), new PipedWriter(), this);

		stages.add(indexing);

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
		ArrayList<File> documents = new ArrayList<>();
		String path = ApplicationSetup.getInstance().getCorporaPath();
		System.out.println("Reading directory: " + path);
		Reader reader = new Reader(path);

		// stores all files in the arrayList
		reader.readFiles(documents);

		PipedWriter inputFileWriter = null;

		try {
			System.out.println("STARTING");

			inputFileWriter = new PipedWriter();

			init(inputFileWriter);

			BufferedReader br;
			String line;
			String[] tokens;
			int id = 0; // for testing
			String fileID;
			float percent;
			int c=0;
			
			boolean firstBlankLineFound=false;
			
			for (File file : documents) {
				percent = round((c++ * 100.0f) / reader.getSize(),4);
				System.out.println(percent+"%\t\t|| processing file :" + file.getAbsolutePath());
				
				//defining file ID
				String parentName = getParentFolderName(file.getAbsolutePath());
				id = getParentFolderValue(parentName);

				// id will be parentFolderNameValue + fileName
				fileID = id + file.getName();

				indexing.setCurrentDocID(fileID);
				
				
				//reading file and passing tokens to next pipe stages
				br = new BufferedReader(new FileReader(file));
				 firstBlankLineFound = false;
				
				while ((line = br.readLine()) != null) {
					if ((line.isEmpty() || line.trim().equals("")
							|| line.trim().equals("\n"))&&!firstBlankLineFound){
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

			}

			//indexing.backup();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (inputFileWriter != null)
					inputFileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mergeBlocks(indexing.getBlocks());
			
			long end = System.currentTimeMillis();
			System.out.println("DONE");
			System.out.println((end - start) / 1000 + " seconds");
			isRunning = false;
			
			pool.shutdown();
		}

	}
	
	public void mergeBlocks(ArrayList<String> blocks){
		BlockMerger merger= new BlockMerger(blocks);
		pool.execute(merger);
	}
	
	private String getParentFolderName(String absolutePath) {
		String parentName = "";

		if (absolutePath != null) {
			// take care of OS specific path separator...
			String[] tokens = absolutePath.split(Pattern.quote(File.separator));
			parentName = tokens[tokens.length - 2];
		}

		return parentName;
	}
	
	private float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	
	private int getParentFolderValue(String parentName) {
		int value = 0;

		if (parentName != null) {
			// to match enum values
			parentName = parentName.replace("-", "_");
			parentName = parentName.replace(".", "_");
			parentName = parentName.toUpperCase();

			// take value form enum
			DocParentFolderEnum type = DocParentFolderEnum.ALT_ATHEISM;
			try {
				value = type.getValue(parentName);
			} catch (java.lang.Exception ex) {
				value = -1;
			}
		}

		return value;
	}


}
