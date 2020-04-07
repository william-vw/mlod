package wvw.query;

import org.semanticweb.owlapi.model.OWLException;

import it.unibz.inf.ontop.owlapi.resultset.OWLBindingSet;
import it.unibz.inf.ontop.owlapi.resultset.TupleOWLResultSet;

public class OntopQueryResults implements IQueryResults {

	private TupleOWLResultSet rs;

	private OWLBindingSet curBs;

	public OntopQueryResults(TupleOWLResultSet rs) {
		this.rs = rs;
	}

	@Override
	public boolean hasNext() throws QueryException {
		try {
			return rs.hasNext();

		} catch (OWLException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public void next() throws QueryException {
		try {
			curBs = rs.next();

		} catch (OWLException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public String getUri(String name) throws QueryException {
		try {
			return curBs.getOWLIndividual(name).toStringID();

		} catch (OWLException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public String getUri(String name, boolean optional) throws QueryException {
		try {
			if (!optional || curBs.getOWLIndividual(name) != null)
				return curBs.getOWLIndividual(name).toStringID();

		} catch (OWLException e) {
			throw new QueryException(e);
		}

		return null;
	}

	@Override
	public String getLiteral(String name) throws QueryException {
		try {
			return curBs.getOWLLiteral(name).getLiteral();

		} catch (OWLException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public String getLiteral(String name, boolean optional) throws QueryException {
		try {
			if (!optional || curBs.getOWLLiteral(name) != null)
				return curBs.getOWLLiteral(name).getLiteral();

		} catch (OWLException e) {
			throw new QueryException(e);
		}

		return null;
	}

	@Override
	public void close() throws QueryException {
		try {
			rs.close();

		} catch (OWLException e) {
			throw new QueryException(e);
		}
	}

}
