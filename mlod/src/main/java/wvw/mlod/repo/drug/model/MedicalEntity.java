package wvw.mlod.repo.drug.model;

import java.util.ArrayList;
import java.util.List;

public abstract class MedicalEntity extends LabeledEntity {

	private String source;
	private List<ExternalId> ids = new ArrayList<>();

	public MedicalEntity() {
		super();
	}

	public MedicalEntity(String uri) {
		super(uri);
	}

	public MedicalEntity(String uri, String label) {
		super(uri, label);
	}

	public abstract EntityTypes getType();

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<ExternalId> getIds() {
		return ids;
	}

	public void setIds(List<ExternalId> ids) {
		this.ids = ids;
	}
}
