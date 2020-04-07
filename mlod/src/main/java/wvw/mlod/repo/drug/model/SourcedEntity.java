package wvw.mlod.repo.drug.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class SourcedEntity {

	private List<EntityDescription> descriptions = new ArrayList<>();
	private List<ExternalId> ids = new ArrayList<>();

	public SourcedEntity() {
	}

	public SourcedEntity(EntityDescription description) {
		descriptions.add(description);
	}

	public String getUriFrom(String source) {
		Optional<EntityDescription> ed = descriptions.stream().filter(d -> d.getSource().equals(source)).findAny();
		if (ed.isPresent())
			return ed.get().getUri();

		return null;
	}
	
	public EntityDescription getAnyDescription() {
		return descriptions.get(0);
	}

	public void addDescription(EntityDescription description) {
		descriptions.add(description);
	}

	public List<EntityDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<EntityDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public List<ExternalId> getIds() {
		return ids;
	}

	public void addId(ExternalId id) {
		ids.add(id);
	}

	public void setIds(List<ExternalId> ids) {
		this.ids = ids;
	}

	public abstract EntityTypes getType();

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
}
