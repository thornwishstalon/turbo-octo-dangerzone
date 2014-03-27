package stemming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.Exception;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.StringTokenizer;

import org.junit.runner.RunWith;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import sun.util.locale.StringTokenIterator;

public class SnowballStemmerWrapper extends Thread {

	private String input;
	private SnowballStemmer stemmer;
	private BufferedReader reader;
	private OutputStream outstream;

	public SnowballStemmerWrapper(String input) {
		this.input = input;
		
		stemmer= new englishStemmer();
		Class stemClass=null;
		try {
			stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer"); //english only!!
			stemmer = (SnowballStemmer) stemClass.newInstance();
		
			//runStringInput();
		
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public SnowballStemmerWrapper(BufferedReader reader, OutputStream outstream)
	{
		stemmer= new englishStemmer();
		
		Class stemClass=null;
		try {
			stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer"); //english only!!
			stemmer = (SnowballStemmer) stemClass.newInstance();
		
			this.reader=reader;
			this.outstream= outstream;
			run();
		

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public void run()
	{

		Writer output = new OutputStreamWriter(outstream);
		output = new BufferedWriter(output);

		String input=null;
		
		try{
			while ((input = reader.readLine()) != null) {
				
				stemmer.setCurrent(input);
				stemmer.stem();
				
				output.write(stemmer.getCurrent()+"\n"); //write line holding the stemmed and to lowercase transformed word
				
			}
			output.flush();
			//output.close();
			
		}catch(IOException e)
		{
			e.printStackTrace();
			//TODO
		}
		finally{
			try {
				if(output!=null)
					output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String runStringInput() {
		String output = "";
		
		try {
			if (input != null) {
				stemmer.setCurrent(input);
				stemmer.stem();
				output = stemmer.getCurrent();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return output;
	}

}
