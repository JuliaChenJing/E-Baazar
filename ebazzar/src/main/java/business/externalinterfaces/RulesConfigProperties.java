package business.externalinterfaces;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

public class RulesConfigProperties {

    private static final String PROPERTIES = "rulesconfig.properties";
    private static final Logger LOG = Logger.getLogger("");
    private static final String PROPS = PROPERTIES;
    public static Properties props;

    static {
        if (props == null) {
            readProps();
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);

    }

    private static void readProps() {
        readProps(PROPS);

    }

    /**
     * This method allows a client of this properties configurator to point to a
     * different location for the properties file.
     *
     * @param propsLoc
     */
    public static void readProps(String loc) {
        System.out.println(loc);
        Properties ret = new Properties();
        URL url = RulesConfigProperties.class.getClassLoader().
                getResource(loc);
        try {
            ret.load(url.openStream());
        } catch (IOException e) {
            LOG.warning("Unable to read properties file for Ebazaar");
        } finally {
            props = ret;
        }

    }
}
