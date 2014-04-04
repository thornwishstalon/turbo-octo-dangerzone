package index.entities;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;


/***
 * 
 * @author F
 * 
 */

public class PostingList implements JSONString{
	
	private int currentPosition=0;
	private int overallFrequency=0;
	private String term;
	private ArrayList<Posting> postingList;
	private static Logger log= LogManager.getLogger("PostingLIST");
	
	public PostingList(String term){
		this.term = term;
		postingList= new ArrayList<>();
	}
	
	public PostingList(JSONObject json){
		this.term = json.getString("term");
		this.overallFrequency = json.getInt("overallFrequency");
		
		JSONArray tmp=json.getJSONArray("postings");		
		postingList = new ArrayList<>();
		Posting p;
		for(int i=0; i< tmp.length();i++){
			p = new Posting(tmp.getJSONObject(i));
			postingList.add(p);
		}
		currentPosition = postingList.size();
		
		
	}
	
	public void addToList(Posting p){
		//System.out.println("added "+p.toString()+"to dictionary");
		
//		if(currentPosition >= SIZE)
//			doublePostingList();
		
		if(currentPosition > 0 ){
			//check if documentID is already present and update documentfrequency
			if(postingList.get(currentPosition-1).getDocID() == p.getDocID()){
				postingList.get(currentPosition-1).updateDocumentFrequency();
				
			}else{
				postingList.add(p);
				currentPosition++;
			}
		}else{ 	
			postingList.add(p);
			currentPosition++;	
		}
		overallFrequency++;

		// System.out.println(postingList[currentPosition].toString());

	}
	
	public String getTerm(){
		return term;
	}

	public int getDocumentFrequency(String term, int docID) {
		for (Posting p : postingList) {
			if (p.getDocID().equals(docID))
				return p.getDocumentFrequency();
		}
		return -1; // not found
	}

	public int getOverallFrequency() {
		return overallFrequency;
	}

	
	
	public String toString(){
		String out="{"+overallFrequency+" ";
		
		for(Posting p: postingList){
			out+= p.toString();
		}

		return out + "}";
	}
	
	public ArrayList<Posting> getPostings(){
		return postingList;
	}
	
	public void merge(PostingList p){
		try{
		//System.out.println("try to merge posting");
		//System.out.println("B:\n"+p.toString());
		
		//init stuff
		HashMap<String, Posting> mapA= new HashMap<String, Posting>();
		HashMap<String, Posting> mapB= new HashMap<String, Posting>();
		
		for(Posting tmp: postingList)
		{
			mapA.put(tmp.getDocID(), tmp);
		}
		
		for(Posting tmp: p.getPostings())
		{
			mapB.put(tmp.getDocID(), tmp);
		}
		
		
		//actual merging
		Posting tmp;
		for(String key: mapB.keySet())
		{
			tmp= mapB.get(key);
			//System.out.println("tmp: "+tmp.toString());
			
			if(mapA.containsKey(key)){
				mapA.get(key).merge(tmp);
				//System.out.println("merged postings");
			}else
			{
				
				mapA.put(key, tmp);
			}
			
			
		}
		postingList = new ArrayList<Posting>();
		for(String key:mapA.keySet())
		{
			postingList.add(mapA.get(key));
		}
		
		updateOverallFrequency();
		}catch(Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void updateOverallFrequency()
	{
		int tf=0;
		for(Posting p:postingList)
		{
			tf+= p.getDocumentFrequency();
		}
		overallFrequency= tf;
		
	}
	
	
	@Override
	public String toJSONString() {
		JSONObject json= new JSONObject();
		json.put("postings", postingList);		
		json.put("overallFrequency", overallFrequency);
		json.put("term", term);
		
		return json.toString();
//		String json ="{\"term\":"+term+"\", \"overallFrequency\":"+overallFrequency+"\"postings\": [";
//		for(Posting p: postingList)
//		{
//			json += p.toJSONString();
//		}
//		json += "]}";
//		return json;
	}



}
