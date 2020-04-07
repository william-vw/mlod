package wvw.mlod.repo.drug.model;

public class DrugSynonym extends MedicalEntity {

	private String synonym;

	public DrugSynonym() {
	}

	public DrugSynonym(String uri, String synonym) {
		super(uri);
		
		this.synonym = synonym;
	}

	public String getSynonym() {
		return synonym;
	}

	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.SYNONYM;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DrugSynonym))
			return false;

		DrugSynonym syn = (DrugSynonym) o;

		return synonym.equals(syn.getSynonym());
	}
}
