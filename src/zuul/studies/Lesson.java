package zuul.studies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.plaf.SliderUI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import zuul.Woz;
import zuul.io.IO;

public class Lesson {

	private int id;
	private String course;
	private boolean poo;
	private boolean done;
	private ArrayList<String> sentences;
	private ArrayList<Question> questions;

	public Lesson(String path) {
		HashMap<String, Object> lesson = null;
		try {
			lesson = IO.getFromFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.id = Integer.parseInt(lesson.get("id").toString());
		this.course = (String) lesson.get("course");
		this.poo = course.equals("POO");
		this.done = false;

		this.sentences = new ArrayList<String>();
		sentencesFromString((String) lesson.get("lesson"));

		this.questions = new ArrayList<Question>();
		buildQuestions((JSONArray) lesson.get("questions"));
	}

	public int getId() {
		return id;
	}

	public Lesson getLesson() {
		return this;
	}

	public String getCourse() {
		return course;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isPoo() {
		return poo;
	}

	public ArrayList<String> getSentences() {
		return sentences;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	@SuppressWarnings("unchecked")
	private void buildQuestions(JSONArray questions) {
		for (Object question : questions) {
			String q = (String) ((HashMap<String, Object>) question)
					.get("question");
			boolean a = (boolean) ((HashMap<String, Object>) question)
					.get("answer");
			this.questions.add(new Question(q, a));
		}
	}

	/**
	 * method parsing the body to extract n substring.
	 * 
	 * @param body
	 *            string to parse/split
	 */
	private void sentencesFromString(String body) {
		String splits[] = body.split("\\.");
		Collections.addAll(sentences, splits);
	}

	@Override
	public String toString() {
		String result = "";
		result += "--- " + course + " ---";
		for (String sentence : sentences) {
			result += sentence + '\n';
		}
		return result;
	}

	public void printLesson() {
		Woz.writeMsg("--- " + course + " ---");
		for (String sentence : sentences) {
			Woz.writeMsg(sentence);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.done = true;
	}

	public void printLessonThread() {
		Thread thread = new Thread() {
			public void run() {
				System.out.println("--- " + course + " ---");
				for (String sentence : sentences) {
					// System.out.println(sentence);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				done = true;
			}
		};
		thread.start();
	}

	public int getNumberOfQuestions() {
		return questions.size();
	}

	public Question getRandomQuestion(Random random) {
		return questions.get(random.nextInt(questions.size()));
	}

}
