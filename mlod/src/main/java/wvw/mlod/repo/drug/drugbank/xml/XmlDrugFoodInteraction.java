package wvw.mlod.repo.drug.drugbank.xml;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import thewebsemantic.Id;
import thewebsemantic.RdfProperty;
import thewebsemantic.Transient;

// when making a subclass of XmlEntity, getting the following exception:
// The property or field value cannot be annotated with XmlValue since it is a subclass of another XML-bound class.

// so, copy that stuff directly here

public class XmlDrugFoodInteraction {

	private String value;
	protected URI type;

	private XmlFood food;

	public XmlDrugFoodInteraction() {
		this.type = URI.create("http://bio2rdf.org/drug_vocabulary:Drug-Food-Interaction");
	}

	@Id
	public URI getUri() {
		try {
			return URI.create("http://bio2rdf.org/drug_interaction:" + URLEncoder.encode(value, "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			return null;
		}
	}

	@XmlValue
	@Transient
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlTransient
	@RdfProperty("http://bio2rdf.org/drug_vocabulary:food-interactor")
	public XmlFood getFood() {
		return food;
	}

	public void setFood(XmlFood food) {
		this.food = food;
	}

	@XmlTransient
	@RdfProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
	public URI getType() {
		return type;
	}

	public void setType(URI type) {
		this.type = type;
	}

	@XmlTransient
	@RdfProperty("http://purl.org/dc/terms/source")
	public String getSource() {
		return "DrugBank";
	}

	public void setSource(String source) {
	}

	public void setup(XmlDrug drug) {
		food = new XmlFood();

		food.setValue(value);
	}
}