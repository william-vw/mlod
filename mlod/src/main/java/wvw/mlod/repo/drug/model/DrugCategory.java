package wvw.mlod.repo.drug.model;

public class DrugCategory extends MedicalEntity {

	public DrugCategory() {
		super();
	}

	public DrugCategory(String uri, String label) {
		super(uri, label);
	}

	public DrugCategory(String uri) {
		super(uri);
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.CATEGORY;
	}
	
	public String toString() {
		return getUri() + ": " + getLabel() + "@" + getSource();
	}
}
