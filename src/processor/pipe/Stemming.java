package processor.pipe;


import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;



public class Stemming extends AbstractPipeStage {

	private SnowballStemmer stemmer;
	private static Logger logger= LogManager.getLogger("Stemming");

	public Stemming(PipedReader in, PipedWriter out) {
		super(in,out);

		stemmer= new englishStemmer();

		Class stemClass=null;
		try {
			stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer"); //english only!!
			stemmer = (SnowballStemmer) stemClass.newInstance();

		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		}
	}


	@Override
	public String process(String input) {
		
		String out="";
		
		if(input != null){
			stemmer.setCurrent(input);
			stemmer.stem();
			out= stemmer.getCurrent();
			System.out.println("stemmer processing : "+ out);
			return out;
		}
		else return "";
	}

}
