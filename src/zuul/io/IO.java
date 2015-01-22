package zuul.io;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public abstract class IO {

	private static JSONObject jso = new JSONObject();
	private static JSONParser jsp = new JSONParser();
	private static final String DOC_PATH = System.getProperty("user.dir") + "/";
	private static final String LESSON_PATH = "data/lessons/";
	private static final String LANGUAGE_PATH = "data/languages/";
	private static ArrayList<String> languagesPaths;
	private static ArrayList<String> lessonsPaths;

	public static ArrayList<String> getPossibleLessonsPaths() {
		lessonsPaths = new ArrayList<String>();
		File folder = new File(DOC_PATH + LESSON_PATH);
		File[] jsonFile = folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});
		for (File file : jsonFile) {
			lessonsPaths.add(file.getPath());
		}
		return lessonsPaths;
	}

	public static ArrayList<String> getPossibleLanguagesPaths() {
		languagesPaths = new ArrayList<String>();
		File folder = new File(DOC_PATH + LANGUAGE_PATH);
		File[] jsonFile = folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});
		for (File file : jsonFile) {
			languagesPaths.add(file.getPath());
		}
		return languagesPaths;
	}

	/**
	 * Add a String to a JSON file using the key in parameter.
	 * 
	 * @param key
	 *            The key to add the string.
	 * @param s
	 *            The string to add.
	 * @param fileUrl
	 *            String path of the file.
	 * @return Json object
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject addToFileByName(String key, String s,
			String fileUrl) throws IOException {
		FileWriter file = new FileWriter(fileUrl); // open the file
		jso.put(key, s); // put the string in the JSON object
		try {
			file.write(jso.toJSONString()); // Write the json object in the
											// file.
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
	 * 
	 * @param key
	 *            the place to add the object.
	 * @param value
	 *            the object to add
	 * @param fileUrl
	 *            String path of the file.
	 * @return Json object
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject addToFileAnObject(String key, Object value,
			String fileUrl) throws IOException {
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

	/**
	 * Get a String from the Json file.
	 * 
	 * @param i
	 *            The index of the string to get.
	 * @param fileUrl
	 *            String path of the file.
	 * @return Expected string
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
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
	public static HashMap<String, Object> getFromFile(String fileUrl)
			throws IOException {
		HashMap<String, Object> contantes = new HashMap<String, Object>();
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
			contantes = (HashMap<String, Object>) obj;
		}
		file.close();
		return contantes;
	}

	/**
	 * If needed, flush the Json Object.
	 */
	public static void flushJSON() {
		// flush JSON objects to avoid overflow on different files
		jso = null;
		jsp = null;
		jso = new JSONObject();
		jsp = new JSONParser();
	}

}
