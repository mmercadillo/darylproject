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

import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.CanalAmq;
import daryl.system.comun.enums.Timeframes;
import daryl.system.comun.enums.TipoRobot;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table
@ToString
public class Robot implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private Activo activo;

	@Getter @Setter
	@Column(unique = true)
	private String robot;

	@Getter @Setter
	private String estrategia;
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private TipoRobot tipoRobot;
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private CanalAmq canal;

	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private Timeframes timeframe;
	
	@Getter @Setter
	private Boolean robotActivo;
	
	@Getter @Setter
	private Boolean inverso;
	
	@Getter @Setter
	@Column(nullable = true)
	private String arimaConfig;
	
	@Getter @Setter
	@Column(nullable = true)
	private String varianceConfig;
	
	@Getter @Setter
	@Column(nullable = true)
	private String ficheroRna;	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	@Getter @Setter
	private Mode mode;
	
	@Getter @Setter
	@Column(nullable = true)
	private Integer neuronasEntrada;
}
