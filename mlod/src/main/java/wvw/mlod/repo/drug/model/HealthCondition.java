package wvw.mlod.repo.drug.model;

import java.util.ArrayList;
import java.util.List;

import wvw.utils.log.Log;

public class HealthCondition extends MedicalEntity {

	private List<DrugConditionInteraction> dcis = new ArrayList<>();

	public HealthCondition() {
		super();
	}

	public HealthCondition(String uri, String label) {
		super(uri, label);
	}

	public HealthCondition(String uri) {
		super(uri);
	}

	@Override
	public EntityTypes getType() {
		return EntityTypes.CONDITION;
	}

	public List<DrugConditionInteraction> getDcis() {
		return dcis;
	}

	public void setDcis(List<DrugConditionInteraction> dcis) {
		this.dcis = dcis;
	}

	public void print() {
		Log.i(getUri() + " \"" + getLabel() + "\" (@" + getSource() + ")");
		getDcis().stream().forEach(dci -> Log.i("dci: " + dci));
		Log.i("");
	}

	public String toString() {
		return getUri() + ": " + getLabel();
		// return getLabel() + (getIds().isEmpty() ? "" : " (" + getIds() + ")");
	}
}
