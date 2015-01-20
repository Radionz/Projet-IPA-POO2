package zuul.entities.items;

/**
 * Created by user on 01/12/14.
 */
public class PaperLesson extends Item{

    private int number;

    //TODO create a new PaperLesson when the lesson is over and add it to the players inventory
    public PaperLesson(String name, int number){
        super(name);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
