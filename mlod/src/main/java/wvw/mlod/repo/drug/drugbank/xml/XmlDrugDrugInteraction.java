package wvw.mlod.repo.drug.drugbank.xml;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import thewebsemantic.Id;
import thewebsemantic.RdfProperty;
import thewebsemantic.Transient;

public class XmlDrugDrugInteraction extends XmlEntity {

	private String drugId;
	private String drugId2;
	private String name;
	private String description;

	// axis aren't supported by moxy ..

	// @XmlPath("ancestor::drug/child::drugbank-id[@primary='true']/text()")
	// private String drugId;

	public XmlDrugDrugInteraction() {
		super(URI.create("http://bio2rdf.org/drug_vocabulary:Drug-Drug-Interaction"));
	}

	@Id
	public URI getUri() {
		String name = (drugId.compareTo(drugId2) < 0 ? drugId + "_" + drugId2 : drugId2 + "_" + drugId);

		return URI.create("http://bio2rdf.org/drug_interaction:" + name);

		// return URI.create("http://bio2rdf.org/drug_interaction:" +
		// UUID.randomUUID().toString());
	}

	// needed by jenabean
	public void setUri(URI uri) {
	}

	@RdfProperty("http://bio2rdf.org/drug_vocabulary:drug-interactor")
	public URI getInteractor() {
		return URI.create("http://bio2rdf.org/drug:" + drugId2);
	}

	// needed by jenabean
	public void setInteractor(URI uri) {
	}

	@XmlTransient
	@Transient
	public String getDrugId() {
		return drugId;
	}

	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}

	@XmlElement(name = "drugbank-id")
	@Transient
	public String getDrugId2() {
		return drugId2;
	}

	public void setDrugId2(String drugId2) {
		this.drugId2 = drugId2;
	}

	@XmlTransient
	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	@RdfProperty("http://purl.org/dc/terms/description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setup(XmlDrug drug) {
		setDrugId(drug.getId());
	}
}