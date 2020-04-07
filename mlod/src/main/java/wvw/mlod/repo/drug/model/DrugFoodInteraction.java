package wvw.mlod.repo.drug.model;

public class DrugFoodInteraction extends DrugInteraction {

	private Drug drug;
	private Food food;

	public DrugFoodInteraction() {
		super();
	}

	public DrugFoodInteraction(String uri) {
		super(uri);
	}

	public DrugFoodInteraction(String uri, String label) {
		super(uri, label);
	}

	public DrugFoodInteraction(String uri, String label, Drug drug, Food food) {
		super(uri, label);

		this.drug = drug;
		this.food = food;
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.DFI;
	}

	public Drug getDrug() {
		return drug;
	}

	public void setDrug(Drug drug) {
		this.drug = drug;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}
	
	public String toString() {
		return food.getLabel();
	}
}
