package processor.pipe;

import java.io.BufferedReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import main.input.settings.ApplicationSetup;

public class StopWordRemoval extends AbstractPipeStage {
	private HashSet<String> stopwords;
	private static Logger logger= LogManager.getLogger("StopWordRemoval");
	
	public StopWordRemoval(PipedReader in, PipedWriter out) {
		super(in,out);
		stopwords = new HashSet<>();
		initStopwords();
	}

	@Override
	public String process(String input) {
		//System.out.println("stopword processing: "+input);
		if(isStopword(input))
			return "";
		else return input;
	}
	
	private boolean isStopword(String input)
	{
		if(stopwords.isEmpty())
			return false;
		
		if(stopwords.contains(input))
			return true;
		else return false;
	}
	
	private void initStopwords()
	{
		String docPath= ApplicationSetup.getInstance().getStopwordsPath();
		System.out.println(docPath);
		try {
			Path path = Paths.get(docPath);
			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("?"))
					stopwords.add(line);
			}
		}
		catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}

}
