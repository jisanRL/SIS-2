package bean;

/*
 * this class is a simple data strucutre 
 * hold information about one student 
 * 
 * @attr 
 * 	sid, student ID number eg:cse67895
 * 	name, sutdent name eg: Philip rodriguez
 * 	credit_taken, eg 81
 * 	credit_graduate eg 90
 * 
 */
public class StudentBean {

	private String sid;
	private String name;
	private int credit_taken;
	private int credit_grad;
	
	// ctr
	public StudentBean() {
		
	}
	
	// getters and setters
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCredit_taken() {
		return credit_taken;
	}

	public void setCredit_taken(int credit_taken) {
		this.credit_taken = credit_taken;
	}

	public int getCredit_grad() {
		return credit_grad;
	}

	public void setCredit_grad(int credit_grad) {
		this.credit_grad = credit_grad;
	}
	
}
