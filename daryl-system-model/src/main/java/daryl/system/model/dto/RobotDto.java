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
import daryl.system.comun.enums.CanalAmq;
import daryl.system.comun.enums.Timeframes;
import daryl.system.comun.enums.TipoRobot;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class RobotDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Long id;
	
	@Getter @Setter
	private Activo activo;

	@Getter @Setter
	private String robot;

	@Getter @Setter
	private String estrategia;
	
	@Getter @Setter
	private TipoRobot tipoRobot;
	
	@Getter @Setter
	private CanalAmq canal;

	@Getter @Setter
	private Timeframes timeframe;
	
	@Getter @Setter
	private Boolean robotActivo;
	
	@Getter @Setter
	private Boolean inverso;
	
	
}
