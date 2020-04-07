package wvw.utils.jena;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

import wvw.utils.IOUtils;
import wvw.utils.log.Log;

public class TdbUtils {

	public static void initFromFile(String tdbFolder, String dataFile, String rdfFormat) throws Exception {
		IOUtils.deleteContents(new File(tdbFolder));

		Dataset dataset = TDBFactory.createDataset(tdbFolder);
		Model model = dataset.getDefaultModel();

		dataset.begin(ReadWrite.WRITE);
		model.read(new FileInputStream(new File(dataFile)), "", rdfFormat);

		dataset.commit();
		dataset.close();
	}

	public static void initFromFolder(String tdbFolder, String dataFolder, String rdfFormat) throws IOException {
		File folder = new File(tdbFolder);
		folder.mkdir();
		IOUtils.deleteContents(folder);

		Dataset dataset = TDBFactory.createDataset(tdbFolder);
		Model model = dataset.getDefaultModel();

		dataset.begin(ReadWrite.WRITE);

		File[] files = new File(dataFolder).listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];

			Log.i("reading " + file.getName() + " (" + (i + 1) + "/" + files.length + ")");
			model.read(new FileInputStream(file), "", rdfFormat);
		}

		dataset.commit();
		dataset.close();
	}

	public static Model loadModel(String tdbFolder) {
		Dataset dataset = TDBFactory.createDataset(tdbFolder);

		return dataset.getDefaultModel();
	}
}
