package index.entities;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;
import org.json.JSONString;


/***
 * 
 * @author F
 *
 */
public class PostingList implements JSONString{
	private int SIZE= 1024;
	private int currentPosition=0;
	private int overallFrequency=0;
	private String term;
	private Posting[] postingList;
	
	
	public PostingList(String term){
		this.term = term;
		postingList= new Posting[SIZE];
	}
	
	public void addToList(Posting p)
	{
		//System.out.println("added "+p.toString()+"to dictionary");
		
//		if(currentPosition >= SIZE)
//			doublePostingList();
		
		if(currentPosition > 0 ){
			//check if documentID is already present and update documentfrequency
			if(postingList[currentPosition-1].getDocID() == p.getDocID()){
				postingList[currentPosition-1].updateDocumentFrequency();
				
			}else{
				postingList[currentPosition]=p;
				currentPosition++;
			}
		}else{ 	
			postingList[currentPosition]=p;
			currentPosition++;	
		}
		overallFrequency++;
		
		//System.out.println(postingList[currentPosition].toString());
		
	}
	
	public int getDocumentFrequency(String term, int docID)
	{
		for(Posting p:postingList)
		{
			if(p.getDocID() == docID)
				return p.getDocumentFrequency();
		}
		return -1; // not found
	}
	
	public int getOverallFrequency(String term)
	{
		return overallFrequency;
	}
	
	
	
	public String toString()
	{
		String out="{"+overallFrequency+" ";
		
		for(int i=0;i<currentPosition;i++){
			out+= postingList[i].toString();
		}
		
		return out+"}";
	}
	
	public Posting[] getPostings()
	{
		return postingList;
	}
	
	public Posting[] merge(PostingList p)
	{
		Posting[] tmp = combine(p.getPostings(), this.postingList);
		Arrays.sort(tmp);
		ArrayList<Posting> merged= new ArrayList<>();
		
		Posting posting;
		for(int i=0; i<tmp.length-1; i++)
		{
			posting= tmp[i];
			posting.merge(tmp[i]);
			merged.add(posting);
		}
		tmp= new Posting[merged.size()];
		merged.toArray(tmp);
		
		return tmp;
	}
	
	private  Posting[] combine(Posting[] a, Posting[] b){
        int length = a.length + b.length;
        Posting[] result = new Posting[length];
        
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

	
	@Override
	public String toJSONString() {
		JSONObject json= new JSONObject();
		json.put("term", term);
		json.put("overallFrequency", overallFrequency);
		json.put("postings", postingList);
		
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
