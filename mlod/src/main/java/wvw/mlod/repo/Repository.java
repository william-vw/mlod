package wvw.mlod.repo;

import wvw.utils.log.Log;

public class Repository {

	protected String label;

	public Repository() {
	}

	public Repository(String label) {
		this.label = label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/** 
	 * 
	 * @return repository label
	 */
	
	public String getLabel() {
		return label;
	}

	protected void repoPrint(String msg) {
		Log.i("@" + getLabel() + " - " + msg);
	}
}
