package wvw.mlod.repo.drug.model;

public class Food extends MedicalEntity {

	public Food() {
		super();
	}

	public Food(String uri, String label) {
		super(uri, label);
	}

	public Food(String uri) {
		super(uri);
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.FOOD;
	}
}
