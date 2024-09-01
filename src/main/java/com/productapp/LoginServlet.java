package com.productapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {

	public void init() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String userId = req.getParameter("userId");
		String password = req.getParameter("userpassword");

		System.out.println("userId  = " + userId);
		System.out.println("password  = " + password);

		// get the student data from the request object and insert in database

		Connection connection = null;
		PreparedStatement statement = null;

		ResultSet rs = null;

		try {

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "rkolakaleti",
					"Rasyakola@2008");

			statement = connection.prepareStatement("select count(*) from users where user_id=? and password=?");

			statement.setString(1, userId);
			statement.setString(2, password);

			rs = statement.executeQuery();
			rs.next();
			int count = Integer.parseInt(rs.getString(1));
			res.setContentType("text/html");
			PrintWriter pw = res.getWriter();
			if (count != 0) {

				HttpSession session = req.getSession();
				session.setAttribute("userId", userId);
				pw.print("<html><body>Login is success");
				pw.print("<br><br>");
				String path="/productapp/products";
				pw.print("<a href="+path+">Products</a>");
				pw.print("</body></html>");

			} else {

				pw.print("<html><body>Login is failed</body></html>");
			}

		} catch (Exception ex) {

			ex.printStackTrace();
		} finally {

			try {
				rs.close();
				statement.close();
				connection.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}
}
