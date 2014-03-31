package processor.pipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public abstract class AbstractPipeStage extends Thread {
	protected PipedReader in;
	protected PipedWriter out;
	protected AbstractPipeStage waitingFor= null;
	protected boolean isDone=false;
	
	private static Logger logger = LogManager.getLogger("AbstractPipeStage");

	public AbstractPipeStage(PipedReader in, PipedWriter out)
	{
		this.in = in;
		this.out= out;
	}
	
	public PipedWriter getOut()
	{
		return out;
	}

	public void run()
	{
		
		String input=null;
		String intermediateResult=null;
		
		try {
			
			BufferedReader reader= new BufferedReader(in);
			
			while((input= reader.readLine())!=null)
			{
				
				intermediateResult=process(input);
				if(intermediateResult!=null)
				{
					if(intermediateResult.length() > 0){
						out.write(intermediateResult+"\n");
						out.flush();
					}
				}
			}
			
			isDone=true;
			
			
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally
		{
			success();
			while(!waitingFor.isDone())
			{
				//waiting
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			backup();
			
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	protected void success()
	{
		//
	}
	
	public void setWaitingFor(AbstractPipeStage a)
	{
		this.waitingFor=a;
	}
	
	
	protected boolean isDone()
	{
		return isDone;
	}

	protected void backup() {
		//backup hook
	}

	public abstract String process(String input);




}
