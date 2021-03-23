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
import daryl.system.comun.enums.Estrategia;
import daryl.system.comun.enums.TipoRobot;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class ArimaConfigDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private String robot;
	
	@Getter @Setter
	private Activo tipoActivo;
	
	@Getter @Setter
	private Estrategia estrategia;
	
	@Getter @Setter
	private String maCoefficients;
	
	@Getter @Setter
	private String arCoefficients;
	
	@Getter @Setter
	private Integer standarDeviation;
	
	@Getter @Setter
	private Integer integrationOrder;

	@Getter @Setter
	private Double shockExpectation;
	
	@Getter @Setter
	private Double shockVariation;

	@Getter @Setter
	private Double constant;
	
	@Getter @Setter
	private Long fModificacion;
	
	@Getter @Setter
	private String fecha;
	
	@Getter @Setter
	private String hora;
	
	@Getter @Setter
	private Integer inicio;
	
	@Getter @Setter
	private Double resultado;
	
	@Getter @Setter
	private Integer indexA;
	
	@Getter @Setter
	private Integer indexB;
	
	
}
