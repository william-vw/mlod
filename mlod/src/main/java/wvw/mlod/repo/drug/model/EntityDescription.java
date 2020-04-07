package wvw.mlod.repo.drug.model;

public class EntityDescription extends DataFromSource {

	private String uri;
	private String label;
	private String description;

	public EntityDescription() {
	}

	public EntityDescription(String label, String source) {
		super(source);
		
		this.label = label;
	}
	
	public EntityDescription(String uri, String label, String source) {
		super(source);
		
		this.uri = uri;
		this.label = label;
	}
	
	public EntityDescription(String uri, String label, String description, String source) {
		super(source);
		
		this.uri = uri;
		this.label = label;
		this.description = description;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		if (label == null)
			return description;
		else
			return label;
	}
}
