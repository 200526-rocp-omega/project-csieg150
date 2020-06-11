package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.LoginController;
import controllers.UserController;

@SuppressWarnings("serial")
public class FrontController extends HttpServlet {
	private static final ObjectMapper om = new ObjectMapper(); // Helps convert JSON to a usable object.
	private static final UserController uc = new UserController();
	private static final LoginController lc = new LoginController();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
		throws ServletException, IOException{
			
		String requestUri = req.getRequestURI(); //Determine where the 'get' is coming from
		
		switch(requestUri) {
		case "/login":
			lc.doGet(req, rsp);
			break;
		case "/user":
			// code code code for user.doGet
			break;
		}
			
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException{

		String requestUri = req.getRequestURI(); //Determine where the 'get' is coming from

		switch(requestUri) {
		case "/login":
			lc.doPost(req, rsp, om);
			break;
		case "/user":
			// code code code for user.doGet
			break;
		}

	}
}
