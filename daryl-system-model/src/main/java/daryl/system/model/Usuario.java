package daryl.system.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@ToString
public class Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;

	@Column(nullable = false, unique = true)
	@Getter @Setter
	private String username;
	
	@Column(nullable = false)
	@Getter @Setter
	private String password;
	
	@Column(nullable = false)
	@Getter @Setter
	private Boolean enabled = Boolean.TRUE;
	
	@Column(nullable = false)
	@Getter @Setter
	private Boolean accountNonExpired = Boolean.TRUE;
	
	@Column(nullable = false)
	@Getter @Setter
	private Boolean accountNonLocked = Boolean.TRUE;
	
	@Column(nullable = false)
	@Getter @Setter
	private Boolean credentialsNonExpired = Boolean.TRUE;

	@Column(nullable = false)
	@Getter @Setter
	private String roles;
	
	@Column(nullable = false)
	@Getter @Setter
	private Long fAlta;
	

	
	
	
}
