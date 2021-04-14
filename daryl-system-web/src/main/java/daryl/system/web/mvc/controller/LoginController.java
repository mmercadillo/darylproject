package daryl.system.web.mvc.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.web.mvc.dto.CuentaUsuarioDto;
import daryl.system.web.mvc.dto.RobotsCuentaDto;

@RestController
public class LoginController {
 
	
	@GetMapping("/")
    public ModelAndView init(Model model) {

		CuentaUsuarioDto cudto = new CuentaUsuarioDto();
		ModelAndView view = new ModelAndView("login", "cuenta_usuario", cudto);
		
        return view;
    }
    
	
	@PostMapping("/")
    public void login(@ModelAttribute CuentaUsuarioDto cudto, HttpServletRequest request, HttpServletResponse response) {

		//Reenviamos al controlador de dashboard
		RequestDispatcher dis = request.getRequestDispatcher("/dashboard");
		try {
			dis.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
