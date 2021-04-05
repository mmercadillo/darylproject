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
import daryl.system.comun.enums.Estrategia;
import daryl.system.comun.enums.TipoRobot;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@ToString
public class ArimaConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private String robot;
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private Activo tipoActivo;
	
	@Getter @Setter
	private String estrategia;
	
	@Column(nullable = true)
	@Getter @Setter
	private String maCoefficients;
	
	@Column(nullable = true)
	@Getter @Setter
	private String arCoefficients;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer standarDeviation;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer integrationOrder;
	
	
	@Column(nullable = true)
	@Getter @Setter
	private Double shockExpectation;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double shockVariation;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double constant;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fModificacion;
	
	@Column(nullable = false)
	@Getter @Setter
	private String fecha;
	
	@Column(nullable = false)
	@Getter @Setter
	private String hora;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer inicio;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double resultado;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer indexA;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer indexB;

	
}
