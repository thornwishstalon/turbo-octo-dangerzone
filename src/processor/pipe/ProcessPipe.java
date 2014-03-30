package processor.pipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.input.settings.ApplicationSetup;
import reader.Reader;

public class ProcessPipe extends Thread {

	private ExecutorService pool;
	private boolean isRunning=false;
	private ArrayList<AbstractPipeStage> stages;
	private Indexing indexing; 
	
	public ProcessPipe() {
		
		stages= new ArrayList<AbstractPipeStage>();
		
	}
	
	private void init(PipedWriter inputStart) throws IOException
	{
		int index=0;
		stages.add(new CaseFolding(new PipedReader(inputStart), new PipedWriter()));
		if(ApplicationSetup.getInstance().getUseStopwords())
		{
			stages.add(new StopWordRemoval(new PipedReader(stages.get(index).getOut()), new PipedWriter()));
			index++;
		}
		if(ApplicationSetup.getInstance().getUseStemmer())
		{
			stages.add(new Stemming(new PipedReader(stages.get(index).getOut()), new PipedWriter()));
			index++;
		}
		if(ApplicationSetup.getInstance().getUseBigrams())
		{
			stages.add(new BigramBufferStage(new PipedReader(stages.get(index).getOut()), new PipedWriter()));
			index++;
		}
		indexing= new Indexing(new PipedReader(stages.get(index).getOut()), new PipedWriter());
		
		stages.add(indexing);
		
		
		pool = Executors.newFixedThreadPool(stages.size());
		
		AbstractPipeStage stage;
		for(int i=0; i<stages.size(); i++)
		{
			stage= stages.get(i);
			if(i==0)
			{
				stage.setWaitingFor(stages.get(stages.size()-1));
			}
			else
			{
				stage.setWaitingFor(stages.get(i-1));
			}
			
			pool.execute(stage);
		}
		
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	@Override
	public void run() {
		isRunning=true;
		
		long start = System.currentTimeMillis();
		ArrayList<File> documents = new ArrayList<>();
		String path=ApplicationSetup.getInstance().getCorporaPath();
		System.out.println("Reading directory: " + path);
		Reader reader = new Reader(path);

		//stores all files in the arrayList
		reader.readFiles(documents);


		PipedWriter inputFileWriter=null;

		try {
			System.out.println("STARTING");
			
			inputFileWriter= new PipedWriter();

			init(inputFileWriter);
			
			BufferedReader br;
			String line;
			String[] tokens;
			int id=0; //for testing
			for(File f: documents)
			{
				//System.out.println("processing file :" +f.getAbsolutePath());
				indexing.setCurrentDocID(""+id++);
				br = new BufferedReader(new FileReader(f)); 
				while((line = br.readLine())!=null)
				{
					tokens= line.split("\\s");
					for(int i=0; i<tokens.length;i++)
					{
						inputFileWriter.write(tokens[i]+"\n");
						inputFileWriter.flush();
					}
				}
				


			}
			
			indexing.backup();


		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally
		{
			try {
				if(inputFileWriter!=null)
					inputFileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long end= System.currentTimeMillis();
			System.out.println("DONE");
			System.out.println((end-start)/1000 + " seconds");
			isRunning=false;
		}





	}

}
