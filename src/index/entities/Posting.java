package index.entities;

import org.json.JSONObject;
import org.json.JSONString;



/***
 * 
 * @author F
 * 
 */

public class Posting implements Comparable<Posting>, JSONString {

	private String docID;
	private int documentFrequency = 1;

	public Posting(String docID) {
		this.docID = docID;
	}
	
	public Posting(JSONObject json){
		docID = json.getString("docID");
		documentFrequency= json.getInt("docFrequency");
	} 

	public String getDocID() {
		return docID;
	}

	public int getDocumentFrequency() {
		return documentFrequency;
	}

	public void updateDocumentFrequency() {
		documentFrequency++;
	}

	public String toString() {
		return "{" + docID + " , " + documentFrequency + "}";

	}

	public boolean merge(Posting p) {
		
		if (p.getDocID().equals(this.docID)) {
			this.documentFrequency += p.getDocumentFrequency();
			return true;
		}
		else return false;
	}
	

	@Override
	public int compareTo(Posting o) {
		return o.getDocID().compareTo(this.docID);
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
