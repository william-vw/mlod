package wvw.mlod.repo.drug.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class LabeledEntity {

	private String uri;
	private String label;
	private String description;

	public LabeledEntity() {
	}

	public LabeledEntity(String uri) {
		this.uri = uri;
	}

	public LabeledEntity(String uri, String label) {
		this.uri = uri;
		this.label = label;
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
	
	public boolean hasDescription() {
		return description != null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof LabeledEntity))
			return false;

		LabeledEntity e = (LabeledEntity) o;
		return uri.equals(e.getUri());
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
