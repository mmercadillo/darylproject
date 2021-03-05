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
public class HistoricoOperaciones implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	
	@Column(nullable = false)
	@Getter @Setter
	private Long ticket;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double cierre;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double profit;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double apertura;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double swap;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double comision;
	
	@Column(nullable = false)
	@Getter @Setter
	private String fcierre;
	
	@Column(nullable = false)
	@Getter @Setter
	private String fapertura;
	
	@Column(nullable = false)
	@Getter @Setter
	private String comentario;
	
	
}
