package daryl.system.model.dto;

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


@ToString
public class HistoricoOperacionesDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private Long ticket;

	@Getter @Setter
	private Double cierre;
	
	@Getter @Setter
	private Double profit;
	
	@Getter @Setter
	private Double apertura;
	
	@Getter @Setter
	private Double swap;
	
	@Getter @Setter
	private Double comision;
	
	@Getter @Setter
	private String fcierre;
	
	@Getter @Setter
	private String fapertura;
	
	@Getter @Setter
	private String comentario;
	
	
}
