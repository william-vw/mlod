package wvw.mlod.repo.drug.model;

public class DrugConditionInteraction extends DrugInteraction {

	private Drug drug;
	private HealthCondition condition;

	public DrugConditionInteraction() {
		super();
	}

	public DrugConditionInteraction(String uri) {
		super(uri);
	}

	public DrugConditionInteraction(String uri, String label) {
		super(uri, label);
	}

	public DrugConditionInteraction(String uri, String label, Drug drug, HealthCondition condition) {
		super(uri, label);

		this.drug = drug;
		this.condition = condition;
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.DCI;
	}

	public Drug getDrug() {
		return drug;
	}

	public void setDrug(Drug drug) {
		this.drug = drug;
	}

	public HealthCondition getCondition() {
		return condition;
	}

	public void setCondition(HealthCondition condition) {
		this.condition = condition;
	}

	public String toString() {
		return getUri() + "\n" + drug.getUri() + ": " + drug.getLabel() + " - " + condition.getUri() + ":"
				+ condition.getLabel() + " (@" + getSource() + ")";
	}
}
