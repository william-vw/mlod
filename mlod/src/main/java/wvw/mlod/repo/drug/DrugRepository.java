package wvw.mlod.repo.drug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wvw.mlod.repo.Repository;
import wvw.mlod.repo.drug.model.Drug;
import wvw.mlod.repo.drug.model.DrugCategory;
import wvw.mlod.repo.drug.model.DrugConditionInteraction;
import wvw.mlod.repo.drug.model.DrugDrugInteraction;
import wvw.mlod.repo.drug.model.DrugFoodInteraction;
import wvw.mlod.repo.drug.model.DrugStructure;
import wvw.mlod.repo.drug.model.DrugSynonym;
import wvw.mlod.repo.drug.model.EntityTypes;
import wvw.mlod.repo.drug.model.ExternalId;
import wvw.mlod.repo.drug.model.Food;
import wvw.mlod.repo.drug.model.HealthCondition;
import wvw.mlod.repo.drug.model.MedicalEntity;
import wvw.query.IQueryResults;
import wvw.query.QueryException;
import wvw.utils.log.Log;

/**
 * A drug repository in the "MLOD cloud". It has methods for finding and loading
 * drugs, drug categories and health conditions, as well as finding DDI and DCI.
 * 
 * It is centered on a drug-ontology that can be found in
 * src/main/resources/schema/drug.owl.
 * 
 * A subclass only needs to implement the {@link #execute(String) } method.
 * 
 * 
 * @author William
 *
 */

public abstract class DrugRepository extends Repository {

	public DrugRepository() {
	}

	public DrugRepository(String label) {
		super(label);
	}

	/**
	 * Find a single drug in this repository.
	 * 
	 * @param name      drug name
	 * @param exact     if true, name will be searched as a string
	 *                  (case-insensitive); else, name will be treated as a regular
	 *                  expression
	 * @param synonyms  whether synonyms should be searched as well
	 * @param loadTypes information that will be loaded for the found drug
	 * @return the found drug, or null if not found
	 * @throws QueryException
	 */

	public Drug findDrug(String name, boolean exact, boolean synonyms, EntityTypes... loadTypes) throws QueryException {
		String query1 = genDrugQuery(name, exact);
		String query2 = genDrugSynQuery(name);

		Drug ret = null;

		IQueryResults[] rss = { execute(query1), execute(query2) };
		for (IQueryResults rs : rss) {
			if (rs.hasNext()) {
				rs.next();

				ret = genDrug(rs, loadTypes);
				break;
			}
		}

		for (IQueryResults rs : rss)
			rs.close();

		return ret;
	}

	/**
	 * Find one or more drugs in this repository.
	 * 
	 * @param name      drug name
	 * @param exact     if true, name will be searched as a string
	 *                  (case-insensitive); else, name will be treated as a regular
	 *                  expression
	 * @param synonyms  whether synonyms should be searched as well
	 * @param loadTypes information that will be loaded for any found drug
	 * @return the found drugs, if any
	 * @throws QueryException
	 */

	public List<Drug> findDrugs(String name, boolean exact, boolean synonyms, EntityTypes... loadTypes)
			throws QueryException {

		List<Drug> ret = new ArrayList<>();

		String query1 = genDrugQuery(name, exact);
		String query2 = genDrugSynQuery(name);

		IQueryResults[] rss = { execute(query1), execute(query2) };
		for (IQueryResults rs : rss) {
			while (rs.hasNext()) {
				rs.next();

				Drug drug = genDrug(rs, loadTypes);
				if (!ret.contains(drug))
					ret.add(drug);
			}

			rs.close();
		}

		return ret;
	}

