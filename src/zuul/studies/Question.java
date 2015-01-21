package zuul.studies;

import java.io.IOException;

import zuul.Woz;
import zuul.io.IO;


/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Question {


    private int number;
    private boolean done;

    private String body;
    private String question;
    private boolean answer;

    /**
     * Question constructor
     * Create a new Question which is the number : number,
     * and that the body is contained in a json file
     * @param number n'th question to create
     */
    public Question(int number){
        this.done = false;
        this.number = number;
        getQuestionFromFile(IO.PossibleFiles.POO_QUESTION.getPath());
    }

    // Basic getters/setters //

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getNumber() {
        return number;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isAnswer() {
        return answer;
    }


    /**
     * Method looking into json file to get the n'th element and use it as a Question
     * @param path String path of the file
     */
    private void getQuestionFromFile(String path){
        try {
            body = IO.getFromFile(number, path);
        } catch (IOException e){
            e.printStackTrace();
        }
        if(body == null){
            body = "ERROR";
        }
        parseBody();
    }

    /**
     * method parsing the body to extract question annd answer.
     */
    private void parseBody(){
        String splits[] = body.split("\\?");
        this.question = splits[0];
        if(splits[1].length()>1){
            splits[1] = splits[1].substring(1, 2);
        }
        if(splits[1].equals("T")){
            this.answer = true;
            //Woz.writeMsg("true");
        }else if(splits[1].equals("F")){
            this.answer = false;
            //Woz.writeMsg("false");
        }else{
            Woz.writeMsg(splits[1]);
        }
    }

    @Override
    public String toString(){
        return this.body+'\n';
    }
}
