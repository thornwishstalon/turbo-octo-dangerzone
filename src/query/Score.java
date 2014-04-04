package query;

public class Score implements Comparable<Score>{
	private String id;
	private float score;
	
	public Score(){
		score=0f;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}

	@Override
	public int compareTo(Score o) {
		if(o.getScore() < score)
			return 1;
		else if(o.getScore() > score)
			return -1;
		else return 0;
	}
	
	
	

}