	private String genDrugQuery(String name, boolean exact) {
		name = name.toLowerCase();

		// @formatter:off
		return 
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ "PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "SELECT DISTINCT ?drug ?name WHERE {\n" 
			+ "	?drug a dv:Drug ;\n" 
			+ "		dc:title ?name .\n" 
			+ (exact ? 
					"	FILTER (lcase(str(?name)) = \"" + name + "\")\n"
					: "	FILTER (regex(str(?name), \"" + name + "\", \"i\"))\n")
			+ "}";
		// @formatter:on
	}

	private String genDrugSynQuery(String name) {
		name = name.toLowerCase();

		// @formatter:off
		return 
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ "PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "SELECT DISTINCT ?drug ?name WHERE {\n" 
			+ "	?drug a dv:Drug ;\n" 
			+ "		dc:title ?name ;\n" 
			+ "		dv:drug-synonym ?ds .\n"
			+ "	?ds dc:title ?syn .\n" 
			+ "	FILTER (lcase(?syn) = \"" + name + "\")\n"
			+ "}";
		// @formatter:on
	}

	private Drug genDrug(IQueryResults rs, EntityTypes... loadTypes) throws QueryException {
		String uri = rs.getUri("drug");
		String fullName = rs.getLiteral("name");

		Drug drug = new Drug(uri, fullName);
		load(drug, loadTypes);

		return drug;
	}

	/**
	 * Load all information for the given drug.
	 * 
	 * @param drug
	 * @throws QueryException
	 */

	public void load(Drug drug) throws QueryException {
		load(drug, true);
	}

	/**
	 * Load the listed types of information for this drug.
	 * 
	 * @param drug
	 * @param types
	 * @throws QueryException
	 */

	public void load(Drug drug, EntityTypes... types) throws QueryException {
		loadDrugStructure(drug);
		loadSource(drug);
		loadExternalIds(drug);

		for (EntityTypes type : types) {
			switch (type) {

			// (already loaded above)
			case STRUCT:
				break;

			case SYNONYM:
				loadSynonyms(drug);
				break;

			case CATEGORY:
				loadCategories(drug);
				break;

			case DDI:
				loadDDIs(drug);
				break;

			case DCI:
				loadDCIs(drug);
				break;

			case DFI:
				loadDFIs(drug);
				break;

			case ALL:
				loadSynonyms(drug);
				loadCategories(drug);
				loadDDIs(drug);
				loadDCIs(drug);
				loadDFIs(drug);
				break;

			default:
				Log.e("unsupported load type for drugs: " + type);
				break;
			}
		}
	}

	private void load(Drug drug, boolean loadAll) throws QueryException {
		loadDrugStructure(drug);

		if (loadAll) {
			loadSource(drug);
			loadExternalIds(drug);
			loadCategories(drug);
			loadDDIs(drug);
			loadDCIs(drug);
			loadDFIs(drug);
		}
	}

	/**
	 * Find a drug category in this repository.
	 * 
	 * @param name  name of the category
	 * @param exact if true, the name will be searched as a string
	 *              (case-insensitive); else, name will be treated as a regular
	 *              expression
	 * @return
	 * @throws QueryException
	 */

	public DrugCategory findDrugCategory(String name, boolean exact) throws QueryException {
		String query = genDrugCategoryQuery(name, exact);

		IQueryResults rs = execute(query);
		if (rs.hasNext()) {
			rs.next();

			DrugCategory cat = genDrugCategory(rs);
			rs.close();

			return cat;
		}

		rs.close();

		return null;
	}

	/**
	 * Find one or more drug categories in this repository.
	 * 
	 * @param name  name of the category
	 * @param exact if true, the name will be searched as a string
	 *              (case-insensitive); else, name will be treated as a regular
	 *              expression
	 * @return
	 * @throws QueryException
	 */

	public List<DrugCategory> findDrugCategories(String name, boolean exact) throws QueryException {
		List<DrugCategory> ret = new ArrayList<>();

		String query = genDrugCategoryQuery(name, exact);

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			DrugCategory cat = genDrugCategory(rs);
			cat.setSource(getLabel());
			ret.add(cat);
		}

		rs.close();

		return ret;
	}

	private String genDrugCategoryQuery(String name, boolean exact) {
		name = name.toLowerCase();

		// @formatter:off
		return 
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ "PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "SELECT DISTINCT ?cat ?name WHERE {\n" 
			+ "	?cat a dv:Drug-Category ;\n" 
			+ "		dc:title ?name .\n"
			+ (exact ? 
					"	FILTER (lcase(str(?name)) = \"" + name + "\")\n"
					: "	FILTER (regex(str(?name), \"" + name + "\", \"i\"))\n")
			+ "}";
		// @formatter:on
	}

	private DrugCategory genDrugCategory(IQueryResults rs) throws QueryException {
		String uri = rs.getUri("cat");
		String fullName = rs.getLiteral("name");

		return new DrugCategory(uri, fullName);
	}

	/**
	 * Find all drugs in the given category.
	 * 
	 * @param cat       category that will be searched
	 * @param loadTypes information that will be loaded for any found drug
	 * @return
	 * @throws QueryException
	 */

	public List<Drug> findDrugsInCategory(DrugCategory cat, EntityTypes... loadTypes) throws QueryException {
		List<Drug> drugs = new ArrayList<>();

		// @formatter:off
		String query =
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ "PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "SELECT ?drug ?name WHERE {\n" 
			+ "	?drug a dv:Drug ;\n"
			+ "		dc:title ?name ;\n" 
			+ "		dv:drug-category <" + cat.getUri() + "> .\n" 
			+ "}";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			Drug drug = genDrug(rs, loadTypes);
			drug.setCategories(Arrays.asList(cat));

			drugs.add(drug);
		}

		rs.close();

		return drugs;
	}

	/**
	 * Find a single health condition in this repository.
	 * 
	 * @param name      name of the condition
	 * @param exact     if true, the name will be searched as a string
	 *                  (case-insensitive); else, name will be treated as a regular
	 *                  expression
	 * @param loadTypes information that will be loaded for the found health
	 *                  condition
	 * @return
	 * @throws QueryException
	 */

	public HealthCondition findHealthCondition(String name, boolean exact, EntityTypes... loadTypes)
			throws QueryException {

		String query = genHealthCondQuery(name, exact);

		IQueryResults rs = execute(query);
		if (rs.hasNext()) {
			rs.next();

			HealthCondition cond = genHealthCondition(rs, loadTypes);
			rs.close();

			return cond;
		}

		rs.close();

		return null;
	}

	/**
	 * Find one or more health conditions in this repository.
	 * 
	 * @param name      name of the condition
	 * @param exact     if true, the name will be searched as a string
	 *                  (case-insensitive); else, name will be treated as a regular
	 *                  expression
	 * @param loadTypes information that will be loaded for any found health
	 *                  condition
	 * @return
	 * @throws QueryException
	 */

	public List<HealthCondition> findHealthConditions(String name, boolean exact, EntityTypes... loadTypes)
			throws QueryException {

		List<HealthCondition> ret = new ArrayList<>();

		String query = genHealthCondQuery(name, exact);

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			HealthCondition cond = genHealthCondition(rs, loadTypes);
			ret.add(cond);
		}

		rs.close();

		return ret;
	}

	private String genHealthCondQuery(String name, boolean exact) {
		name = name.toLowerCase();

		// @formatter:off
		return 
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ "PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "SELECT DISTINCT ?cond ?name WHERE {\n" 
			+ "	?cond a dv:Health-Condition ;\n" 
			+ "		dc:title ?name .\n" 
			+ (exact ? 
					"	FILTER (lcase(?name) = \"" + name + "\")\n"
					: "	FILTER (regex(?name, \"" + name + "\", \"i\"))\n")
			+ "}";
		// @formatter:on
	}

	private HealthCondition genHealthCondition(IQueryResults rs, EntityTypes... loadTypes) throws QueryException {
		String uri = rs.getUri("cond");
		String fullName = rs.getLiteral("name");

		HealthCondition cond = new HealthCondition(uri, fullName);
		loadSource(cond);

		for (EntityTypes loadType : loadTypes) {
			switch (loadType) {

			case DCI:
				loadDCIs(cond);
				break;

			default:
				Log.e("unsupported load type for health conditions: " + loadType);
				break;
			}
		}

		return cond;
	}

	private void loadDCIs(HealthCondition cond) throws QueryException {
		// @formatter:off
		String query = 
			"PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?drug ?drugName ?dci \n"
			+ " WHERE { \n"
			+ "	?drug dv:drug-structure ?struct ;\n"
			+ "		dc:title ?drugName .\n"
			+ "	?struct dv:condition-interaction ?dci .\n"
			+ "		?dci dv:condition-interactor <" + cond.getUri() + "> .\n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			Drug drug = new Drug(rs.getUri("drug"), rs.getLiteral("drugName"));
			String dciUri = rs.getUri("dci");

			DrugConditionInteraction dci = new DrugConditionInteraction(dciUri, null, drug, cond);
			loadSource(dci);

			cond.getDcis().add(dci);
		}

		rs.close();
	}

	/**
	 * Find drug-drug-interactions (DDIs) between two drugs.
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 * @throws QueryException
	 */

	public List<DrugDrugInteraction> findDDIs(Drug d1, Drug d2) throws QueryException {
		List<DrugDrugInteraction> ret = new ArrayList<>();

		// @formatter:off
		String query = 
			"PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?ddi ?descr ?risk \n"
			+ " WHERE { \n"
			+ "		<" + d1.getUri() + "> dv:drug-interaction ?ddi .\n"
			+ "		?ddi a dv:Drug-Drug-Interaction ;\n"
			+ "			dv:drug-interactor <" + d2.getUri() + "> ;\n"
			+ "			dc:description ?descr .\n"
			+ "		OPTIONAL {\n"
			+ "			?ddi dv:interaction-risk ?risk .\n"
			+ "		}\n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			String ddiUri = rs.getUri("ddi");
			String ddiDescr = rs.getLiteral("descr");
			String ddiRisk = rs.getLiteral("risk", true);

			DrugDrugInteraction ddi = new DrugDrugInteraction(ddiUri, null, d1, d2);
			ddi.setDescription(ddiDescr);
			ddi.setRisk(ddiRisk);

			loadSource(ddi);

			ret.add(ddi);
		}

		rs.close();

		return ret;
	}

