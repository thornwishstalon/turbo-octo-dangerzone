package processor.pipe;

import java.io.PipedReader;
import java.io.PipedWriter;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Filter extends AbstractPipeStage {
	// private String pattern="[^a-zA-Z0-9\\-\\'\\@]+";
	private String replacePattern;
	//private Matcher match;
	private static Logger log = LogManager.getLogger("character filtering");

	public Filter(PipedReader in, PipedWriter out) {
		super(in, out);

		replacePattern ="[^\\w\\d]+";
	}

	@Override
	public String process(String input) {
		 	
	        String start;
	        start=input;
	        
	        input= input.replaceAll(replacePattern, "");
	        
	       log.info(start+ "->"+input);
	       if(input!=null)
	    	   return input.trim();
	       else return "";
	}

	@Override
	protected void success() {

		super.success();
		//System.out.println("Filter DONE");
	}

}
