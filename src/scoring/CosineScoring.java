package scoring;

import query.Query;

public class CosineScoring implements Runnable{

	int k=10;
	
	public float scoreForQuery(Query q, int n)
	{
		float[] Scores = new float[n];
		float[] Length= new float[n];
		for(String queryterm: q.getQueryTokens())
		{
			//calculate w_(t,q) and fetch posting lists for queryterm
			//for each posting
			//score[d] += 
			
		}
		
		
		return 0;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
