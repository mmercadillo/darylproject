package daryl.system.robots.client.web.mvc.dto;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Estrategia;
import lombok.Getter;
import lombok.Setter;

public class ResumenRobotDto {

	@Getter @Setter
	private Long id;
	@Getter @Setter
	private String robot;
	@Getter @Setter
	private Activo tipoActivo;
	@Getter @Setter
	private Estrategia estrategia;
	@Getter @Setter
	private String fAlta;
	@Getter @Setter
	private String fModificacion;
	@Getter @Setter
	private String version;	
	@Getter @Setter
	private Long ultimoTicket;
	@Getter @Setter
	private Long numOperaciones = 0L;
	@Getter @Setter
	private Long totalPerdidas = 0L;
	@Getter @Setter
	private Long totalGanancias = 0L;
	@Getter @Setter
	private Long total = 0L;
	@Getter @Setter
	private Long numOpsGanadoras = 0L;
	@Getter @Setter
	private Long numOpsPerdedoras = 0L;

	
	private Integer status = 6;
	@Getter @Setter
	private Integer type = 1;
	private String[] actions = null;
	
	@Getter @Setter
	private Double pctOpsGanadoras;
	@Getter @Setter
	private Double pctOpsPerdedoras;
	@Getter @Setter
	private Double gananciaMediaPorOpGanadora;
	@Getter @Setter
	private Double perdidaMediaPorOpPerdedora;
	

}