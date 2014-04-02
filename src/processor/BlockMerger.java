package processor;

import index.entities.IndexFileWriter;
import index.entities.PostingList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONObject;

public class BlockMerger extends Thread {
	private ArrayList<String> list;
	private HashMap<String, Boolean> map;
	
	public BlockMerger(ArrayList<String> blocks){
		list=blocks;
		map= new HashMap<String, Boolean>();
	}
	
	public void run(){
		
		TreeMap<String, PostingList> a;
		TreeMap<String, PostingList> b;
		String merged="";
		String afile, bfile;
		Iterator<String> iterator;
		
		for(int i=0; i<list.size();i++){
			map.put(list.get(i), false);
		}
		
		Set<String> l;
		int c = 0;  
		while(map.size()>1){
			if(list.size()==1)
				break;
			
			l= map.keySet();
			iterator = l.iterator();
			
			afile = iterator.next();
			bfile= iterator.next();
			
			System.out.println("merging: "+afile+" and "+bfile);
			
			a = readBlock(afile);
			b = readBlock(bfile);
			
			for(String key: b.keySet())
			{
				if(a.containsKey(key))
				{
					a.get(key).merge(b.get(key));
				}
				a.put(key, b.get(key));
			}
			
			merged= IndexFileWriter.writeToDisk(a, list.get(c).replaceFirst("\\.txt", "_merged.txt"));
			map.remove(afile);
			map.remove(bfile);
			
			
			map.put(merged, true);
			
			
		}
		System.out.println(merged);
		
	}
	
	public TreeMap<String, PostingList> readBlock(String filePath){
		
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
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return index;
		
	}
}
