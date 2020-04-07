package wvw.mlod.repo.drug.drugbank.xml;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import thewebsemantic.Id;
import thewebsemantic.RdfProperty;

public class XmlFood extends XmlEntity {

	private String value;

	public XmlFood() {
		this.type = URI.create("http://bio2rdf.org/drug_vocabulary:Food");
	}

	@Id
	public URI getUri() {
		try {
			return URI.create("http://bio2rdf.org/drug_food:" + URLEncoder.encode(value, "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			return null;
		}
	}

	@RdfProperty("http://purl.org/dc/terms/title")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
