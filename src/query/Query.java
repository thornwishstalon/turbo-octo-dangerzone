package query;

import index.entities.PostingList;

public class Query {
	//private String query;
	private double tf_idf = 0.0;;
	private int tf=1;
	private PostingList postings;
	
	
	public Query() {
		// TODO Auto-generated constructor stub
		
	}

	
	public PostingList getPostings() {
		return postings;
	}

	public void setPostings(PostingList postings) {
		this.postings = postings;
	}





	public void setTf_idf(double tf_idf){
		//System.out.println(tf_idf);
		this.tf_idf= tf_idf;
	}
	
	public double getTf_idf(){
		return tf_idf;
	}
	
	public void upgradeTF()
	{
		tf++;
	}
	
	public int getTF(){
		return tf;
	}

	

}
