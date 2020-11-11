package model;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import bean.EnrollmentBean;
import bean.ListWrapper;
import bean.StudentBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

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
	private ListWrapper lw;
	
	// ctr ,intializes the instance of the classes
	public SIS() throws ClassNotFoundException {
		this.students = new StudentDAO();
		this.enrollment = new EnrollmentDAO();
		this.lw = new ListWrapper();
	}
	
	// methods
	public Map<String, StudentBean> retrieveStudent(String name_prefix, String credit_taken) throws Exception {
		checkInput(name_prefix, credit_taken);

		this.creditTaken = Integer.parseInt(credit_taken);
		this.namePrefix = name_prefix;
		return this.students.retrieve(namePrefix, creditTaken);
	} 
	
	// helper method that checks the input validities 
	private void checkInput(String name_prefix, String credit_taken) throws Exception {
		// checks the type of credit taken
		try {
			int taken = Integer.parseInt(credit_taken);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// check if minimum credits are < 0
		int tkn = Integer.parseInt(credit_taken);
		if (tkn < 0) {
			String st = "Minimum credits taken can't be less then zero";
			throw new Exception(st);
		}
		
		// check db connection is there 
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
	
	// method returns a String that contains the XML elements as described below.
	// check the fileName in the signature
	public String export(String namePrefix, String creditTaken, String filename) throws Exception {
		checkInput( namePrefix, creditTaken);			// check this 
		
		
		Map<String, StudentBean> sb = this.students.retrieve(namePrefix, Integer.parseInt(creditTaken));   // map for studentbean to get the students 
		List<StudentBean> stdList = new ArrayList<StudentBean>(sb.values());						 	   // arraylist for studentbean
		
		lw.setNamePrefix(namePrefix);
		lw.setCredit_taken(Integer.parseInt(creditTaken));
		lw.setList(stdList);
		
		JAXBContext jc = JAXBContext.newInstance(lw.getClass());
		Marshaller marshaller = jc.createMarshaller(); 
		
	
		String path = new File(filename).getParent();		 // relative path 
		
		SchemaFactory s = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);	// Factory that creates Schema objects
		Schema sc = s.newSchema(new File(path+"/SIS.xsd"));
		marshaller.setSchema(sc);
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		
		StringWriter sw = new StringWriter();
		sw.write("\n");
		
		marshaller.marshal(lw, new StreamResult(sw));
		System.out.println(sw.toString()); // for debugging
		
//		FileWriter fc = new FileWriter(filename);
//		fc.write(sw.toString());
//		fc.close();
		
		//return XML
		return sw.toString();
	
	}
	
	public int getLWSize() {
		return lw.getListSize();
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		SIS sis = new SIS();
		System.out.println("SIS");
		System.out.println("students = " + sis.students);
		System.out.println("enrollment =" + sis.enrollment);
		System.out.println("nPrefix = " + sis.namePrefix);
		System.out.println("creditTaken =" + sis.creditTaken);
		System.out.println("LW size=" + sis.getLWSize());
		
		
	}
}
