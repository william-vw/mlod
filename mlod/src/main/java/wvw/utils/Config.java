package wvw.utils;

import java.io.IOException;
import java.util.Properties;

import wvw.utils.log.Log;

public class Config {

	public static Properties config = new Properties();

	@SuppressWarnings("rawtypes")
	protected static void init(Class cls) {
		try {
			config.load(cls.getResourceAsStream("config.properties"));
			Log.d("config? " + config);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static int getIntProperty(String name) {
		return Integer.valueOf(config.getProperty(name));
	}

	protected static double getDoubleProperty(String name) {
		return Double.valueOf(config.getProperty(name));
	}

	protected static String getStringProperty(String name) {
		return config.getProperty(name);
	}
}
