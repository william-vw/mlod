package wvw.mlod.repo.drug.drugcentral;

import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;

import it.unibz.inf.ontop.injection.OntopSQLOWLAPIConfiguration;
import it.unibz.inf.ontop.owlapi.OntopOWLFactory;
import it.unibz.inf.ontop.owlapi.OntopOWLReasoner;
import it.unibz.inf.ontop.owlapi.connection.OntopOWLConnection;
import it.unibz.inf.ontop.owlapi.connection.OntopOWLStatement;
import it.unibz.inf.ontop.owlapi.resultset.OWLBindingSet;
import it.unibz.inf.ontop.owlapi.resultset.TupleOWLResultSet;
import wvw.mlod.config.MlodConfig;
import wvw.mlod.repo.RepositoryException;
import wvw.mlod.repo.drug.DrugRepository;
import wvw.query.IQueryResults;
import wvw.query.OntopQueryResults;
import wvw.query.QueryException;
import wvw.utils.Timer;
import wvw.utils.log.Log;

/**
 * Drugcentral repository. It requires first loading the Drugcentral SQL dump
 * (found at <a href=
 * "http://drugcentral.org/download">http://drugcentral.org/download</a>) into a
 * PostgreSQL database. The SQL dump contains code specific to PostgreSQL, so
 * try other RDB at your own peril.
 * 
 * The PostgreSQL database properties need to be setup in
 * src/resources/ontop/drugcentral.properties.
 * 
 * The repository relies on <a href="https://github.com/ontop/ontop">Ontop</a>
 * as a semantic layer on top of the SQL database.
 * 
 * @author William
 *
 */

public class Drugcentral extends DrugRepository {

	private OntopOWLReasoner reasoner;
	private OntopOWLConnection conn;

	public Drugcentral() throws RepositoryException {
		super("Drugcentral");

		Timer.start("loadDrugcentral");
		load();
		Timer.end("loadDrugcentral");
	}

	private void load() throws RepositoryException {
		String obdaFile = MlodConfig.drugcentralObdaFile();
		String owlFile = MlodConfig.drugcentralOwlFile();
		String propFile = MlodConfig.drugcentralPropFile();

		OntopOWLFactory factory = OntopOWLFactory.defaultFactory();
		OntopSQLOWLAPIConfiguration config = (OntopSQLOWLAPIConfiguration) OntopSQLOWLAPIConfiguration.defaultBuilder()
				.nativeOntopMappingFile(obdaFile).ontologyFile(owlFile).propertyFile(propFile).enableTestMode().build();

		try {
			reasoner = factory.createReasoner(config);

		} catch (IllegalConfigurationException | OWLOntologyCreationException e) {
			throw new RepositoryException(e);
		}

		conn = reasoner.getConnection();
	}

	@Override
	protected IQueryResults execute(String query) throws QueryException {
		try {
			return new OntopQueryResults(conn.createStatement().executeSelectQuery(query));

		} catch (OWLException e) {
			throw new QueryException(e);
		}
	}

	/**
	 * Useful for testing - print the results of a given SPARQL query, including the
	 * generated SQL query.
	 * 
	 * @param query
	 * @throws OWLException
	 * @throws Exception
	 */

	public void printResults(String query) throws OWLException {
		OntopOWLStatement stmt = conn.createStatement();
		TupleOWLResultSet rs = stmt.executeSelectQuery(query);

		while (rs.hasNext()) {
			OWLBindingSet bs = rs.next();

			bs.forEach(binding -> {
				OWLObject obj = null;
				try {
					obj = binding.getValue();
					
				} catch (OWLException e) {
					e.printStackTrace();
				}

				Log.i(ToStringRenderer.getInstance().getRendering(obj) + ", ");
			});

			Log.i("\n");
		}

		rs.close();

		String sqlQuery = stmt.getExecutableQuery(query).toString();

		Log.d("\nSPARQL query:");
		Log.d("=======================");
		Log.d(query);

		Log.d("\n\nSQL query:");
		Log.d("=====================");
		Log.d(sqlQuery);
	}
}