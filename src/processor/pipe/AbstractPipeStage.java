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
			//PrintWriter writer = new PrintWriter(out);
			
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
		} catch (IOException e) {
			logger.error(e.getMessage());
		}finally
		{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			backup();
		}
	}

	protected void backup() {
		
	}

	public abstract String process(String input);




}
