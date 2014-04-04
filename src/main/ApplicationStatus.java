package main;

import index.entities.IndexFileReader;
import index.entities.Posting;
import index.entities.PostingList;









import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.json.JSONObject;

import query.Query;
import main.input.settings.ApplicationSetup;

public class ApplicationStatus {
	private static ApplicationStatus instance = null;
	private TreeMap<String, PostingList> index;
	private boolean indexIsSet=false;
	private int Nd=0;
	private TreeMap<String, Integer> length;

	private ArrayList<PostingList> lists;
	private HashMap<String, Query > queryTerms;
	private HashMap<String, Float> scores;

	private ApplicationStatus() {
		lists = new ArrayList<PostingList>();
		queryTerms = new HashMap<String, Query>();
		scores= new HashMap<String, Float>();
		length= new TreeMap<String, Integer>();
	}

	public static ApplicationStatus getInstance() {
		if (instance == null)
			instance = new ApplicationStatus();
		return instance;
	}

	public void setN(int n){
		this.Nd = n;
	}
	
	public void setLength(TreeMap<String, Integer> map){
		this.length= map;
	}

	public void readIndex()
	{
		String filename;

		if(ApplicationSetup.getInstance().getUseBigrams())
		{
			filename= "./dictionary/bigram_index.txt";
		}else filename= "./dictionary/index.txt";

		index= IndexFileReader.readBlock(filename);
		if(index.size() == 0)
		{
			System.out.println("WARNING: you need to initialize your indices first! use the '!buildVoc' command");
		}else{
			System.out.println("index-file read. ready for queries!");
			indexIsSet=true;
		}
		
		readLength();
		

	}
	
	

	public synchronized void doRanking()
	{
		System.out.println("doing ranking");
		
		calculate_tf_idf_q();
		Query q;
		for(String term: queryTerms.keySet()){
			System.out.println(term);
			
			q= queryTerms.get(term);
			q.setPostings(getPostingsFor(term));
			
			System.out.println("found "+ q.getPostings().getPostings().size() +" for"+ term);
			for(Posting p: q.getPostings().getPostings()){
				double value = q.getTf_idf() * calculate_tf_idf_d(p, q.getPostings().getOverallFrequency(term));
				Float f = new Float(value);
				if(!scores.containsKey(p.getDocID())){
					scores.put(term, f);
				}else
				{
					float score=scores.get(term).floatValue();
					score+= value;
					scores.put(term, new Float(score));
					
				}
			}
		}
		
		Float newScore,oldScore,lengthV;
		float fnewScore,foldScore,flength;
		for(String id: scores.keySet())
		{
			System.out.println(id);
			System.out.println("llll: "+ length.get(id).toString());
			oldScore= scores.get(id);
			if(oldScore== null)
				oldScore=new Float(0f);
			
			try{
				newScore= new Float(oldScore.floatValue() / length.get(id).floatValue());
				scores.put(id, newScore);
				System.out.println(newScore.floatValue());
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	private void readLength(){
		System.out.println("reading length file");
		BufferedReader br=null;
		String line;
		JSONObject json;
		 

		try {
			br = new BufferedReader(new FileReader("./dictionary/length.txt"));
			while ((line = br.readLine()) != null) {
				json= new JSONObject(line);
				length.put(json.getString("docID").trim(), new Integer(json.getInt("length")));
				System.out.println(length.get(json.getString("docID").trim()));
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			//logger.error(e.getMessage());
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//logger.error(e.getMessage());
				}
			}
		}

	}
	
	

	public synchronized void addToQuery(String term) {
		
		if(!queryTerms.containsKey(term)){
			queryTerms.put(term, new Query() );
			
		}else{
			queryTerms.get(term).upgradeTF();
			
		}
	
	}
	
	private synchronized void calculate_tf_idf_q()
	{
		Query q=null;
		double N = queryTerms.size();
		
		for(String term: queryTerms.keySet()){
			q= queryTerms.get(term);
			
			q.setTf_idf(Math.log(1 + q.getTF() ) * Math.log(N/ q.getTF()));
		}
	}
	
	private double calculate_tf_idf_d(Posting p, int termF)
	{
		return Math.log(1 + termF )* Math.log(Nd/p.getDocumentFrequency());
	}
	


	public boolean indexIsSet(){
		return indexIsSet;
	}
	
	public PostingList getPostingsFor(String term){
		System.out.println("retrieve postings");
		if(index.containsKey(term.trim())){
			return index.get(term);
		}else return null;

	}

	public synchronized void printResults() {
		System.out.println(scores.size());
		for(String id: scores.keySet()){
			System.out.println(id + " : "+ scores.get(id));
		}
		
	}


}
