package wvw.mlod.repo.drug.drugbank.xml;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import thewebsemantic.Id;
import thewebsemantic.RdfProperty;

public class XmlDrug extends XmlEntity {

	@XmlPath("drugbank-id[@primary='true']/text()")
	private String id;

	private String name;

	private Collection<XmlDrugFoodInteraction> dfis = new ArrayList<>();
	private Collection<XmlDrugDrugInteraction> ddis = new ArrayList<>();
	private Collection<XmlDrugCategory> categories = new ArrayList<>();
	private Collection<XmlDrugSynonym> synonyms = null;

	public XmlDrug() {
		super(URI.create("http://bio2rdf.org/drug_vocabulary:Drug"));
	}

	@Id
	public URI getUri() {
		return URI.create("http://bio2rdf.org/drug:" + id);
	}

	@XmlElement(name = "drugbank-id")
	@RdfProperty("http://purl.org/dc/terms/identifier")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@RdfProperty("http://purl.org/dc/terms/title")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name = "food-interactions")
	@XmlElement(name = "food-interaction")
	@RdfProperty("http://bio2rdf.org/drug_vocabulary:food-interaction")
	public Collection<XmlDrugFoodInteraction> getDfis() {
		return dfis;
	}

	public void setDfis(Collection<XmlDrugFoodInteraction> dfis) {
		this.dfis = dfis;
	}

	@XmlElementWrapper(name = "drug-interactions")
	@XmlElement(name = "drug-interaction")
	@RdfProperty("http://bio2rdf.org/drug_vocabulary:drug-interaction")
	public Collection<XmlDrugDrugInteraction> getDdis() {
		return ddis;
	}

	public void setDdis(Collection<XmlDrugDrugInteraction> ddis) {
		this.ddis = ddis;
	}

	@XmlElementWrapper(name = "categories")
	@XmlElement(name = "category")
	@RdfProperty("http://bio2rdf.org/drug_vocabulary:drug-category")
	public Collection<XmlDrugCategory> getCategories() {
		return categories;
	}

	public void setCategories(Collection<XmlDrugCategory> categories) {
		this.categories = categories;
	}

	public void setup() {
		for (XmlDrugDrugInteraction ddi : ddis)
			ddi.setup(this);

		for (XmlDrugFoodInteraction dfi : dfis)
			dfi.setup(this);

		for (XmlDrugCategory cat : categories)
			cat.setup(this);
		
		synonyms = Arrays.asList(new XmlDrugSynonym(name));
	}
	
	@RdfProperty("http://bio2rdf.org/drug_vocabulary:drug-synonym")
	public Collection<XmlDrugSynonym> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(Collection<XmlDrugSynonym> synonyms) {
		this.synonyms = synonyms;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
