package daryl.system.model;

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

@Entity
@Table
@ToString
public class Prediccion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private TipoOrden tipoOrden;
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private Activo tipoActivo;
	
	@Getter @Setter
	private String estrategia;

	@Column(nullable = false)
	@Getter @Setter
	private Long fechaHora;
	
	@Column(nullable = false)
	@Getter @Setter
	private String fecha;
	
	@Column(nullable = false)
	@Getter @Setter
	private String hora;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double cierre;
	
	@Getter @Setter
	private String robot;
	
}
