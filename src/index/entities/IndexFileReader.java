package index.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class IndexFileReader {
	private static Logger logger= LogManager.getLogger("IndexFileReader");
	
	public static TreeMap<String, PostingList> readBlock(String filePath){

		BufferedReader br=null;
		String line;
		JSONObject json;
		PostingList pl;
		TreeMap<String, PostingList> index= new TreeMap<String, PostingList>();

		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				json= new JSONObject(line);
				pl= new PostingList(json);
				index.put(pl.getTerm(), pl);
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(e.getMessage());
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}


		return index;

	}
	
}
