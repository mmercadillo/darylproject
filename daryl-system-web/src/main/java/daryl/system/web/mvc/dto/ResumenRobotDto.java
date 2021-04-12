package daryl.system.web.mvc.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import daryl.system.comun.enums.Activo;
import daryl.system.model.ResumenRobot;
import lombok.Getter;
import lombok.Setter;

public class ResumenRobotDto implements Comparable<ResumenRobotDto>{

	@Getter @Setter
	private Long id;
	@Getter @Setter
	private String robot;
	@Getter @Setter
	private Activo tipoActivo;
	@Getter @Setter
	private String estrategia;
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
	
	public static ResumenRobotDto getDto(ResumenRobot resumen) {
		
		ResumenRobotDto resumenDto = new ResumenRobotDto();
			try{resumenDto.setEstrategia(resumen.getEstrategia());}catch (Exception e) {}
			resumenDto.setFAlta(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(resumen.getFAlta())));
			resumenDto.setFModificacion(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(resumen.getFModificacion())));
			resumenDto.setId(resumen.getId());
			resumenDto.setNumOperaciones(resumen.getNumOperaciones());
			resumenDto.setNumOpsGanadoras(resumen.getNumOpsGanadoras());
			resumenDto.setNumOpsPerdedoras(resumen.getNumOpsPerdedoras());
			resumenDto.setRobot(resumen.getRobot());
			resumenDto.setTipoActivo(resumen.getTipoActivo());
			resumenDto.setTotal(Math.round(resumen.getTotal()));
			resumenDto.setTotalGanancias(Math.round(resumen.getTotalGanancias()));
			resumenDto.setTotalPerdidas(Math.round(resumen.getTotalPerdidas()));
			resumenDto.setVersion(resumen.getVersion());
			/*resumenDto.setMes(resumen.getMes());
			resumenDto.setSemana(resumen.getSemana());
			resumenDto.setAnyo(resumen.getAnyo());
			resumenDto.setDia(resumen.getDia());
			resumenDto.setTotalSemana(Math.round(resumen.getTotalSemana()));
			resumenDto.setTotalMes(Math.round(resumen.getTotalMes()));
			resumenDto.setTotalAnyo(Math.round(resumen.getTotalAnyo()));
			resumenDto.setTotalDia(Math.round(resumen.getTotalDia()));*/
			
			resumenDto.setPctOpsGanadoras(resumen.getPctOpsGanadoras());
			resumenDto.setPctOpsPerdedoras(resumen.getPctOpsPerdedoras());
			resumenDto.setGananciaMediaPorOpGanadora(resumen.getGananciaMediaPorOpGanadora());
			resumenDto.setPerdidaMediaPorOpPerdedora(resumen.getPerdidaMediaPorOpPerdedora());
			
			if(Math.round(resumen.getTotal()) >= 15) resumenDto.setType(1);
			else resumenDto.setType(2);
			
		return resumenDto;
		
	}


	public int compareTo(ResumenRobotDto dto) {
		// TODO Auto-generated method stub''
		int i = this.getTotal() > dto.getTotal()?-1:1;
		return i;
	}
}