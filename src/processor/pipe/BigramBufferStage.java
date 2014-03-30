package processor.pipe;

import java.io.PipedReader;
import java.io.PipedWriter;

public class BigramBufferStage extends AbstractPipeStage {

	private String last=null;
	public BigramBufferStage(PipedReader in, PipedWriter out) {
		super(in, out);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String process(String input) {
		// TODO Auto-generated method stub
		if(last!=null)
		{
			last= input;
			return "";
		}
		if(!input.equals(last))
				return last+" "+input;
		else
		{
			return "";
		}
		
	}

}
