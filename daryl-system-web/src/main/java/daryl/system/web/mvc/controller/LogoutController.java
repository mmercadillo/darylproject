package daryl.system.web.mvc.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
 
	
	@GetMapping("/logout")
    public void init(HttpSession session, HttpServletRequest request, HttpServletResponse response) {

		//Reenviamos al controlador de dashboard
		RequestDispatcher dis = request.getRequestDispatcher("/");
		try {
			session.invalidate();
			dis.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	
}
