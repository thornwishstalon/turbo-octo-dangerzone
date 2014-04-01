package processor.pipe;

import java.io.PipedReader;
import java.io.PipedWriter;

public class CaseFolding extends AbstractPipeStage {

	public CaseFolding(PipedReader in, PipedWriter out) {
		super(in, out);
	}

	@Override
	public String process(String input) {
		// System.out.println("folding processing: "+input);
		return input.toLowerCase();
	}

}
