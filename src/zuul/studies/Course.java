package zuul.studies;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import zuul.io.IO;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Course {

    private int number;
    private boolean done;
    private boolean isPOO;

    // List of string : phrase the teacher says in the lesson
    private ArrayList<String> insts;


    public Course(boolean isPOO, int number){
        this.done = false;
        this.isPOO = isPOO;
        this.number = number;
        insts = new ArrayList<>(100);
        if(isPOO){
            getLessonFromFile(IO.PossibleFiles.POO_LESSON.getPath());
        }else{
            getLessonFromFile(IO.PossibleFiles.OTHER_LESSON.getPath());
        }
    }

    // Basic getters/setters //
    public boolean isPOO() {
        return isPOO;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getNumber() {
        return number;
    }


    /**
     * method allowing you to get the n'th sentences of teacher speech
     * @param number number of the speech line
     * @return the n'th speech line
     */
    public String getSentence(int number){
    	if (insts.size()<=number) {
    		this.done = true;
    		return "END";
		}else{
			return insts.get(number);
		}
    }


    /**
     * Method looking into json file to get the n'th element and use it as a lesson
     * @param path String path of the file
     */
    private void getLessonFromFile(String path){
        String body = null;
        try {
            body = IO.getFromFile(number, path);
        } catch (IOException e){
            e.printStackTrace();
        }
        if(body == null){
            body = "ERROR";
        }
        parseBody(body);
    }

    /**
     * method parsing the body to extract n substring.
     * @param body string to parse/split
     */
    private void parseBody(String body){
        String splits[] = body.split("\\.");
        Collections.addAll(insts, splits);
    }

    @Override
    public String toString(){
        String res ="";
        for(String s : insts){
            res += s + '\n';
        }

        return res;
    }


}
