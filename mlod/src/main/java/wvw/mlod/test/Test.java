package wvw.mlod.test;

import wvw.mlod.repo.drug.aggr.AggregatedDrugRepository;
import wvw.mlod.repo.drug.drugbank.Drugbank;
import wvw.mlod.repo.drug.drugcentral.Drugcentral;
import wvw.mlod.repo.drug.model.Drug;
import wvw.mlod.repo.drug.model.EntityTypes;
import wvw.mlod.repo.drug.model.HealthCondition;
import wvw.query.QueryException;
import wvw.utils.Timer;
import wvw.utils.Timer.TimeUnits;
import wvw.utils.log.Log;

public class Test {

	public static void main(String[] args) throws Exception {
		Timer.newRun();

		Timer.start("load");

		AggregatedDrugRepository repo = new AggregatedDrugRepository(new Drugcentral(), new Drugbank());

		Timer.end("load");

		// - find drug based on exact (case-insensitive) name

		Timer.start("findDrug1");

		Drug erythrom = repo.findDrug("erythromycin", true, false);
		erythrom.print();

		Timer.end("findDrug1");

		Timer.start("findDrug2");

		Drug aminosal = repo.findDrug("aminosalicylic acid", false, true);
		aminosal.print();

		Timer.end("findDrug2");

		Timer.start("findDrug3");

		Drug ppi = repo.findDrug("proton pump inhibitor", false, true);
		ppi.print();

		Timer.end("findDrug3");

		// - find drugs based on regular-expression

		Timer.start("findDrugs1");

		repo.findDrugs(".*warfarin.*", false, true).stream().forEach(d -> d.print());

		Timer.end("findDrugs1");

		// - find a drug and load its interactions (DDI, DCI)

		Timer.start("findDrugAndItsInteractions");

		Drug warfarin = repo.findDrug("warfarin", true, false, EntityTypes.DDI, EntityTypes.DCI);
		warfarin.print();

		Timer.end("findDrugAndItsInteractions");

		// - find a specific DDI between two drugs

		Timer.start("findDDI1");

		repo.findDDIs(warfarin, erythrom).stream().forEach(d -> Log.d(d));

		Timer.end("findDDI1");

		// - find a health condition

		Timer.start("findCondition1");

		HealthCondition ulcer = repo.findHealthCondition("duodenal ulcer disease", true);
		Log.i(ulcer);

		Timer.end("findCondition1");

		Timer.start("findCondition2");

		HealthCondition osteop = repo.findHealthCondition("osteoporosis", true);
		Log.i(osteop);

		Timer.end("findCondition2");

		// - find a specific DCI between a drug and health condition

		Timer.start("findDCI1");

		repo.findDCIs(aminosal, ulcer).stream().forEach(dci -> Log.i(dci));

		Timer.end("findDCI1");

		Timer.start("findDCI2");

		HealthCondition diabetes = new HealthCondition("http://bio2rdf.org/drug_condition:21000604",
				"Diabetes mellitus");
		Drug methylclot = new Drug("http://bio2rdf.org/drug:5911", "methylclothiazide");
		// (need structure for finding DCIs)
		repo.load(methylclot, EntityTypes.STRUCT);
		
		repo.findDCIs(methylclot, diabetes).stream().forEach(dci -> Log.d(dci));

		Timer.end("findDCI2");

		Timer.start("findDCI3");

		repo.findDCIs(ppi, osteop).stream().forEach(dci -> Log.i(dci));

		Timer.end("findDCI3");

		// - find health conditions based on exact (case-insensitive) name
		// for each condition, find their DCI

		Timer.start("findConditionsAndTheirDCIs");

		repo.findHealthConditions("Diabetes mellitus", true, EntityTypes.DCI).stream().forEach(h -> Log.i(h));

		Timer.end("findConditionsAndTheirDCIs");

		// - find a drug category based on regular-expression and load all drugs within
		// that category

		Timer.start("findCategoriesAndTheirDrugs");

		repo.findDrugCategories(".*thiazide.*", false).stream().forEach(cat -> {
			Log.i(cat);

			try {
				repo.findDrugsInCategory(cat).stream().forEach(cd -> cd.print());

			} catch (QueryException e) {
				e.printStackTrace();
			}
		});

		Timer.end("findCategoriesAndTheirDrugs");

		Timer.printTimes(TimeUnits.SEC);
	}
}