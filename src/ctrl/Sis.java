package ctrl;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.EnrollmentDAO;
import DAO.StudentDAO;
import bean.StudentBean;
import model.SIS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class Test
 */
@WebServlet({ "/Sis", "/Sis/*", "/sis", "/sis/*" })
public class Sis extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SIS sisModel;
	private StudentDAO sd;
	private EnrollmentDAO ed;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Sis() {
		super();
	}

	public void init() throws ServletException {
		// ServletContext context = getServletContext();

		try {
			sisModel = new SIS();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		getServletContext().setAttribute("sis", sisModel);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<StudentBean> res = new ArrayList<StudentBean>();				
		Map<String, StudentBean> tr = new HashMap<String, StudentBean>();	
		int ep = 0;
		int numberOfResults = 0;
		
		// check if u clicked on Report
		String reportButton = request.getParameter("report");		// button clicked

		if (reportButton != null) {

			String prefix = request.getParameter("prefix");
			String creditTaken = request.getParameter("creditTaken");
			
			if (!emptyNullChecker(prefix) && !emptyNullChecker(creditTaken)) {
				try {
					// calls sisModel and gets the student
					sisModel = (SIS) getServletContext().getAttribute("sis");
					tr = sisModel.retrieveStudent(prefix, creditTaken);
					
					// go through the hashmap and retrieve data in the set for the StudentBean
					for (String studentBean : tr.keySet()) {
						res.add(tr.get(studentBean));
					}
					
					numberOfResults = tr.size();							// number of outputs 
					request.setAttribute("numberOfResults", numberOfResults);
					
					// output as an array in form
					request.setAttribute("resultMap", res.toArray());

				} catch (Exception e) { 
					ep = 1;
					request.setAttribute("errorValue", ep);
				}
			} else { // if fields are empty
				ep = 1;
				request.setAttribute("errorValue", ep);
			}
			System.out.println("name = " +  prefix);
			System.out.println("resultmap=" + res);
		}
		
	request.getRequestDispatcher("/form.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private boolean emptyNullChecker(String input) {
		if (input.isEmpty() || input == null) {
			return true;
		}
		return false;
	}

}
