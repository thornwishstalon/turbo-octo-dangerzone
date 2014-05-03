package processor.pipe;

import java.io.PipedReader;
import java.io.PipedWriter;

public class BigramBufferStage extends AbstractPipeStage {

	private String last = null;
	private String s;

	public BigramBufferStage(PipedReader in, PipedWriter out) {
		super(in, out);
	}

	@Override
	public String process(String input) {
		
		if (last == null) {
			last = input;
			return "";
		}

		if (!input.equals(last)) {
			s = last + " " + input;
			last = input;
			return s;
		} else {
			return "";
		}

	}

}
