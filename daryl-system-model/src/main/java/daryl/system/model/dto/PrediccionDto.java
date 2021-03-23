package daryl.system.model.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.TipoOrden;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class PrediccionDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Getter @Setter
	private TipoOrden tipoOrden;
	
	@Getter @Setter
	private Activo tipoActivo;
	
	@Getter @Setter
	private String estrategia;

	@Getter @Setter
	private Long fechaHora;
	
	@Getter @Setter
	private String fecha;
	
	@Getter @Setter
	private String hora;
	
	@Getter @Setter
	private Double cierre;
	
	@Getter @Setter
	private String robot;
	
}
