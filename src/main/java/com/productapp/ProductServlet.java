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

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

	public void init() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("userId");
		System.out.println("userId  " + userId);
		Connection connection = null;
		PreparedStatement statement = null;

		ResultSet rs = null;
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		pw.print("<html><body>");

		if (userId != null) {

			try {

				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/productdb", "rkolakaleti",
						"Rasyakola@2008");

				statement = connection.prepareStatement("Select * from products");

				rs = statement.executeQuery();
				
				pw.print("<h2>Product Details</h2>");
				pw.print("<table>");
				pw.print("<th>ProductID</th> <th>ProductName</th> <th>Price</th>");

				while (rs.next()) {
					pw.print("<tr>");
					String productId = rs.getString("ProductId");

					pw.print("<td>" + productId + "</td>");
					String productName = rs.getString("ProductName");
					pw.print("<td>" + productName + "</td>");
					String price = rs.getString("Price");
					pw.print("<td>" + price + "</td>");

					pw.print("</tr>");

				}
				pw.print("</table>");
				

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
		}else {
			
			String path = "/productapp/login.html";
			pw.print("<a href="+path+">Login</a>");
		}
		pw.print("</body></html>");
	}
}
