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
    public TestConfig(int type) throws IOException {
        dictionary = new HashMap<>();

        if(type==0){
            dictionary.put("probabilityDoorStuck","0.9");
    }else if(type==1){
            dictionary.put("probabilityStuck","0.9");
        }
        dictionary.put("dateFormatPattern","dd-MM-yyyy HH:mm:ss");
        dictionary.put("waitTime","9175");
        dictionary.put("csvFileName", "test.csv");
        dictionary.put("distanceBetweenFloors", "3.5");
        dictionary.put("startDate", "01-01-2021 14:00:00");
        dictionary.put("velocity", "1.27");
        dictionary.put("numElevators","4");
        dictionary.put("numFloors","22");
        dictionary.put("numHandlerThreads", "10");
        dictionary.put("timeout","50000");
        dictionary.put("maxMessageSize","1024");
        dictionary.put("schedulerPort","8000");
        dictionary.put("floorPort","9000");
        dictionary.put("elevatorPort","7000");
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

    public void addDictElem(String s1, String s2){
        dictionary.put(s1,s2);
    }
}
