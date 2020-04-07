package wvw.query;

public interface IQueryResults {

	public boolean hasNext() throws QueryException;
	
	public void next() throws QueryException;
	
	public String getUri(String name) throws QueryException;
	
	public String getUri(String name, boolean optional) throws QueryException;
	
	public String getLiteral(String name) throws QueryException;
	
	public String getLiteral(String name, boolean optional) throws QueryException;
	
	public void close() throws QueryException;
}
