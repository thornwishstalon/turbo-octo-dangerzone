package index.entities;

/**
 * 
 * @author F
 *
 */
public class Token {
	private String term;
	private int docID;
	
	public Token(String term, String docID)
	{
		this.term= term;
		this.docID= Integer.valueOf(docID);
		
	}
	
	public Token(String readLine) {
		// TODO Auto-generated constructor stub
		String[] tmp = readLine.split("_");
		term= tmp[0];
		docID= Integer.valueOf(tmp[1]);
	}

	public String getTerm() {
		return term;
	}
	public int getDocID() {
		return docID;
	}
	
	public String toString()
	{
		return term+"_"+docID;
	}
	
	
}
