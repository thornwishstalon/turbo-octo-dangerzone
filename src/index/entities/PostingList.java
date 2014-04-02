package index.entities;

import java.util.ArrayList;

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

	public int getOverallFrequency(String term) {
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
		System.out.println("try to merge posting");
		System.out.println("B:\n"+p.toString());
		
		for(Posting postingA: postingList){
			for(Posting postingB: p.getPostings()){
				if(postingA.getDocID().equals(postingB.getDocID())){
					postingA.merge(postingB);
					System.out.println("merged a and b");
				}
			}
		}
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
