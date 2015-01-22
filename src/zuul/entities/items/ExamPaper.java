package zuul.entities.items;

import zuul.studies.Exam;



public class ExamPaper extends Item {

	private Exam exam;
	private int id;

	public int getId() {
		return id;
	}

	public ExamPaper(Exam exam, int energy) {
		super(exam.getCourse(), energy);
		this.exam = exam;
		id = exam.getId();
	}
}
