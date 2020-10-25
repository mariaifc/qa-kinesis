package api;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesUtils {

    private static Logger LOGGER = Logger.getLogger(PropertiesUtils.class.getName());

    public static String getPropertyFromConfigFile(String property) {
        return getPropertyFromPropertiesFile("config", property);
    }

    private static String getPropertyFromPropertiesFile(String bundle, String property) {
        try {
            ResourceBundle configBundle = ResourceBundle.getBundle(bundle);

            if (configBundle != null) {
                return configBundle.getString(property);
            }
        } catch (MissingResourceException e) {
            LOGGER.log(Level.WARNING, "Configuration issue: property is absent in " + bundle + ".properties");
        }
        throw new IllegalStateException("Cannot get the property from " + bundle + ".properties");
    }

}
