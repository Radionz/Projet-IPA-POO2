package zuul.io;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public abstract class IO {

    private static JSONObject jso =  new JSONObject();
    private static JSONParser jsp = new JSONParser();
    private static final String DOC_PATH = System.getProperty("user.dir")+"/";


    public enum PossibleFiles{
        OTHER_LESSON("lesson_other.json"), POO_LESSON("lesson_poo.json"), POO_QUESTION("question_poo.json"), ENGLISH("english.json");

        private String value;
        PossibleFiles(String value){
            this.value = value;
        }

        public String getPath(){
            return this.value;
        }

    }

    /**
     * Add a String to a JSON file using the key in parameter.
     * @param key The key to add the string.
     * @param s The string to add.
     * @param fileUrl String path of the file.
     * @return Json object
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static JSONObject addToFileByName(String key, String s, String fileUrl) throws IOException {
        FileWriter file = new FileWriter(DOC_PATH + fileUrl); // open the file
        jso.put(key, s); // put the string in the JSON object
        try {
            file.write(jso.toJSONString()); // Write the json object in the file.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
        file.close();
        return jso;
    }

    /**
     * Add an Object to the Json file.
     * @param key the place to add the object.
     * @param value the object to add
     * @param fileUrl String path of the file.
     * @return Json object
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static JSONObject addToFileAnObject(String key, Object value, String fileUrl) throws IOException {
        FileWriter file = new FileWriter(fileUrl); // open the file
        // different cases
        if (value instanceof String) { // String ?
            jso.put(key, value);
        }
        // TODO other checks

        try {
            file.write(jso.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
        file.close();
        return jso;

    }

    /** Get a String from the Json file.
     * @param i The index of the string to get.
     * @param fileUrl String path of the file.
     * @return Expected string
     * @throws IOException
     */
    public static String getFromFile(int i, String fileUrl) throws IOException {
        String string = null;
        FileReader file = new FileReader(fileUrl); // open the file
        Object obj = null;
        try {
            file = new FileReader(fileUrl);
            obj = jsp.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            file.close();
        }
        if (obj != null) {
            JSONObject jso = (JSONObject) obj;
            string = (String) jso.get(String.valueOf(i));
        }
        file.close();
        return string;
    }
    
    @SuppressWarnings({ "resource", "unchecked" })
	public static HashMap<String, String> getFromFile(String fileUrl) throws IOException {
        HashMap<String, String> contantes = new HashMap<String, String>();
        FileReader file = new FileReader(fileUrl); // open the file
        Object obj = null;
        try {
            file = new FileReader(fileUrl);
            obj = jsp.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            file.close();
        }
        if (obj != null) {
        	contantes = (HashMap<String, String>)obj;
        }
        file.close();
        return contantes;
    }

    /**
     * If needed, flush the Json Object.
     */
    public static void flushJSON(){
        // flush JSON objects to avoid overflow on different files
        jso = null;
        jsp = null;
        jso = new JSONObject();
        jsp = new JSONParser();
    }

}
