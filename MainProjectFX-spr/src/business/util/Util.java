package business.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Util {
	private final static String REL_RULES_PATH = "business/rulefiles";
	public static BufferedReader pathToRules(ClassLoader classLoader, String filename) throws IOException {
	    URL url = classLoader.getResource(REL_RULES_PATH + "/" + filename);
	    BufferedReader buf = new BufferedReader((new InputStreamReader(url.openStream())));
	    return buf;
	}
}
