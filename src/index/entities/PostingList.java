package index.entities;

/***
 * 
 * @author F
 *
 */
public class PostingList {
	private int SIZE= 1024;
	private int currentPosition=0;
	
	private Posting[] postingList;
	
	
	public PostingList(){
		postingList= new Posting[SIZE];
	}
	
	public void addToList(Posting p)
	{
		if(currentPosition >= SIZE)
			doublePostingList();
		
		if(currentPosition > 0 ){
			//check if documentID is present and update frequency
			if(postingList[currentPosition-1].getDocID() == p.getDocID()){
				postingList[currentPosition].updateDocumentFrequency();
			}else{
				postingList[currentPosition]=p;
			}
		}else 	postingList[currentPosition]=p;
	}
	
	public void doublePostingList()
	{
		SIZE = 2*SIZE;
		Posting[] tmp = new Posting[SIZE];
		
		//make a deep copy
		for(int i=0; i< postingList.length;i++){
			tmp[i]= postingList[i];
		}
		
		postingList= tmp;
		
	}
	
	public String toString()
	{
		String out="{";
		
		for(int i=0;i<currentPosition;i++){
			out+= postingList[i].toString();
		}
		
		return out+"}";
	}
	
}
