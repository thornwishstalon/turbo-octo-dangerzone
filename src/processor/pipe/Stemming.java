package processor.pipe;

import java.io.PipedReader;
import java.io.PipedWriter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
//import org.tartarus.snowball.ext.PorterStemmer;
//import org.tartarus.snowball.SnowballStemmer;
//import org.tartarus.snowball.ext.englishStemmer;

import stemmer.Stemmer;

public class Stemming extends AbstractPipeStage {

	//private SnowballStemmer stemmer;
	private Stemmer stemmer;
	private static Logger logger = LogManager.getLogger("Stemming");

	public Stemming(PipedReader in, PipedWriter out) {
		super(in, out);
		
		 stemmer= new Stemmer();
		logger.info("stemming active");
		/*
		stemmer = new englishStemmer();

		Class stemClass = null;
		try {
			stemClass = Class
					.forName("org.tartarus.snowball.ext.englishStemmer"); 	// english
																			// only!!
			stemmer = (SnowballStemmer) stemClass.newInstance();


		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally{
			logger.info("stemming active");
		}
		*/
	}

	@Override
	public String process(String input) {
	
		if (input != null) {
			char[] w= input.toCharArray();
			
			for(int c = 0; c < w.length;c++){
				stemmer.add(w[c]);
			}
			stemmer.stem();
			
			return stemmer.toString();

		} else
			return "";
	}

}

