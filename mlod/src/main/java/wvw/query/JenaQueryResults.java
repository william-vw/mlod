package wvw.query;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class JenaQueryResults implements IQueryResults {

	private ResultSet rs;

	private QuerySolution curSol;

	public JenaQueryResults(ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public boolean hasNext() {
		return rs.hasNext();
	}

	@Override
	public void next() {
		curSol = rs.next();
	}

	@Override
	public String getUri(String name) {
		return curSol.get(name).asResource().getURI();
	}
	
	@Override
	public String getUri(String name, boolean optional) {
		if (!optional || curSol.contains(name))
			return curSol.get(name).asResource().getURI();
		
		return null;
	}

	@Override
	public String getLiteral(String name) {
		return curSol.get(name).asLiteral().getString();
	}
	
	@Override
	public String getLiteral(String name, boolean optional) {
		if (!optional || curSol.contains(name))
			return curSol.get(name).asLiteral().getString();
		
		return null;
	}

	@Override
	public void close() {
	}
}