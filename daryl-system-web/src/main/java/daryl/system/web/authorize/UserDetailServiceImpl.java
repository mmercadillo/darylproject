package daryl.system.web.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import daryl.system.model.Usuario;
import daryl.system.web.repository.IUserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	

    @Autowired
    IUserRepository userRepository;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
    	//Buscar el usuario con el repositorio y si no existe lanzar una exepcion
    	Usuario usuarioFormBds = userRepository.findByUsername(username);
    	
		//Habría que mapear el usuario de la base de datos al tipo de usuairo que ncesita spring security
    	if(usuarioFormBds != null) {
    		//Mapeamos el Usuario de la BD,s a un Objeto de tipo UserDetails que SpringSecurity necesita    		
    		User user = User.getUser(usuarioFormBds);
	    	return user;
    	}else {
    		throw new UsernameNotFoundException("No existe usuario");
    	}
        
    }
    
	
}