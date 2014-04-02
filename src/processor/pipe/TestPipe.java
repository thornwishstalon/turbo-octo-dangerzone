package processor.pipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.input.settings.ApplicationSetup;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import processor.DocumentProcessor;
import processor.TextDocument;
import reader.Reader;

public class TestPipe extends Thread{
	
	private ExecutorService pool;
	private boolean isRunning = false;
	private ArrayList<AbstractPipeStage> stages;
	private static Logger logger = LogManager.getLogger("TestPipe");
	
	public TestPipe() {
		stages = new ArrayList<AbstractPipeStage>();
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	private void init(PipedWriter inputStart) throws IOException {
		logger.info("init pipe");

		if (!stages.isEmpty()) {
			stages.removeAll(stages);
		}

		stages.add(new Filter(new PipedReader(inputStart), new PipedWriter()));

		stages.add(new CaseFolding(new PipedReader(stages.get(stages.size() - 1).getOut()), new PipedWriter()));

		if (ApplicationSetup.getInstance().getUseStopwords()) {
			stages.add(new StopWordRemoval(new PipedReader(stages.get(stages.size() - 1).getOut()), new PipedWriter()));
		}

		if (ApplicationSetup.getInstance().getUseStemmer()) {
			stages.add(new Stemming(new PipedReader(stages.get(stages.size() - 1).getOut()), new PipedWriter()));
		}

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
	
	@Override
	public void run() {
		isRunning = true;

		long start = System.currentTimeMillis();
		ArrayList<File> testDocuments = new ArrayList<>();
		String path = ApplicationSetup.getInstance().getTopicFilePath();
		System.out.println("Reading directory: " + path);
		
		// stores all files in the arrayList
		Reader reader = new Reader(path);
		reader.readFiles(testDocuments);

		PipedWriter inputFileWriter = null;

		try {
			System.out.println("STARTING");

			inputFileWriter = new PipedWriter();

			init(inputFileWriter);

			//test read - just 1 file
			List<TextDocument> documentList = new ArrayList<TextDocument>();
			DocumentProcessor documentProcessor = new DocumentProcessor();
			String completeText = documentProcessor.process(testDocuments, documentList);
			System.out.println("Number of test docs: " + testDocuments.size());

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
			
			long end = System.currentTimeMillis();
			System.out.println("DONE");
			System.out.println((end - start) / 1000 + " seconds");
			isRunning = false;
		}

	}
}
