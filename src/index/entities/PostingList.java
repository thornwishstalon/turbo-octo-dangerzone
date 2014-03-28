package index.entities;

import java.util.Dictionary;

/***
 * 
 * @author F
 *
 */
public class PostingList {
	private int SIZE= 1024;
	private int currentPosition=0;
	private int overallFrequency=0;
	
	private Posting[] postingList;
	
	
	public PostingList(){
		postingList= new Posting[SIZE];
	}
	
	public void addToList(Posting p)
	{
		//System.out.println("added "+p.toString()+"to dictionary");
		
		if(currentPosition >= SIZE)
			doublePostingList();
		
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
		String out="{"+overallFrequency+" ";
		
		for(int i=0;i<currentPosition;i++){
			out+= postingList[i].toString();
		}
		
		return out+"}";
	}
	
}
