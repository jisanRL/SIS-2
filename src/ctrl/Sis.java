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
	private String errorMessage = "";

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
		
		String reportButton = request.getParameter("report");		// button clicked
		String xmlButton = request.getParameter("xml");				// xml button
		String jsonButton = request.getParameter("json");			// json button

		if (reportButton != null || xmlButton != null) {
			String prefix = request.getParameter("prefix");
			String creditTaken = request.getParameter("creditTaken");
			
			// do changes from this line onwards, now make the listwrapper calss and the export method in model
			if (!emptyNullChecker(prefix) && !emptyNullChecker(creditTaken)) {
				try {
					// calls sisModel and gets the student
					sisModel = (SIS) getServletContext().getAttribute("sis");

					if (reportButton != null) { 									// if report button is pressed
						tr = sisModel.retrieveStudent(prefix, creditTaken);

						// go through the hashmap and retrieve data in the set for the StudentBean
						for (String studentBean : tr.keySet()) {
							res.add(tr.get(studentBean));
						}
						numberOfResults = tr.size(); // number of outputs
						request.setAttribute("numberOfResults", numberOfResults);

						// output as an array in form
						request.setAttribute("resultMap", res.toArray());
						request.getRequestDispatcher("/form.jspx").forward(request, response);

					} else if (xmlButton != null ) { // if generate xml button is pressed [&& (reportButton == null || jsonButton == null)]

						String exp = "export/" + request.getSession().getId() + ".xml"; // create the xml file
						String fName = this.getServletContext().getRealPath("/" + exp); // path to export folder

						System.out.println("fName = " + fName + "\n" +"exp = " + exp);
						
						sisModel.export(prefix, creditTaken); // exports the data
						request.setAttribute("link", exp);
						request.setAttribute("anchor", fName);
						request.getRequestDispatcher("/XMLRes.jspx").forward(request, response);
						
//						if (sisModel.getLWSize() == 0) {
//							setErrorMsg("XML can;t be generated with 0 entries, Please try again");
//							ep = 3;
//							request.setAttribute("errVal", ep);
//							String errMessage = errorMessage;
//							request.setAttribute("errXML", errMessage);
//							request.getRequestDispatcher("/form.jspx").forward(request, response);
//						} else {
//							request.setAttribute("link", exp);
//							request.setAttribute("anchor", fName);
//							request.getRequestDispatcher("/XMLRes.jspx").forward(request, response);
//						}
//						request.getRequestDispatcher("/XMLRes.jspx").forward(request, response);
					} else if (jsonButton != null ) {		// && (reportButton == null || xmlButton == null)
						request.getParameter("/jsonRes.jspx");
					}
				} catch (Exception e) {
					ep = 1;
					request.setAttribute("errorValue", ep);
//					String errorMessage = e.getMessage(); 
//					request.setAttribute("errMsg", errorMessage);
//					request.getRequestDispatcher("/form.jspx").forward(request, response);					
				} 
			} else if (prefix.isEmpty() || creditTaken.isEmpty()) { // if fields are empty
				ep = 1;
				request.setAttribute("errorValue", ep);
				request.getRequestDispatcher("/form.jspx").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("/form.jspx").forward(request, response);
		}
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
	
	public void setErrorMsg(String s) {
		this.errorMessage = s;
	}
	
	public String getErrorMsg() {
		return errorMessage;
	}

}
