package DAO;

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

/*
 * retrieves data from enrollment table
 */
public class EnrollmentDAO {
	
	private DataSource ds;
	
	// ctr
	public EnrollmentDAO() throws ClassNotFoundException {
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	//method
	public Map<String, bean.EnrollmentBean> retrieve(String cidPrefix, int credit_taken) throws SQLException {
		String query = "select * from enrollment where cid like '%" + cidPrefix + "%' and credit_taken >= "
				+ credit_taken;
		Map<String, EnrollmentBean> rv = new HashMap<String, bean.EnrollmentBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();

		while (r.next()) {
			String name = r.getString("GIVENNAME") + ", " + r.getString("SURNAME");
			String sid = r.getString("CID");
			EnrollmentBean eb = new EnrollmentBean();		// to get enrollment bean info
			rv.put(sid, eb);
			
			System.out.println("name" + name);
			System.out.println("sid" + sid);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}

}
