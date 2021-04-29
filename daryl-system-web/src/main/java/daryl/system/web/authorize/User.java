package daryl.system.web.authorize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import daryl.system.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements UserDetails{

	@Getter @Setter
	private String username;
	@Getter @Setter
	private String password;
	@Getter @Setter
	private Boolean enabled;
	@Getter @Setter
	private Boolean accountNonExpired;
	@Getter @Setter
	private Boolean accountNonLocked;
	@Getter @Setter
	private Boolean credentialsNonExpired;
	@Getter @Setter
	private List<GrantedAuthority> authorities;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return accountNonExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return accountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return credentialsNonExpired;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}
	
	public static User getUser(Usuario usuario) {
		
		User user = new User();
			user.setUsername(usuario.getUsername());
			user.setPassword(usuario.getPassword());
			user.setAccountNonExpired(usuario.getAccountNonExpired());
			user.setAccountNonLocked(usuario.getAccountNonLocked());
			user.setCredentialsNonExpired(usuario.getCredentialsNonExpired());
			user.setEnabled(usuario.getEnabled());
			
			String[] roles = usuario.getRoles().split(",");
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (String rol : roles) {
				authorities.add(new SimpleGrantedAuthority(rol));
			}
			user.setAuthorities(authorities);
		
		return user;
	}
	
}