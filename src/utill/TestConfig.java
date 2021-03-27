package utill;

import java.io.IOException;
import java.util.HashMap;

public class TestConfig extends Config {

    private HashMap<String,String> dictionary;

    /**
     * The default constructor to load the configuration file.
     *
     * @throws IOException If there is a problem loading the file.
     */
    public TestConfig() throws IOException {
        dictionary = new HashMap<>();

        dictionary.put("dateFormatPattern","dd-MM-yyyy HH:mm:ss");
        dictionary.put("waitTime","9175");
        dictionary.put("distanceBetweenFloors", "3.5");
    }
    /**
     * Get a property from the configuration file.
     *
     * @param key The key/label of the property in the file.
     * @return The property as a string.
     */
    public String getProperty(String key) {
        return this.dictionary.get(key);
    }

    /**
     * Get a property as a integer from the configuration file.
     *
     * @param key The key/label of the property in the file.
     * @return The property as a integer.
     * @throws NumberFormatException If the property does not contain a parsable integer.
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    /**
     * Get a property as a float from the configuration file.
     *
     * @param key The key/label of the property in the file.
     * @return The property as a integer.
     * @throws NumberFormatException If the property does not contain a parsable float.
     */
    public float getFloatProperty(String key) {
        return Float.parseFloat(getProperty(key));
    }
}
