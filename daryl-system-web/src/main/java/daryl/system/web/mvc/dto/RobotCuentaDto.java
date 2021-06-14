package daryl.system.web.mvc.dto;

import java.util.List;

import javax.persistence.Column;

import daryl.system.model.RobotsCuenta;
import lombok.Getter;
import lombok.Setter;

public class RobotCuentaDto{


	@Getter @Setter
	private String robot;
	
	@Getter @Setter
	private String cuenta;
		
	@Column(nullable = true)
	@Getter @Setter
	private Long fAlta;
	
	
	public static RobotCuentaDto getDto(RobotsCuenta robot) {
		
		RobotCuentaDto robotCuentaDto = new RobotCuentaDto();
		
		robotCuentaDto.setRobot(robot.getRobot());
		robotCuentaDto.setFAlta(robot.getFAlta());
		robotCuentaDto.setCuenta(robot.getCuenta());
		
		return robotCuentaDto;
	
	}
}
