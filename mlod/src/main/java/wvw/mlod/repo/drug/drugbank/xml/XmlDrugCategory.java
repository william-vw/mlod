package wvw.mlod.repo.drug.drugbank.xml;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import thewebsemantic.Id;
import thewebsemantic.RdfProperty;
import thewebsemantic.Transient;

public class XmlDrugCategory extends XmlEntity {

	private String category;
	private String meshId;

	public XmlDrugCategory() {
		super(URI.create("http://bio2rdf.org/drug_vocabulary:Drug-Category"));
	}

	@Id
	public URI getUri() {
		try {
			return URI.create("http://bio2rdf.org/drug_category:" + URLEncoder.encode(category, "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			return null;
		}
	}

	@XmlElement
	@RdfProperty("http://purl.org/dc/terms/title")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@XmlElement(name = "mesh-id")
	@Transient
	public String getMeshId() {
		return meshId;
	}

	public void setMeshId(String meshId) {
		this.meshId = meshId;
	}

	public void setup(XmlDrug drug) {
		if (meshId != null && !meshId.equals("")) {
			XmlExternalId xid = new XmlExternalId();
			xid.setIdentifier(meshId);
			xid.setExternalSource("MeSH");

			getExternalIds().add(xid);
		}
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
