package wvw.mlod.repo.drug.drugbank.xml;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import javax.xml.bind.annotation.XmlElement;

import thewebsemantic.Id;
import thewebsemantic.RdfProperty;

public class XmlExternalId extends XmlEntity {

	private String extSource;
	private String identifier;

	public XmlExternalId() {
		super(URI.create("http://bio2rdf.org/drug_vocabulary:External-Id"));
	}

	@Id
	public URI getUri() {
		try {			
			return URI.create("http://bio2rdf.org/drug_id:" + URLEncoder.encode(extSource, "UTF-8") + "_"
					+ URLEncoder.encode(identifier, "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			return null;
		}
	}

	@XmlElement(name = "resource")
	@RdfProperty("http://bio2rdf.org/drug_vocabulary:external-source")
	public String getExternalSource() {
		return extSource;
	}

	public void setExternalSource(String extSource) {
		this.extSource = extSource;
	}

	@XmlElement
	@RdfProperty("http://purl.org/dc/terms/identifier")
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
