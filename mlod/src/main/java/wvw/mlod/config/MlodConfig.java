package wvw.mlod.config;

import wvw.utils.Config;

public class MlodConfig extends Config {

	static {
		init(MlodConfig.class);
	}

	public static String dbUsn() {
		return getStringProperty("dbUsn");
	}

	public static String dbPwd() {
		return getStringProperty("dbPwd");
	}

	public static String dbUrl() {
		return getStringProperty("dbUrl");
	}

	public static String drugbankTdbFolder() {
		return getStringProperty("drugbankTdbFolder");
	}

	public static String drugbankXmlFile() {
		return getStringProperty("drugbankXmlFile");
	}

	public static String drugbankSegmFolder() {
		return getStringProperty("drugbankSegmFolder");
	}
	
	public static String drugbankRdfFolder() {
		return getStringProperty("drugbankRdfFolder");
	}
	
	public static String drugcentralObdaFile() {
		return getStringProperty("drugcentralObdaFile");
	}
	
	public static String drugcentralPropFile() {
		return getStringProperty("drugcentralPropFile");
	}
	
	public static String drugcentralOwlFile() {
		return getStringProperty("drugcentralOwlFile");
	}
}
