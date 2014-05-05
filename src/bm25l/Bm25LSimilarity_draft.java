package bm25l;


import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class Bm25LSimilarity_draft extends SimilarityBase {
	private float k3 = 1000; //like in paper (introduction of section 3)
	private float k1 = 1.2f;
	private float b = 0.75f;
	private float sigma = 0.5f;
	
	
	public Bm25LSimilarity_draft(float sigma){
		this.sigma= sigma;
	}
	
	
	private float scoreDoc(){
		
		float score=0f;
		
		//for each terms both in query and document
		String term="";
		String doc="";
		float cDash= cDash(term, doc);
		score+= queryTerm(term) * fDash(cDash) * idf(term);
		//end
		
		return score;
	}
	
	private float queryTerm(String term){
		float cQ= cQ(term);
		
		return ((k3 + 1) * cQ) / (k3 + cQ);
	}
	
	private float idf(String term){
		float documentFrequency=0f;//TODO documentFrequency of term! set in computeNorm during indexCreation!!
		float N = 1f; //total number of documents TODO computeNorm!!
		double idf= (N + 1)/(documentFrequency + 0.5);
		
		return (float) Math.log(idf);
	}
	
	
	
	private float fDash(float cDash){
		if(cDash > 0){
			return ((k1 + 1) * (cDash + sigma) ) / (k1 + (cDash + sigma));
			
		}else return 0f;
		
		
		
	}
	

	
	private float cQ(String term){
		//c is raw term frequency in query Q
		
		
		return 0f;
	}
	

	
	private float cD(String term, String doc){
		//c is raw term frequency in document D
		
		return 0f;
	}
	
	private float cDash(String  term, String doc){
		float averageDocLength = 1f; //TODO
		float documentLength = 1f; //TODO
		float cDash= (cD(term,doc)) / ((1 - b) + b * (documentLength / averageDocLength));
		
		return cDash;
		
	}


	@Override
	protected float score(BasicStats stats, float freq, float docLen) {
		// TODO Auto-generated method stub
		
		return 0;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
