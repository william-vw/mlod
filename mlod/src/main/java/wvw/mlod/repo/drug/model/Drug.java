package wvw.mlod.repo.drug.model;

import java.util.ArrayList;
import java.util.List;

import wvw.utils.log.Log;

public class Drug extends MedicalEntity {

	private DrugStructure struct;
	private List<DrugCategory> categories = new ArrayList<>();

	private List<DrugDrugInteraction> ddis = new ArrayList<>();
	private List<DrugFoodInteraction> dfis = new ArrayList<>();
	private List<DrugConditionInteraction> dcis = new ArrayList<>();
	private List<DrugSynonym> synonyms = new ArrayList<>();

	public Drug() {
	}

	public Drug(String uri) {
		super(uri);
	}

	public Drug(String uri, String label) {
		super(uri, label);
	}

	public EntityTypes getType() {
		return EntityTypes.DRUG;
	}

	public boolean hasStructure() {
		return struct != null;
	}

	public DrugStructure getStructure() {
		return struct;
	}

	public void setStructure(DrugStructure struct) {
		this.struct = struct;
	}

	public List<DrugCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<DrugCategory> categories) {
		this.categories = categories;
	}

	public List<DrugDrugInteraction> getDdis() {
		return ddis;
	}

	public void setDdis(List<DrugDrugInteraction> ddis) {
		this.ddis = ddis;
	}

	public List<DrugFoodInteraction> getDfis() {
		return dfis;
	}

	public void setDfis(List<DrugFoodInteraction> dfis) {
		this.dfis = dfis;
	}

	public List<DrugConditionInteraction> getDcis() {
		return dcis;
	}

	public void setDcis(List<DrugConditionInteraction> dcis) {
		this.dcis = dcis;
	}

	public List<DrugSynonym> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<DrugSynonym> synonyms) {
		this.synonyms = synonyms;
	}

	public void print() {
		Log.i(getUri() + " \"" + getLabel() + "\" (@" + getSource() + ")");
		// Log.i("struct: " + struct);
		getSynonyms().stream().forEach(syn -> Log.i("synonym: " + syn.getSynonym()));
		getCategories().stream().forEach(category -> Log.i("category: " + category.getLabel()));
		getDdis().stream().forEach(ddi -> Log.i("ddi: " + ddi));
		getDfis().stream().forEach(dfi -> Log.i("dfi: " + dfi));
		getDcis().stream().forEach(dci -> Log.i("dci: " + dci));
		Log.i("");
	}

	public String toString() {
		return getUri() + ": " + getLabel();
		// return getLabel();
	}
}
