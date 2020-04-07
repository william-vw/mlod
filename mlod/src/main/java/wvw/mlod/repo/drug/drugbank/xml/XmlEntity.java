package wvw.mlod.repo.drug.drugbank.xml;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import thewebsemantic.RdfProperty;
import thewebsemantic.binding.RdfBean;

public class XmlEntity extends RdfBean<XmlEntity> {

	protected URI type;

	private Collection<XmlExternalId> externalIds = new ArrayList<>();

	public XmlEntity() {
	}

	public XmlEntity(URI type) {
		this.type = type;
	}

	@RdfProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
	public URI getType() {
		return type;
	}

	public void setType(URI type) {
		this.type = type;
	}

	@XmlElementWrapper(name = "external-identifiers")
	@XmlElement(name = "external-identifier")
	@RdfProperty("http://bio2rdf.org/drug_vocabulary:external-id")
	public Collection<XmlExternalId> getExternalIds() {
		return externalIds;
	}

	public void setExternalIds(Collection<XmlExternalId> externalIds) {
		this.externalIds = externalIds;
	}
	
	@XmlTransient
	@RdfProperty("http://purl.org/dc/terms/source")
	public String getSource() {
		return "DrugBank";
	}
	
	public void setSource(String source) {}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
