package index.entities;

import org.json.JSONObject;
import org.json.JSONString;



/***
 * 
 * @author F
 *
 */
public class Posting implements Comparable<Posting>, JSONString {
	private int docID;
	private int documentFrequency=1;
	
	public Posting(int docID)
	{
		this.docID=docID;
	}
	
	public int getDocID() {
		return docID;
	}
	
	public int getDocumentFrequency() {
		return documentFrequency;
	}
	
	public void updateDocumentFrequency(){
		documentFrequency++;
	}
	
	public String toString(){
		return "{"+docID+" , " + documentFrequency+"}";
		
	}
	
	public Posting merge(Posting p)
	{
		if(p.getDocID() == this.docID)
		{
			this.documentFrequency += p.getDocumentFrequency();
			return this;

		}
		return null;
	}

	@Override
	public int compareTo(Posting o) {
		if(o.getDocID() == this.docID)
			return 0;
		else if(o.getDocID() > this.docID)
			return 1;
		else return -1;
		
	}

	@Override
	public String toJSONString() {
		JSONObject json = new JSONObject();
		json.put("docID", docID);
		json.put("docFrequency", documentFrequency);
		
		//return "{\"docID\":\""+docID+"\", \"documentFrequency\":\""+documentFrequency+"\"}";
		return json.toString();
	}
	
}
