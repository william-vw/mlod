package wvw.mlod.repo.drug.drugbank.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import thewebsemantic.binding.Jenabean;
import wvw.utils.log.Log;

public class XmlDrugbankParser {

	public XmlDrugbankParser() {
	}

	/**
	 * Segment the Drugbank XML file into separate XML segments for easier
	 * processing.
	 * 
	 * @param inPath    XML file to be segmented
	 * @param outFolder folder where XML segments will be stored
	 * @param prefix    prefix for XML segment file names
	 * @param size      number of drugs in each XML segment
	 * @param limit     number of XML segments to be created
	 * @throws Exception
	 */

	// TODO last file will contain two closing drugbank tags

	public void segmentXml(String inPath, String outFolder, String prefix, int size, int limit) throws IOException {
		new File(outFolder).mkdir();

		BufferedReader br = new BufferedReader(new FileReader(inPath));

		String preamble = br.readLine() + "\n";
		// causes issues for JAXB
		preamble += br.readLine().replace("xmlns=\"http://www.drugbank.ca\" ", "") + "\n";

		int nr = 0;
		int cnt = 0;
		String line = null;
		do {
			String outPath = outFolder + prefix + "-" + (size * (nr + 1)) + ".xml";
			Writer writer = new OutputStreamWriter(new FileOutputStream(outPath), StandardCharsets.UTF_8);

			writer.write(preamble);

			while ((line = br.readLine()) != null) {
				if (line.startsWith("</drugbank>"))
					break;

				writer.write(line + "\n");

				if (line.startsWith("</drug>")) {
					if (++cnt == size)
						break;
				}
			}

			writer.write("\n</drugbank>");

			Log.i("written " + cnt + " drugs to " + outPath);

			if (cnt == 0)
				break;

			cnt = 0;
			writer.close();

		} while (++nr < limit);

		br.close();
	}

	/**
	 * Parse XML segments, created by
	 * {@link #segmentXml(String, String, String, int, int)} and each containing a
	 * set of drugs from Drugbank, into RDF.
	 * 
	 * @param inFolder  folder containing the XML segments.
	 * @param outFolder folder where RDF segments will be stored
	 * @param from      start point for parsing
	 * @param step      should correspond to size parameter of
	 *                  {@link #segmentXml(String, String, String, int, int)}
	 *                  method.
	 * @param to        end point for parsing
	 * @throws IOException
	 * @throws JAXBException
	 * @throws Exception
	 */

	public void parseXmlSegments(String inFolder, String outFolder, int from, int step)
			throws JAXBException, IOException {

		int cnt = 0;
		double total = 0;

		new File(outFolder).mkdir();

		int to = new File(inFolder).listFiles().length * step;
		while (from <= to) {
			long start = System.currentTimeMillis();

			String name = "drug_segment-" + from;

			File inFile = new File(inFolder + name + ".xml");
			if (!inFile.exists() || inFile.length() == 0)
				break;

			XmlDrugbank db = parseXml(inFolder + name + ".xml");
			Log.i("parsed " + name);

			printRdf(db, outFolder + name + ".n3");
			Log.i("printed " + name);

			long end = System.currentTimeMillis();
			long time = (end - start);
			Log.i("time: " + time);

			cnt++;
			total += time;

			double avg = total / cnt;

			double remaining = (avg * (to - from) / step) / 1000;
			Log.i("remaining: " + remaining + " s (" + (remaining / 60) + " m)");

			from += step;

			Log.i("");
		}
	}

	private XmlDrugbank parseXml(String path) throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(XmlDrugbank.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		XmlDrugbank xdb = (XmlDrugbank) jaxbUnmarshaller.unmarshal(new FileInputStream(path));
		xdb.setup();

		return xdb;
	}

	private void printRdf(XmlDrugbank db, String outPath) throws IOException {
		Model m = ModelFactory.createDefaultModel();
		Jenabean instance = Jenabean.instance();

		instance.bind(m);

		db.save(true);

		m.write(new FileOutputStream(outPath), "N3");
		// m.write(System.out, "N3");
	}

//	// TODO could likely be optimized by using commons-io or guava
//
//	public void fixXml(String inPath, String outPath) throws Exception {
//		BufferedReader br = new BufferedReader(new FileReader(inPath));
//		Writer writer = new OutputStreamWriter(new FileOutputStream(outPath), StandardCharsets.UTF_8);
//
//		writer.write(br.readLine() + "\n");
//
//		// causes issues for JAXB
//		String line = br.readLine();
//		line = line.replace("xmlns=\"http://www.drugbank.ca\" ", "");
//
//		int cnt = 2;
//		do {
//			writer.write(line + "\n");
//
//			if (cnt % 100000 == 0)
//				Log.i("copied line " + cnt);
//
//			cnt++;
//
//		} while ((line = br.readLine()) != null);
//
//		writer.close();
//
//		br.close();
//	}
}