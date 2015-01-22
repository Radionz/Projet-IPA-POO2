package zuul.entities.items;

import zuul.studies.Lesson;


public class LessonPaper extends Item{
	
	private Lesson lesson;
	private int id;
	
	public int getId() {
		return id;
	}
	
	public LessonPaper(Lesson lesson, int energy){
		super(lesson.getCourse(), energy);
		this.lesson = lesson;
		id = lesson.getId();
	}

}
