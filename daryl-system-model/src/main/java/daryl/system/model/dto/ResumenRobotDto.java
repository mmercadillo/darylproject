package daryl.system.model.dto;


import java.io.Serializable;

import daryl.system.comun.enums.Activo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class ResumenRobotDto implements Serializable{
	
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
	private String estrategia;
	
	@Getter @Setter
	private Long fAlta;

	@Getter @Setter
	private Long fModificacion;
	
	@Getter @Setter
	private String fUltimoCierre;

	@Getter @Setter
	private String version;	

	@Getter @Setter
	private Long ultimoTicket;
	
	@Getter @Setter
	private Long numOperaciones = 0L;
	
	@Getter @Setter
	private Double totalPerdidas = 0.0;
	
	@Getter @Setter
	private Double totalGanancias = 0.0;
	
	@Getter @Setter
	private Double total = 0.0;
	
	@Getter @Setter
	private Long numOpsGanadoras = 0L;
	
	@Getter @Setter
	private Long numOpsPerdedoras = 0L;
	
	@Getter @Setter
	private Double pctOpsGanadoras = 0.0;
	@Getter @Setter
	private Double pctOpsPerdedoras = 0.0;
	@Getter @Setter
	private Double gananciaMediaPorOpGanadora = 0.0;
	@Getter @Setter
	private Double perdidaMediaPorOpPerdedora = 0.0;
}
