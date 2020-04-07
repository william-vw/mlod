package wvw.mlod.repo.drug.drugbank.xml;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import thewebsemantic.Id;
import thewebsemantic.RdfProperty;

public class XmlDrugSynonym extends XmlEntity {

	private String name;

	public XmlDrugSynonym() {
		super(URI.create("http://bio2rdf.org/drug_vocabulary:Drug-Synonym"));
	}

	public XmlDrugSynonym(String name) {
		super(URI.create("http://bio2rdf.org/drug_vocabulary:Drug-Synonym"));

		this.name = name;
	}

	@Id
	public URI getUri() {
		try {
			return URI.create("http://bio2rdf.org/drug_synonym:" + URLEncoder.encode(name, "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			return null;
		}
	}

	@RdfProperty("http://purl.org/dc/terms/title")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
