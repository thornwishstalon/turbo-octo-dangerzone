package processor;

import index.entities.IndexFileReader;
import index.entities.IndexFileWriter;
import index.entities.PostingList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import main.ApplicationStatus;
import main.input.settings.ApplicationSetup;

import org.json.JSONObject;

public class BlockMerger extends Thread {
	private ArrayList<String> list;
	private HashMap<String, Boolean> map;

	public BlockMerger(ArrayList<String> blocks){
		list=blocks;
		map= new HashMap<String, Boolean>();
	}

	public void run(){
		System.out.println("merging blocks...");
		long start = System.currentTimeMillis();
		
		TreeMap<String, PostingList> a;
		TreeMap<String, PostingList> b;
		String merged="";
		String afile, bfile;
		Iterator<String> iterator;
		ArrayList<String> toBeRemoved= new ArrayList<String>();
		for(int i=0; i<list.size();i++){
			map.put(list.get(i), false);
		}

		Set<String> l;
		int c = 0;  
		while(map.size()>1){
			if(list.size()==1){
				break;
				
			}

			l= map.keySet();
			iterator = l.iterator();

			afile = iterator.next();
			bfile= iterator.next();

			//System.out.println("merging: "+afile+" and "+bfile);

			a = readBlock(afile);
			b = readBlock(bfile);

			for(String key: b.keySet())
			{
				if(a.containsKey(key))
				{
					//System.out.println("merge posting list");
					a.get(key).merge(b.get(key));
				}
				else{
				//	System.out.println("add posting");
					a.put(key, b.get(key));
				}
			}

			merged= IndexFileWriter.writeToDisk(a, list.get(c).replaceFirst("\\.txt", "_merged.txt"));
			map.remove(afile);
			map.remove(bfile);

			if(map.size()>2){
				toBeRemoved.add(afile);
				toBeRemoved.add(bfile);
			}else
			{
				toBeRemoved.add(bfile);

			}
			

			map.put(merged, true);
			
		}
		
		//removing merged block files and renaming final block to index.txt
		System.out.println("FINAL ::"+merged);
		String filename;
		
		if(ApplicationSetup.getInstance().getUseBigrams())
		{
			filename= "./dictionary/bigram_index.txt";
		}else filename= "./dictionary/index.txt";
		
		File index = new File(merged);
		index.renameTo(new File(filename));
		
		for(String s: toBeRemoved )
		{
			if(!s.equals(merged) || !s.equals(filename)){
			//	remove files!!
				File f= new File(s);
				f.delete();
			}
			
			
		}
		
		long end = System.currentTimeMillis();
		System.out.println("merging blocks in "+(end-start)/1000 + " seconds");
		
		ApplicationStatus.getInstance().readIndex();
		System.out.println("ready for queries!");
		
	}

	public TreeMap<String, PostingList> readBlock(String filePath){
		return IndexFileReader.readBlock(filePath);

	}
}
