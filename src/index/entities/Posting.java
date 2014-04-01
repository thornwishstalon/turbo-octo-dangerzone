package index.entities;

/***
 * 
 * @author F
 *
 */
public class Posting implements Comparable<Posting>{
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
	
}
