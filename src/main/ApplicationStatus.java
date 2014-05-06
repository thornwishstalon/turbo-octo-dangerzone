package main;

import index.entities.IndexFileReader;
import index.entities.Posting;
import index.entities.PostingList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.TreeMap;

import main.input.settings.ApplicationSetup;

import org.json.JSONObject;

import query.Query;
import query.Score;

public class ApplicationStatus extends Observable{
	public final static int TFID=0;
	public final static int DEFAULT_LUCENE=1;
	public final static int BM25L=2;
	public final static int BM25=3;
	
	
	private static ApplicationStatus instance = null;
	private TreeMap<String, PostingList> index;
	private boolean indexIsSet=false;
	private int Nd=8000;
	private TreeMap<String, Integer> length;

	private ArrayList<PostingList> lists;
	private HashMap<String, Query > queryTerms;
	private HashMap<String, Score> scores;
	private String topic;
	private boolean iScriptRunning=false;
	
	private ApplicationStatus() {
		lists = new ArrayList<PostingList>();
		queryTerms = new HashMap<String, Query>();
		scores= new HashMap<String, Score>();
		length= new TreeMap<String, Integer>();
	}
	
	public void setTopic(String topicnr){
		this.topic = topicnr;
	}

	public static ApplicationStatus getInstance() {
		if (instance == null)
			instance = new ApplicationStatus();
		return instance;
	}

	public void setN(int n){
		this.Nd = n;
		System.out.println("set N "+n);
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
		if(Nd==0){
			Nd= length.size();
		}

	}
	
	public void clear(){
		scores= new HashMap<String, Score>();
		queryTerms = new HashMap<String, Query>();
	}

	public synchronized void doRanking()
	{
		//System.out.println("doing ranking");
		
		
		
		calculate_tf_idf_q();
		Query q;
		for(String term: queryTerms.keySet()){
			//System.out.println(term);
			term= term.trim();
			q= queryTerms.get(term);
			q.setPostings(getPostingsFor(term));
			if(q.getPostings()== null){
				System.out.println("sry. no postings found");
				continue;
			}
			 //System.out.println("found "+ q.getPostings().getPostings().size() +" for"+ term);
			for(Posting p: q.getPostings().getPostings()){
				
				float value = (float) (q.getTf_idf() * calculate_tf_idf_d(p, q.getPostings().getOverallFrequency()));
				
				Score s= new Score();
				s.setScore(value);
				s.setId(p.getDocID());
				//System.out.println("id"+ p.getDocID()+" -> "+value);
				
				if(!scores.containsKey(p.getDocID())){
					scores.put(p.getDocID(), s);
				}else
				{
					Score sc = scores.get(p.getDocID());
					float score= sc.getScore();
					score+= value;
					sc.setScore(score);
					scores.put(p.getDocID(),sc );
					
				}
			}
			
			
		}
		
		float newScore;
		
		Score s;
		for(String id: scores.keySet())
		{
			s= scores.get(id);
			try{
				newScore= s.getScore() / length.get(id).floatValue();
				s.setScore(newScore);
				scores.put(id, s);
				//System.out.println(newScore.floatValue());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			//order by score  and select top k files
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
				//System.out.println(length.get(json.getString("docID").trim()));
				
			}

			System.out.println("lenght size: "+length.size());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//logger.error(e.getMessage());
			//e.printStackTrace();
			//System.out.println("build your index first! use the !buildVoc command");
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
			/*
<<<<<<< HEAD
			
			//q.setTf_idf(Math.log(1 + q.getTF() ) * Math.log( N / q.getTF() ));
			q.setTf_idf(Math.log(1 + q.getTF() ) * Math.log( Nd / (overallTermFrequency) ));
=======
*/
			
			// ORIGINAL
			//q.setTf_idf(Math.log(1 + q.getTF() ) * Math.log( N / q.getTF() ));
			
			// ADDED BY PARIS
			double overallTermFrequency = getTermFrequency(term) ;
			q.setTf_idf(Math.log(1 + q.getTF() ) * Math.log( Nd / overallTermFrequency ));
			
//>>>>>>> 26bee01f93b1cb087e87e9f42197c02bdb91bc62
			//System.out.println(q.getTf_idf());
		}
	}
	
	private double calculate_tf_idf_d(Posting p, int termF)
	{
		double v=Math.log(1 + p.getDocumentFrequency() ) * Math.log(Nd/(double)termF);
		
		return v;
	}
	
	private double getTermFrequency(String term){
		if(index.get(term)!=null)
		 return index.get(term).getOverallFrequency();
		else return 1; // avoid divide by zero if term is not in corpus

	}


	public boolean indexIsSet(){
		return indexIsSet;
	}
	
	public PostingList getPostingsFor(String term){
		//System.out.println("retrieve postings");
		if(index.containsKey(term.trim())){
			return index.get(term);
		}else return null;

	}
	
	public void clearScoreList()
	{
		clear();
	}
	
