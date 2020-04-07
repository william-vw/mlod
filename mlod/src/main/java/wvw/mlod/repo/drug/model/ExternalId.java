package wvw.mlod.repo.drug.model;

public class ExternalId extends MedicalEntity {

	private String source;

	public ExternalId() {
		super();
	}

	public ExternalId(String uri, String label) {
		super(uri, label);
	}
	
	public ExternalId(String uri, String label, String source) {
		super(uri, label);
		
		this.source = source;
	}

	public ExternalId(String uri) {
		super(uri);
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.ID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String toString() {
		return getLabel() + " @" + source;
	}
}
