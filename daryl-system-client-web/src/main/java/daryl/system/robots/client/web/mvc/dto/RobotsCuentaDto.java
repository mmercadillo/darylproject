package daryl.system.robots.client.web.mvc.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import daryl.system.model.RobotsCuenta;
import lombok.Getter;
import lombok.Setter;

public class RobotsCuentaDto {

	@Getter @Setter
	private Long id;
	@Getter @Setter
	private String robot;
	@Getter @Setter
	private String fAlta;
	@Getter @Setter
	private String cuenta;
	
	
	public static RobotsCuentaDto getDto(RobotsCuenta robotCuenta) {
		
		RobotsCuentaDto robotCuentaDto = new RobotsCuentaDto();
			robotCuentaDto.setFAlta(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(robotCuenta.getFAlta())));
			robotCuentaDto.setId(robotCuenta.getId());
			robotCuentaDto.setRobot(robotCuenta.getRobot());
			robotCuentaDto.setCuenta(robotCuenta.getCuenta());
			
		return robotCuentaDto;
		
	}

	
}
