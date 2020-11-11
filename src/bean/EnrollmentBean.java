package bean;

import java.util.ArrayList;
import java.util.List;

/*
 * this class is a simple data strucutre 
 * holds information about one course 
 * 
 * @attr 
 * 		cid , 		course ID 
 * 		students, 	list of studentIDs who are enrolled in the course 
 * 		credit, 	credit taken
 * 
 */
public class EnrollmentBean {

	private String cid;
	private List<String> students = new ArrayList<String>();
	private int credit;

	//ctr
	public EnrollmentBean() {
		
	}

	// getters and setters 
	public String getCid() {
		return cid;
	}


	public void setCid(String cid) {
		this.cid = cid;
	}

	public List<String> getStudents() {
		return students;
	}


	public void setStudents(String sid) {
		this.students.add(sid);
	}


	public int getCredit() {
		return credit;
	}


	public void setCredit(int credit) {
		this.credit = credit;
	}

	
}