	public void addScoreFromLucene(String docID, float score)
	{
		Score s= new Score();
		s.setId(docID);
		s.setScore(score);
		
		scores.put(docID, s);
	}
	
	
	public synchronized void printResults() {
		//System.out.println(scores.size());
		ArrayList<Score> sco= new ArrayList<Score>();
		String TAG =ApplicationSetup.getInstance().getTrecTag();
		String resultLine,result="";
		

		int from;
		
		ApplicationSetup setup=ApplicationSetup.getInstance();
		if(!setup.getUseLucene()){
			from= TFID;
			//TAG+="_tfidf";
		}
		else if(setup.getUseBM25()){
			from= BM25;
			//TAG+="_bm25";
		}
		else if(setup.getUseBM25L()){
			from= BM25L;
			//TAG+="_bm25l";
		}
		else{
			from= DEFAULT_LUCENE;
			//TAG+="_lucene";
		}
		
		
		
		
		for(String id: scores.keySet()){
			//System.out.println(id + " : "+ scores.get(id));
			sco.add(scores.get(id));
			
		}
		
		java.util.Collections.sort(sco);
		String filename;
		Score score;
		
		for(int i = 0; i< Math.min(sco.size(),100); i++){
			
			score= sco.get(i);
			if(score.getScore() == Float.NEGATIVE_INFINITY)
				//break;
			if(sco.size()<= i)
				break;
			
			filename=resolve(score.getId());
			resultLine= "topic"+topic+" Q0 "+" "+filename+" "
					 + i + " "+score.getScore() +" "+ TAG ;
			System.out.println(resultLine);
			result += resultLine+"\n";
		}
		
		
		//store result in file
		storeInFile(result, from,""+topic);
		
	}
	
	public void storeInFile(String result, int from, String topic) {
		System.out.println("\nwriting result to file");
		topic= topic.trim();
		
		String filename="";
		switch (from) {
		case TFID:
			filename= "tfidf_result_t"+topic+".txt";
			break;
		case DEFAULT_LUCENE:
			filename= "defaultLucene_result_t"+topic+".txt";
			break;
		case BM25L:
			filename= "bm25l_result_t"+topic+".txt";
			break;
		case BM25:
			filename= "bm25_result_t"+topic+".txt";
			break;
			
		default:
			filename="result.txt";
			break;
		}
		
		
		File file= new File( ApplicationSetup.getInstance().getResultPath());
		if(!Files.exists(file.toPath(), LinkOption.NOFOLLOW_LINKS)){
			file.mkdir();
		}
		
		PrintWriter out=null;

		try {

			File outputfile= new File(file.getAbsolutePath()+"/"+filename); 
			out = new PrintWriter(new BufferedWriter(new FileWriter(outputfile, false)));
			out.print(result);

		} catch (IOException e) {
			//exception handling left as an exercise for the reader
			e.printStackTrace();
		} finally {
			if(out!=null){
				out.close();
			}
		}
		
	}

	public String resolve(String id){
		String[] tokens= id.split(" ");
		
		if(tokens[0].equals("1")){
			return "alt.atheism/"+tokens[1];
		 
		}else if(tokens[0].equals("2")){
			return "comp.graphics/"+tokens[1];
		 
		}if(tokens[0].equals("3")){
			return "comp.os.ms-windows.misc/"+tokens[1];
		 
		}else if(tokens[0].equals("4")){
			return "comp.sys.ibm.pc.hardware/"+tokens[1];
		 
		}else if(tokens[0].equals("5")){
			return "comp.sys.mac.hardware/"+tokens[1];
		 
		}else if(tokens[0].equals("6")){
			return "comp.windows.x/"+tokens[1];
		 
		}else if(tokens[0].equals("7")){
			return "misc.forsale/"+tokens[1];
		 
		}if(tokens[0].equals("8")){
			return "rec.autos/"+tokens[1];
		 
		}else if(tokens[0].equals("9")){
			return "rec.motorcycles/"+tokens[1];
		 
		}else if(tokens[0].equals("10")){
			return "rec.sport.baseball/"+tokens[1];
		 
		}if(tokens[0].equals("11")){
			return "rec.sport.hockey/"+tokens[1];
		 
		}else if(tokens[0].equals("12")){
			return "sci.crypt/"+tokens[1];
		 
		}else if(tokens[0].equals("13")){
			return "sci.electronics/"+tokens[1];
		 
		}if(tokens[0].equals("14")){
			return "sci.med/"+tokens[1];
		 
		}else if(tokens[0].equals("15")){
			return "sci.space/"+tokens[1];
		 
		}else if(tokens[0].equals("16")){
			return "soc.religion.christian/"+tokens[1];
		 
		}if(tokens[0].equals("17")){
			return "talk.politics.guns/"+tokens[1];
		 
		}else if(tokens[0].equals("18")){
			return "talk.politics.mideast/"+tokens[1];
		 
		}else if(tokens[0].equals("19")){
			return "talk.politics.misc/"+tokens[1];
		 
		}
		else if(tokens[0].equals("20")){
			return "talk.religion.misc/"+tokens[1];
		 
		}
		
		return "";
	}
	
	public boolean isiScriptRunning() {
		return iScriptRunning;
	}

	public void setiScriptRunning(boolean iScriptRunning) {
		this.iScriptRunning = iScriptRunning;
	}
	
	@Override
	public void notifyObservers() {
		//System.out.println("notify observer");
		// TODO Auto-generated method stub
		if(iScriptRunning){
			setChanged();
			super.notifyObservers();
		}
	}
	

}
