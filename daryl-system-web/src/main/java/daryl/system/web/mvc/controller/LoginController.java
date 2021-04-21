package daryl.system.web.mvc.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import daryl.system.web.mvc.dto.CuentaUsuarioDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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


		String token = getJWTToken(cudto.getCuenta());
		cudto.setToken(token);		
		
		
		//Reenviamos al controlador de dashboard
		/*
		RequestDispatcher dis = request.getRequestDispatcher("/dashboard");
		try {
			dis.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    }
	
	private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("darylJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60000))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
	
}
