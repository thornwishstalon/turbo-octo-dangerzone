package processor.pipe;

import java.io.PipedReader;
import java.io.PipedWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Filter extends AbstractPipeStage {
	// private String pattern="[^a-zA-Z0-9\\-\\'\\@]+";
	private String replacePattern = "[\\W_-]+";
	//private String pattern;
	//private Matcher match;
	private static Logger log = LogManager.getLogger("character filtering");

	public Filter(PipedReader in, PipedWriter out) {
		super(in, out);
		log.info("filtering active");
	}

	@Override
	public String process(String input) {
		    //System.out.println("filtering "  + input);
	        input= input.replaceAll(replacePattern, "");
	        
	        //ignore terms with digits in it
	        if(input.matches(".*\\d+.*"))
	        	input="";
	        
	        return input;
	        
	}

	@Override
	protected void success() {

		super.success();
		//System.out.println("Filter DONE");
	}

}
