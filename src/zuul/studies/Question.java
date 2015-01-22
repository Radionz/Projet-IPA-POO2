package zuul.studies;

public class Question {

	private String question;
	private boolean answer;

	public String getQuestion() {
		return question;
	}

	public boolean getAnswer() {
		return answer;
	}

	public Question(String question, boolean answer) {
		this.question = question;
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "Question : " + question + " - Answer : " + answer;
	}
	
	public String ask() {
		return "Question : " + question;
	}

	public boolean isAnswer(boolean answer) {
		return this.answer == answer;
	}

}
