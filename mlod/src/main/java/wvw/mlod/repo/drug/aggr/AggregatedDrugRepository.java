package wvw.mlod.repo.drug.aggr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import wvw.mlod.repo.drug.DrugRepository;
import wvw.mlod.repo.drug.model.Drug;
import wvw.mlod.repo.drug.model.DrugCategory;
import wvw.mlod.repo.drug.model.DrugConditionInteraction;
import wvw.mlod.repo.drug.model.DrugDrugInteraction;
import wvw.mlod.repo.drug.model.EntityTypes;
import wvw.mlod.repo.drug.model.HealthCondition;
import wvw.query.IQueryResults;
import wvw.query.QueryException;

/**
 * Delegates each method call to the repositories it is loaded with, and returns
 * their aggregated results.
 * 
 * 
 * @author William
 *
 */

public class AggregatedDrugRepository extends DrugRepository {

	private List<DrugRepository> repos;

	public AggregatedDrugRepository(List<DrugRepository> repos) {
		this.repos = repos;

		setupLabel();
	}

	public AggregatedDrugRepository(DrugRepository... repos) {
		this.repos = new ArrayList<>(Arrays.asList(repos));

		setupLabel();
	}

	private void setupLabel() {
		setLabel(repos.stream().map(r -> r.getLabel()).collect(Collectors.joining(", ")));
	}

	@Override
	public Drug findDrug(String name, boolean exact, boolean synonyms, EntityTypes... loadTypes) throws QueryException {
		for (DrugRepository repo : repos) {

			Drug d = repo.findDrug(name, exact, synonyms, loadTypes);
			if (d != null)
				return d;
		}

		return null;
	}

	@Override
	public List<Drug> findDrugs(String name, boolean exact, boolean synonyms, EntityTypes... loadTypes)
			throws QueryException {

		List<Drug> ret = new ArrayList<>();

		for (DrugRepository repo : repos)
			ret.addAll(repo.findDrugs(name, exact, synonyms, loadTypes));

		return ret;
	}

	@Override
	public void load(Drug drug) throws QueryException {
		for (DrugRepository repo : repos)
			repo.load(drug);
	}

	@Override
	public void load(Drug drug, EntityTypes... types) throws QueryException {
		for (DrugRepository repo : repos)
			repo.load(drug, types);
	}

	@Override
	public DrugCategory findDrugCategory(String name, boolean exact) throws QueryException {
		for (DrugRepository repo : repos) {

			DrugCategory dc = repo.findDrugCategory(name, exact);
			if (dc != null)
				return dc;
		}

		return null;
	}

	@Override
	public List<DrugCategory> findDrugCategories(String name, boolean exact) throws QueryException {
		List<DrugCategory> ret = new ArrayList<>();

		for (DrugRepository repo : repos)
			ret.addAll(repo.findDrugCategories(name, exact));

		return ret;
	}

	@Override
	public List<Drug> findDrugsInCategory(DrugCategory cat, EntityTypes... loadTypes) throws QueryException {
		List<Drug> ret = new ArrayList<>();

		for (DrugRepository repo : repos)
			ret.addAll(repo.findDrugsInCategory(cat, loadTypes));

		return ret;
	}

	@Override
	public HealthCondition findHealthCondition(String name, boolean exact, EntityTypes... loadTypes)
			throws QueryException {

		for (DrugRepository repo : repos) {

			HealthCondition hc = repo.findHealthCondition(name, exact, loadTypes);
			if (hc != null)
				return hc;
		}

		return null;
	}

	// TODO: allow loading entity-types directly in this find() method as with drugs

	@Override
	public List<HealthCondition> findHealthConditions(String name, boolean exact, EntityTypes... loadTypes)
			throws QueryException {

		List<HealthCondition> ret = new ArrayList<>();

		for (DrugRepository repo : repos)
			ret.addAll(repo.findHealthConditions(name, exact, loadTypes));

		return ret;
	}

	@Override
	public List<DrugDrugInteraction> findDDIs(Drug d1, Drug d2) throws QueryException {
		List<DrugDrugInteraction> ret = new ArrayList<>();

		for (DrugRepository repo : repos)
			ret.addAll(repo.findDDIs(d1, d2));

		return ret;
	}

	@Override
	public List<DrugConditionInteraction> findDCIs(Drug drug, HealthCondition cond) throws QueryException {
		List<DrugConditionInteraction> ret = new ArrayList<>();

		for (DrugRepository repo : repos)
			ret.addAll(repo.findDCIs(drug, cond));

		return ret;
	}

	@Override
	public List<DrugConditionInteraction> findDCIs(Drug drug) throws QueryException {
		List<DrugConditionInteraction> ret = new ArrayList<>();

		for (DrugRepository repo : repos)
			ret.addAll(repo.findDCIs(drug));

		return ret;
	}

	@Override
	protected IQueryResults execute(String query) throws QueryException {
		throw new UnsupportedOperationException();
	}
}
