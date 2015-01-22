package zuul.entities.items;

import zuul.studies.Lab;



public class LabPaper extends Item{
	
	private Lab lab;
	private int id;
	
	public int getId() {
		return id;
	}
	
	public LabPaper(Lab lab, int energy){
		super(lab.getCourse(), energy);
		this.lab = lab;
		id = lab.getId();
	}

}
