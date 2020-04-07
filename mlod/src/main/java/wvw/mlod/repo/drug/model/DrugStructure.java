package wvw.mlod.repo.drug.model;

public class DrugStructure extends MedicalEntity {

	public DrugStructure() {
	}

	public DrugStructure(String uri) {
		super(uri);
	}

	public DrugStructure(String uri, String label) {
		super(uri, label);
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.STRUCT;
	}
}
