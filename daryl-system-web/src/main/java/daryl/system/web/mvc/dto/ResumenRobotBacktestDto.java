package daryl.system.web.mvc.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;

import daryl.system.comun.enums.Activo;
import daryl.system.model.ResumenRobot;
import daryl.system.model.backtest.ResumenRobotBacktest;
import lombok.Getter;
import lombok.Setter;

public class ResumenRobotBacktestDto implements Comparable<ResumenRobotBacktestDto>{

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
	private Double total = 0.0;
	@Getter @Setter
	private Long numOpsGanadoras = 0L;
	@Getter @Setter
	private Long numOpsPerdedoras = 0L;
	@Getter @Setter
	private Double maximaPerdidaConsecutiva = 0.0;
	@Getter @Setter
	private Double maximo = 0.0;
	@Getter @Setter
	private Double minimo = 0.0;
	@Getter @Setter
	private Double difMaxMin = 0.0;
	
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

	@Getter @Setter
	private String fprimeraOpTxt;
	@Getter @Setter
	private String fultimaOpTxt;

	
	public static ResumenRobotBacktestDto getDto(ResumenRobotBacktest resumen) {
		
		ResumenRobotBacktestDto resumenDto = new ResumenRobotBacktestDto();
			try{resumenDto.setEstrategia(resumen.getEstrategia());}catch (Exception e) {}
			try{resumenDto.setFAlta(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(resumen.getFAlta())));}catch (Exception e) {}
			try{resumenDto.setFModificacion(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(resumen.getFModificacion())));}catch (Exception e) {}
			
			try{resumenDto.setFprimeraOpTxt(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(resumen.getFprimeraOp())));}catch (Exception e) {}
			try{resumenDto.setFultimaOpTxt(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(resumen.getFultimaOp())));}catch (Exception e) {}
			
			try{resumenDto.setId(resumen.getId());}catch (Exception e) {}
			try{resumenDto.setNumOperaciones(resumen.getNumOperaciones());}catch (Exception e) {}
			try{resumenDto.setNumOpsGanadoras(resumen.getNumOpsGanadoras());}catch (Exception e) {}
			try{resumenDto.setNumOpsPerdedoras(resumen.getNumOpsPerdedoras());}catch (Exception e) {}
			try{resumenDto.setRobot(resumen.getRobot());}catch (Exception e) {}
			try{resumenDto.setTipoActivo(resumen.getTipoActivo());}catch (Exception e) {}
			try{resumenDto.setTotal(resumen.getTotal());}catch (Exception e) {}
			try{resumenDto.setTotalGanancias(Math.round(resumen.getTotalGanancias()));}catch (Exception e) {}
			try{resumenDto.setTotalPerdidas(Math.round(resumen.getTotalPerdidas()));}catch (Exception e) {}
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
			try{resumenDto.setMaximo(resumen.getMaximo());}catch (Exception e) {}
			try{resumenDto.setMinimo(resumen.getMinimo());}catch (Exception e) {}
			try{resumenDto.setDifMaxMin(resumen.getDifMaxMin());}catch (Exception e) {}
			
		return resumenDto;
		
	}


	public int compareTo(ResumenRobotBacktestDto dto) {
		// TODO Auto-generated method stub''
		int i = this.getTotal() > dto.getTotal()?-1:1;
		return i;
	}
}