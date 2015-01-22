package zuul.studies;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Lab {

	private int id;
	private ArrayList<Question> questions;
	private Lesson lesson;
	private int mark;
	private Scanner scanner;
	private boolean done;

	public int getId() {
		return id;
	}

	public boolean isDone() {
		return done;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public int getMark() {
		return mark;
	}

	public int getMark20() {
		return mark * 20 / getNumberOfQuestions();
	}
	
	public String getCourse() {
		return lesson.getCourse();
	}
	
	public Lesson getLesson() {
		return lesson;
	}

	public Lab(Lesson lesson) {
		this.lesson = lesson;
		this.id = (int) lesson.getId();
		this.questions = lesson.getQuestions();
		this.mark = 0;
		this.done = false;
	}

	public Lab(Lesson lesson, int numberOfQuestion) {
		this.lesson = lesson;
		this.questions = new ArrayList<Question>();
		Random random = new Random();
		if (numberOfQuestion > lesson.getNumberOfQuestions()) {
			numberOfQuestion = lesson.getNumberOfQuestions();
			System.err
					.println("You asked to have more questions than there are in the course.");
		}
		while (questions.size() < numberOfQuestion)
			addQuestion(lesson.getRandomQuestion(random));
		this.mark = 0;
		this.done = false;
	}

	private void addQuestion(Question question) {
		if (!questions.contains(question))
			questions.add(question);
	}

	public int getNumberOfQuestions() {
		return questions.size();
	}

	public int takeALab() {
		if (!lesson.isDone()) {
			System.err.println("You must have attended the course of "+getCourse()+" before passing this Lab.");
			return 0;
		}
		System.out.println("--- Lab of " +getCourse()+" ---");
		scanner = new Scanner(System.in);
		for (Question question : questions) {
			mark += askAQuestion(question) ? 1 : 0;
		}
		//scanner.close();
		System.out.println("You've got a " + getMark20() + "/20.");
		this.done = true;
		return getMark20();
	}

	private boolean askAQuestion(Question question) {
		System.out.println(question.ask());
		try {
			boolean answer = scanner.nextBoolean();
			return question.isAnswer(answer);
		} catch (InputMismatchException e) {
			System.err.println("You must answer true or false.");
			scanner.next();
			askAQuestion(question);
		}
		return false;
	}

	public boolean isPassed() {
		if (!done)
			return false;
		return getMark20() >= 10;
	}

	public boolean isPoo() {
		return lesson.isPoo();
	}
}
