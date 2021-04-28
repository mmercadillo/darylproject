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
	@Getter @Setter
	private Double maximaPerdidaConsecutiva = 0.0;

	
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
	@Getter @Setter
	private Double espmat;
	
	
	public static ResumenRobotDto getDto(ResumenRobot resumen) {
		
		ResumenRobotDto resumenDto = new ResumenRobotDto();
			try{resumenDto.setEstrategia(resumen.getEstrategia());}catch (Exception e) {}
			try{resumenDto.setFAlta(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(resumen.getFAlta())));}catch (Exception e) {}
			try{resumenDto.setFModificacion(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(resumen.getFModificacion())));}catch (Exception e) {}
			try{resumenDto.setId(resumen.getId());}catch (Exception e) {}
			try{resumenDto.setNumOperaciones(resumen.getNumOperaciones());}catch (Exception e) {}
			try{resumenDto.setNumOpsGanadoras(resumen.getNumOpsGanadoras());}catch (Exception e) {}
			try{resumenDto.setNumOpsPerdedoras(resumen.getNumOpsPerdedoras());}catch (Exception e) {}
			try{resumenDto.setRobot(resumen.getRobot());}catch (Exception e) {}
			try{resumenDto.setTipoActivo(resumen.getTipoActivo());}catch (Exception e) {}
			try{resumenDto.setTotal(Math.round(resumen.getTotal()));}catch (Exception e) {}
			try{resumenDto.setTotalGanancias(Math.round(resumen.getTotalGanancias()));}catch (Exception e) {}
			try{resumenDto.setTotalPerdidas(Math.round(resumen.getTotalPerdidas()));}catch (Exception e) {}
			try{resumenDto.setVersion(resumen.getVersion());}catch (Exception e) {}
			/*resumenDto.setMes(resumen.getMes());}catch (Exception e) {}
			resumenDto.setSemana(resumen.getSemana());}catch (Exception e) {}
			resumenDto.setAnyo(resumen.getAnyo());}catch (Exception e) {}
			resumenDto.setDia(resumen.getDia());}catch (Exception e) {}
			resumenDto.setTotalSemana(Math.round(resumen.getTotalSemana()));}catch (Exception e) {}
			resumenDto.setTotalMes(Math.round(resumen.getTotalMes()));}catch (Exception e) {}
			resumenDto.setTotalAnyo(Math.round(resumen.getTotalAnyo()));}catch (Exception e) {}
			resumenDto.setTotalDia(Math.round(resumen.getTotalDia()));*/
			
			try{resumenDto.setPctOpsGanadoras(resumen.getPctOpsGanadoras());}catch (Exception e) {}
			try{resumenDto.setPctOpsPerdedoras(resumen.getPctOpsPerdedoras());}catch (Exception e) {}
			try{resumenDto.setGananciaMediaPorOpGanadora(resumen.getGananciaMediaPorOpGanadora());}catch (Exception e) {}
			try{resumenDto.setPerdidaMediaPorOpPerdedora(resumen.getPerdidaMediaPorOpPerdedora());}catch (Exception e) {}
			try{resumenDto.setEspmat(resumen.getEspmat());}catch (Exception e) {}
			
			try{
				if(Math.round(resumen.getTotal()) >= 15) resumenDto.setType(1);
				else resumenDto.setType(2);
			}catch (Exception e) {}
			
			try{resumenDto.setMaximaPerdidaConsecutiva(resumen.getMaximaPerdidaConsecutiva());}catch (Exception e) {}
			
		return resumenDto;
		
	}


	public int compareTo(ResumenRobotDto dto) {
		// TODO Auto-generated method stub''
		int i = this.getTotal() > dto.getTotal()?-1:1;
		return i;
	}
}