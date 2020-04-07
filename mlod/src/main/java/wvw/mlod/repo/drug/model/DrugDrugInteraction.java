package wvw.mlod.repo.drug.model;

public class DrugDrugInteraction extends DrugInteraction {

	private Drug drug1;
	private Drug drug2;

	public DrugDrugInteraction() {
		super();
	}

	public DrugDrugInteraction(String uri) {
		super(uri);
	}

	public DrugDrugInteraction(String uri, String label) {
		super(uri, label);
	}

	public DrugDrugInteraction(String uri, String label, Drug drug1, Drug drug2) {
		super(uri, label);

		this.drug1 = drug1;
		this.drug2 = drug2;
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.DDI;
	}

	public Drug getDrug1() {
		return drug1;
	}

	public void setDrug1(Drug drug1) {
		this.drug1 = drug1;
	}

	public Drug getDrug2() {
		return drug2;
	}

	public void setDrug2(Drug drug2) {
		this.drug2 = drug2;
	}

	public String toString() {
		return getUri() + "\n" + getDrug1().getUri() + ": " + getDrug1().getLabel() + "\n" + getDrug2().getUri() + ": "
				+ getDrug2().getLabel() + (hasRisk() ? "\n(risk = " + getRisk() + ")" : "")
				+ (hasDescription() ? ": " + getDescription() : "") + " (@" + getSource() + ")\n";
	}
}
