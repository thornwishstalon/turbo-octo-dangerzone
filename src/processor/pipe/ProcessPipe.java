package processor.pipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import processor.BlockMerger;
import processor.DocParentFolderEnum;
import main.ApplicationStatus;
import main.input.settings.ApplicationSetup;
import reader.Reader;

public class ProcessPipe extends Thread {

	private ExecutorService pool;
	private boolean isRunning = false;
	private ArrayList<AbstractPipeStage> stages;
	private Indexing indexing;
	private static Logger logger = LogManager.getLogger("ProcessPipe");
	private TreeMap<String, Integer> fileN;

	public ProcessPipe() {

		stages = new ArrayList<AbstractPipeStage>();
		fileN = new TreeMap<String, Integer>();
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

		ApplicationStatus.getInstance().setN(reader.getSize());

		PipedWriter inputFileWriter = null;
		BufferedReader br = null;
		try {
			System.out.println("STARTING");

			inputFileWriter = new PipedWriter();

			init(inputFileWriter);


			String line;
			String[] tokens;
			int id = 0; // for testing
			String fileID;
			float percent;
			int c=0;
			int nd;



			for (File file : documents) {
				percent = round((c++ * 100.0f) / reader.getSize(),2);
				System.out.println(percent+"%\t\t|| processing file :" + file.getAbsolutePath());

				//defining file ID
				String parentName = getParentFolderName(file.getAbsolutePath());
				id = getParentFolderValue(parentName);

				// id will be parentFolderNameValue + fileName
				fileID = (id +" "+ file.getName()).trim();

				fileN.put(fileID, new Integer(0));

				indexing.setCurrentDocID(fileID);


				//reading file and passing tokens to next pipe stages
				br = new BufferedReader(new FileReader(file));
				String pattern= "^[\\w-]+:\\s.*";

				while ((line = br.readLine()) != null) {
					if(line.length() > 0){
						if(!line.matches(pattern)){
							tokens = line.split("\\s");

							if(!fileN.containsKey(fileID.trim())){
								fileN.put(fileID.trim(), tokens.length);
							}else{
								nd = fileN.get(fileID).intValue();
								fileN.put(fileID.trim(), new Integer(tokens.length+nd));

							}

							for (int i = 0; i < tokens.length; i++) {
								inputFileWriter.write(tokens[i] + "\n");
								inputFileWriter.flush();
							}
						}
					}
				}

			}

			writeLengthFile();
			indexing.backup();

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

			mergeBlocks(indexing.getBlocks());

			long end = System.currentTimeMillis();
			System.out.println("reading files DONE in ");
			System.out.println((end - start) / 1000 + " seconds");
			isRunning = false;

			try {
				pool.awaitTermination(2000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private synchronized void writeLengthFile() {

		System.out.println("writing lenght file");
		PrintWriter out=null;

		try{

			File outputfile= new File("./dictionary/length.txt"); 

			out = new PrintWriter(new BufferedWriter(new FileWriter(outputfile, false)));


			JSONObject json;
			for(String id: fileN.keySet()){
				json= new JSONObject();
				json.put("docID", id.trim());
				json.put("length", fileN.get(id));

				out.println(json.toString());
			}

		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			e.printStackTrace();

		}
		finally
		{
			if(out!=null){
				out.close();
			}

			ApplicationStatus.getInstance().setLength(fileN);
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
