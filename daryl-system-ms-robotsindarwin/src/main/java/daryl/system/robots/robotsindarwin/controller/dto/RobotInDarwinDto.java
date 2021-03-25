package daryl.system.robots.robotsindarwin.controller.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import daryl.system.comun.enums.Darwin;
import daryl.system.model.RobotsDarwin;
import lombok.Getter;
import lombok.Setter;

public class RobotInDarwinDto implements Comparable<RobotInDarwinDto>{

	@Getter @Setter
	private Long id;
	@Getter @Setter
	private String robot;
	@Getter @Setter
	private String darwin;
	@Getter @Setter
	private String fAlta;

	
	
	public static RobotInDarwinDto getDto(RobotsDarwin robotDawin) {
		
		RobotInDarwinDto robotDarwinDto = new RobotInDarwinDto();

			robotDarwinDto.setFAlta(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(robotDawin.getFAlta())));
			robotDarwinDto.setId(robotDawin.getId());
			robotDarwinDto.setRobot(robotDawin.getRobot());
			robotDarwinDto.setDarwin(robotDawin.getDarwin());
	
			
		return robotDarwinDto;
		
	}


	public int compareTo(RobotInDarwinDto dto) {
		return 1;
	}
	
}