	/**
	 * Find drug-condition-interactions (DCIs) between a drug and health condition.
	 * 
	 * @param drug
	 * @param cond
	 * @return
	 * @throws QueryException
	 */
	public List<DrugConditionInteraction> findDCIs(Drug drug, HealthCondition cond) throws QueryException {
		List<DrugConditionInteraction> ret = new ArrayList<>();

		if (drug.getStructure() == null) {
			Log.e("drug does not have structure so cannot find DCI");
			return ret;
		}

		// @formatter:off
		String query = 
			"PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?dci \n"
			+ " WHERE { \n"
			+ "		<" + drug.getStructure().getUri() + "> dv:condition-interaction ?dci .\n"
			+ "		?dci dv:condition-interactor <" + cond.getUri() + "> .\n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			String dciUri = rs.getUri("dci");

			DrugConditionInteraction dci = new DrugConditionInteraction(dciUri, null, drug, cond);
			loadSource(dci);

			ret.add(dci);
		}

		rs.close();

		return ret;
	}

	/**
	 * Find drug-condition-interactions associated with a drug.
	 * 
	 * @param drug
	 * @return
	 * @throws QueryException
	 */

	public List<DrugConditionInteraction> findDCIs(Drug drug) throws QueryException {
		List<DrugConditionInteraction> ret = new ArrayList<>();

		// @formatter:off
		String query = 
			"PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?cond ?dci \n"
			+ " WHERE { \n"
			+ "		<" + drug.getUri() + "> dv:drug-structure ?struct .\n"
			+ "		?struct dv:condition-interaction ?dci .\n"
			+ "		?dci dv:condition-interactor ?cond .\n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			HealthCondition cond = new HealthCondition(rs.getUri("cond"));

			DrugConditionInteraction dci = new DrugConditionInteraction(rs.getUri("dci"), null, drug, cond);
			loadSource(dci);

			ret.add(dci);
		}

		rs.close();

		return ret;
	}

	private void loadExternalIds(MedicalEntity entity) throws QueryException {
		// @formatter:off
		String query = 
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n" 
			+ "PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ " SELECT DISTINCT ?res ?id ?source \n"
			+ " WHERE { \n" 
			+ "		<" + entity.getUri() + "> dv:external-id ?res . \n"
			+ "		?res dc:identifier ?id ;\n"
			+ "			dv:external-source ?source .\n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			String res = rs.getUri("res");
			String id = rs.getLiteral("id");
			String source = rs.getLiteral("source");

			ExternalId extId = new ExternalId(res, id, source);
			loadSource(extId);

			entity.getIds().add(extId);
		}

		rs.close();
	}

	private void loadDrugStructure(Drug drug) throws QueryException {
		// @formatter:off
		String query = 
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?struct \n"
			+ " WHERE { \n"
			+ "		<" + drug.getUri() + "> dv:drug-structure ?struct . \n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		if (rs.hasNext()) {
			rs.next();

			String structUri = rs.getUri("struct");

			DrugStructure struct = new DrugStructure(structUri);
			drug.setStructure(struct);

			loadSource(struct);
		}

		rs.close();
	}

	private void loadSynonyms(Drug drug) throws QueryException {
		// @formatter:off
		String query = 
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ "PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "SELECT DISTINCT ?ds ?syn WHERE {\n" 
			+ "	<" + drug.getUri() + "> dv:drug-synonym ?ds .\n"
			+ "	?ds dc:title ?syn .\n"
			+ "}";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			String synUri = rs.getUri("ds");
			String synonym = rs.getLiteral("syn");

			DrugSynonym syn = new DrugSynonym(synUri, synonym);
			loadSource(syn);

			drug.getSynonyms().add(syn);
		}
	}

	private void loadCategories(Drug drug) throws QueryException {
		// @formatter:off
		String query = 
			"PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ "PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "SELECT ?category ?name WHERE {\n" 
			+ "	<" + drug.getUri() + "> dv:drug-category ?category .\n"
			+ "	?category dc:title ?name .\n"
			+ "}";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			String catUri = rs.getUri("category");
			String catName = rs.getLiteral("name");

			DrugCategory category = new DrugCategory(catUri, catName);
			loadSource(category);
			loadExternalIds(category);

			drug.getCategories().add(category);
		}

		rs.close();
	}

	private void loadDDIs(Drug drug) throws QueryException {
		// @formatter:off
		String query = 
			"PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?ddi ?descr ?risk ?drug2 ?name2 \n"
			+ " WHERE { \n"
			+ "		<" + drug.getUri() + "> dv:drug-interaction ?ddi .\n"
			+ "		?ddi a dv:Drug-Drug-Interaction ;\n"
			+ "			dv:drug-interactor ?drug2 ;\n"
			+ "			dc:description ?descr .\n"
			+ "		?drug2 dc:title ?name2 . \n"
			+ "		FILTER (?drug2 != <" + drug.getUri() + ">)\n"
			+ "		OPTIONAL {\n"
			+ "			?ddi dv:interaction-risk ?risk .\n"
			+ "		}\n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			String ddiUri = rs.getUri("ddi");
			String ddiDescr = rs.getLiteral("descr");
			String ddiRisk = rs.getLiteral("risk", true);
			String drug2Uri = rs.getUri("drug2");
			String drug2Name = rs.getLiteral("name2");

			Drug drug2 = new Drug(drug2Uri, drug2Name);
			loadSource(drug2);

			DrugDrugInteraction ddi = new DrugDrugInteraction(ddiUri, null, drug, drug2);
			ddi.setDescription(ddiDescr);
			ddi.setRisk(ddiRisk);

			loadSource(ddi);

			drug.getDdis().add(ddi);
		}

		rs.close();
	}

	private void loadDCIs(Drug drug) throws QueryException {
		if (drug.getStructure() == null)
			return;

		// @formatter:off
		String query = 
			"PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?dci ?cond ?name ?risk \n"
			+ " WHERE { \n"
			+ "		<" + drug.getStructure().getUri() + "> dv:condition-interaction ?dci .\n"
			+ "		?dci dv:condition-interactor ?cond .\n"
			+ "		?cond dc:title ?name .\n"
			+ "		OPTIONAL {\n"
			+ "			?dci dv:interaction-risk ?risk .\n"
			+ "		}\n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			String dciUri = rs.getUri("dci");
			String condUri = rs.getUri("cond");
			String condName = rs.getLiteral("name");
			String dciRisk = rs.getLiteral("risk", true);

			// TODO ODBA has direct access to condition details ..
			// (see drugcentral.obda)
			HealthCondition cond = new HealthCondition(condUri, condName); // getHealthCondition(condUri);

			DrugConditionInteraction dci = new DrugConditionInteraction(dciUri, null, drug, cond);
			dci.setRisk(dciRisk);
			loadSource(dci);

			drug.getDcis().add(dci);
		}

		rs.close();
	}

	private void loadDFIs(Drug drug) throws QueryException {
		// @formatter:off
		String query = 
			"PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?dfi ?food2 ?name2 \n"
			+ " WHERE { \n"
			+ "		<" + drug.getUri() + "> dv:food-interaction ?dfi .\n"
			+ "		?dfi dv:food-interactor ?food2 .\n"
			+ "		?food2 dc:title ?name2 ."
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		while (rs.hasNext()) {
			rs.next();

			String dfiUri = rs.getUri("dfi");
			String food2Uri = rs.getUri("food2");
			String food2Name = rs.getLiteral("name2");

			Food food = new Food(food2Uri, food2Name);
			loadSource(food);

			DrugFoodInteraction dfi = new DrugFoodInteraction(dfiUri);
			dfi.setFood(food);

			loadSource(dfi);

			drug.getDfis().add(dfi);
		}

		rs.close();
	}

	private void loadSource(MedicalEntity entity) throws QueryException {
		// @formatter:off
		String query =
			"PREFIX dc: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dv: <http://bio2rdf.org/drug_vocabulary:> \n"
			+ " SELECT DISTINCT ?source \n"
			+ " WHERE { \n"
			+ "		<" + entity.getUri() + "> dc:source ?source .\n"
			+ " }";
		// @formatter:on

		IQueryResults rs = execute(query);
		if (rs.hasNext()) {
			rs.next();
			String source = rs.getLiteral("source");

			entity.setSource(source);
		}

		rs.close();
	}

	protected abstract IQueryResults execute(String query) throws QueryException;
}
