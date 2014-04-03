package processor.pipe;


import java.io.PipedReader;
import java.io.PipedWriter;


import main.ApplicationStatus;

public class QueryLookStage extends AbstractPipeStage {
	

	
	public QueryLookStage(PipedReader in, PipedWriter out) {
		super(in, out);
		//queryTerms = new ArrayList<String>();
	
	}

	
	@Override
	public String process(String input) {
		//input is term we search for
		//System.out.println("query_token: "+input);
		ApplicationStatus.getInstance().doSearch(input.trim());
		
		return "";
	}

	

}
