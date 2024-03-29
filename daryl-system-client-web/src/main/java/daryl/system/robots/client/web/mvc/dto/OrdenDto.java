package daryl.system.robots.client.web.mvc.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import lombok.Getter;
import lombok.Setter;

public class OrdenDto{

	@Getter @Setter
	private Long id;
	@Getter @Setter
	private TipoOrden tipoOrden;
	@Getter @Setter
	private Activo tipoActivo;
	@Getter @Setter
	private String estrategia;
	@Getter @Setter
	private String fAlta;
	@Getter @Setter
	private String fBaja;
	@Getter @Setter
	private String robot;
	@Getter @Setter
	private String fecha;
	@Getter @Setter
	private String hora;
	@Getter @Setter
	private String labRobot;	
	
	public static OrdenDto getDto(Orden orden) {
		
		OrdenDto ordenDto = new OrdenDto();
		ordenDto.setTipoOrden(orden.getTipoOrden());
		ordenDto.setTipoActivo(orden.getTipoActivo());
		ordenDto.setEstrategia(orden.getEstrategia());
		ordenDto.setFAlta(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(orden.getFAlta())));
		try{ordenDto.setFBaja(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(orden.getFBaja())));}catch (Exception e) {}
		ordenDto.setRobot(orden.getRobot());
		ordenDto.setFecha(orden.getFecha());
		ordenDto.setHora(orden.getHora());
		ordenDto.setLabRobot(getLabComment(orden.getRobot()).trim());
		return ordenDto;
		
	}
	
	private static String getLabComment(String robot) {
		
		
		String labComment = robot	.replace("ARIMA", "ARI")
									.replace("XAUUSD", "XU")
									.replace("GDAXI", "GD")
									.replace("NDX", "ND")
									.replace("WTI", "WT")
									.replace("AUDCAD", "AC")
									.replace("EURUSD", "EU")
									.replace("VARIANCE", "VAR")
									;
		return labComment;
		
	}
	
	
}
