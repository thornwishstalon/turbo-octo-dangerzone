package index.entities;

/***
 * 
 * @author F
 *
 */
public class Posting {
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
	
}
