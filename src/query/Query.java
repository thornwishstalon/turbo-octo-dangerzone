package query;

public class Query {
	private String query;
	
	
	public Query(String query )
	{
		this.query = query;
	}
	
	public String[] getQueryTokens()
	{
		return query.split(" ");
	}
	
	
}
