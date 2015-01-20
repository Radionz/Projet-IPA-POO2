package zuul.studies;

import zuul.Game;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Lab {

    private Question[] questions;
    private boolean succeed;
    private int questionNum;
    private int grade;
    private int i;

    public Lab(int i){
        this.i = i;
        questions = new Question[3];
        //System.arraycopy(Game.getQuestions(), i * 3, questions, 0, i);
        for(int k = 0; k<3;k++){
            questions[k]=Game.getQuestions()[(i*3)+k];
        }
        this.questionNum = 0;
        this.grade = 0;
        this.succeed = false;
    }

    /* basic getters */
    public Question[] getQuestions() {
        return questions;
    }

    public void isSucceed(){
        this.succeed= (this.questionNum == 3);
    }

    public boolean getSuccess(){
        isSucceed();
        return this.succeed;
    }
    /* basic getters */

    /**
     * method allowing you to display a question
     * @return a string of the question
     */
    public String askQuestion() {
        //return Game.getQuestions()[Game.getPlayer().getCurrentPOOLevel()*3+questionNum].getQuestion();
        return questions[questionNum].getQuestion();
    }

    /**
     * method allowing you to answer a question
     * @param answer answer to check
     * @return if the answer is right
     */
    public String answerQuestion(boolean answer) {
        Question q = questions[questionNum];
        System.out.println(q.isAnswer());
        if(answer == q.isAnswer()){
            grade++;
        }
        questionNum++;
        return nextQuestion();
    }

    /**
     * method allowing you to display next question
     * @return string of the next question
     */
    private String nextQuestion() {
        if (questionNum < questions.length) {
            return this.getQuestions()[questionNum].getQuestion();
        }else{
            return "Lab done, you've got "+ grade +"/3";
        }
    }
}
