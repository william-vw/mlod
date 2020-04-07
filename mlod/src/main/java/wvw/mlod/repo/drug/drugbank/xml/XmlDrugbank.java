package wvw.mlod.repo.drug.drugbank.xml;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import thewebsemantic.Id;
import thewebsemantic.RdfProperty;

@XmlRootElement(name = "drugbank")
public class XmlDrugbank extends XmlEntity {

	private Collection<XmlDrug> drugs = new ArrayList<>();

	@Id
	public URI getUri() {
		return URI.create("http://bio2rdf.org/drug_resource:DrugBank");
	}
	
	@XmlElement(name = "drug")
	@RdfProperty("http://bio2rdf.org/drug_vocabulary:Drug")
	public Collection<XmlDrug> getDrugs() {
		return drugs;
	}

	public void setDrugs(Collection<XmlDrug> drugs) {
		this.drugs = drugs;
	}
	
	public void setup() {
		for (XmlDrug drug : drugs)
			drug.setup();
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
