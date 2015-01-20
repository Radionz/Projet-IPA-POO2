package zuul.studies;

import java.util.ArrayList;
import java.util.Random;

import zuul.Game;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Exam {

    private ArrayList<Question> questions;
    private int pointerQuestion = 0;
    private int grade = 0;

	/**
	 * Exam constructor
	 */
    public Exam(){
        questions = new ArrayList<Question>();
		for(int i = 0; i < 5; i++){
			questions.add(Game.getQuestions()[(i*3)+ new Random().nextInt(3)]);
		}
    }


	// getters / setters //
    public ArrayList<Question> getQuestions() {
        return questions;
    }

	/**
	 * method allowing you to answer a question
	 * @param answer player's answer
	 * @return if the answer is right
	 */
	public String answerQuestion(boolean answer) {
		Question q = questions.get(pointerQuestion);
		if(q.isAnswer() == answer)
			grade++;
		pointerQuestion++;
		return nextQuestion();
	}


	/**
	 * method allowing you to display the next question
	 * @return string of the question
	 */
	private String nextQuestion() {
		if (pointerQuestion < questions.size()) {
			return questions.get(pointerQuestion).getQuestion();
		}else{
			return "Exam done, you've got "+grade+"/5";
		}
	}

	/**
	 * method allowing you to ask a question
	 * @return string of the question
	 */
	public String askQuestion() {
		return questions.get(pointerQuestion).getQuestion();
	}
}
