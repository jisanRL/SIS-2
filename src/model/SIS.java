package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;

import bean.EnrollmentBean;
import bean.StudentBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import DAO.StudentDAO;
import DAO.EnrollmentDAO;

/*
 * attributes that hold the instance of 
 * 	studentDao and EnrollmentDao
 */
public class SIS {
	private StudentDAO students;
	private EnrollmentDAO enrollment;
	private String namePrefix;
	private int creditTaken;
	
	// ctr ,intializes the instance of the classes
	public SIS() throws ClassNotFoundException {
		this.students = new StudentDAO();
		this.enrollment = new EnrollmentDAO();
	}
	
	// methods
	public Map<String, StudentBean> retrieveStudent(String name_prefix, String credit_taken) throws Exception {
		checkInput(name_prefix, credit_taken);

		this.creditTaken = Integer.parseInt(credit_taken);
		this.namePrefix = name_prefix;
		return this.students.retrieve(namePrefix, creditTaken);
	} 
	
	// helper method that checks the input validities 
	private void checkInput(String name_prefix, String credit_taken) throws SQLException {

		try {
			int taken = Integer.parseInt(credit_taken);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		try {
			String qy = "select * from students where surname like ? and credit_taken >= ?";
			Connection cn = this.students.getDS().getConnection();
			PreparedStatement iv = cn.prepareStatement(qy);
			iv.setString(1, name_prefix);
			iv.setInt(2, Integer.parseInt(credit_taken));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Map<String, EnrollmentBean> retriveEnrollment() throws Exception {
		return this.enrollment.retrieve(namePrefix, creditTaken);
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		SIS sis = new SIS();
		System.out.println("SIS");
		System.out.println("students = " + sis.students);
		System.out.println("enrollment =" + sis.enrollment);
		System.out.println("nPrefix = " + sis.namePrefix);
		System.out.println("creditTaken =" + sis.creditTaken);
		
		
	}
}
