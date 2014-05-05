package index.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.TreeMap;

import main.input.settings.ApplicationSetup;

public class IndexFileWriter {

	public static synchronized String writeToDisk(TreeMap<String, PostingList> block, String filename) {
		//System.out.println("write to block");
		PrintWriter out=null;

		try{

			File outputfile= new File(filename); 

			out = new PrintWriter(new BufferedWriter(new FileWriter(outputfile, false)));


			Iterator<String> it= block.keySet().iterator();
			String term;
			String s="";
			while(it.hasNext()){

				term= it.next();
				//s="{"+term+ block.get(term).toString()+"}";
				s= block.get(term).toJSONString();
				//for debug reason currently
				//System.out.println(s);

				//out.println(s); //append to files
				out.println(s);
			}


		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			e.printStackTrace();
			filename=null;
		}
		finally
		{
			if(out!=null){
				out.close();
			}


		}
		return filename;
		//mark for future merge
		//pushBlockForMerge(file.getName());

		//return file;

	}

	public static String writeToDisk(TreeMap<String, PostingList> block, int blockID) {

		String filename;

		if(ApplicationSetup.getInstance().getUseBigrams())
		{
			filename= "./dictionary/bigram_index_b"+blockID+".txt";
		}else filename= "./dictionary/index_b"+blockID+".txt";
		return writeToDisk(block, filename);
	}

}
