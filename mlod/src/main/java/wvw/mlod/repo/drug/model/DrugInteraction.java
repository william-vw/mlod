package wvw.mlod.repo.drug.model;

public abstract class DrugInteraction extends MedicalEntity {

	// TODO enum instead of free text
	private String risk;

	public DrugInteraction() {
		super();
	}

	public DrugInteraction(String uri, String label) {
		super(uri, label);
	}

	public DrugInteraction(String uri) {
		super(uri);
	}

	public boolean hasRisk() {
		return risk != null;
	}
	
	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}
}
