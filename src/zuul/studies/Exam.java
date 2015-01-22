package zuul.studies;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Exam {

	private int id;
	private ArrayList<Question> questions;
	private Lab lab;
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
	
	private int getNumberOfQuestions() {
		return questions.size();
	}
	
	public String getCourse() {
		return lab.getCourse();
	}

	public Exam(Lab lab) {
		this.lab = lab;
		this.id = (int) lab.getId();
		this.questions = lab.getLesson().getQuestions();
		this.mark = 0;
		this.done = false;
	}

	public int takeAnExam() {
		if (!lab.isDone()) {
			System.err.println("You must have passed the lab of "+getCourse()+" before passing this Exam.");
			return 0;
		}
		System.out.println("--- Exam of " +getCourse()+" ---");
		scanner = new Scanner(System.in);
		for (Question question : questions) {
			mark += askAQuestion(question) ? 1 : 0;
		}
		//scanner.close();
		System.out.println("You've got a " + getMark20() + "/20.");
		this.done = true;
		return mark;
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
		return lab.isPoo();
	}
}
