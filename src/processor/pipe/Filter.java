package processor.pipe;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Filter extends AbstractPipeStage {
	// private String pattern="[^a-zA-Z0-9\\-\\'\\@]+";
	private String replacePattern;
	private String pattern;
	//private Matcher match;
	private static Logger log = LogManager.getLogger("character filtering");

	public Filter(PipedReader in, PipedWriter out) {
		super(in, out);

		pattern = "[\\W\\.\\-\\<\\>]+";
	}

	@Override
	public String process(String input) {
		 	//System.out.println("filter:"+input);
		 	
	       // match = pattern.matcher(input);
	        String s,start;
	        start=input;
	        
	        //input= input.replaceAll("[^\\w\\d]+", "");
	        if(input.matches(pattern))
	        	return "";
	        else return input;
	        
	        /*
	        while(match.find())
	        {
	        	s= match.group();
	            input=input.replaceAll( s, "");
	        }
	        */
//	       log.info(start+ "->"+input);
//	       if(input!=null)
//	    	   return input.trim();
//	       else return "";
	}

	@Override
	protected void success() {

		super.success();
		//System.out.println("Filter DONE");
	}

}
