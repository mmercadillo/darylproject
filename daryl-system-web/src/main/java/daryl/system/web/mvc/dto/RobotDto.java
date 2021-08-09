package daryl.system.web.mvc.dto;

import daryl.system.model.Robot;
import lombok.Getter;
import lombok.Setter;

public class RobotDto implements Comparable<RobotDto>{

	@Getter @Setter
	private String robot;
		
	public static RobotDto getDto(Robot robot) {
		
		RobotDto robotCuentaDto = new RobotDto();
		
		robotCuentaDto.setRobot(robot.getRobot());

		return robotCuentaDto;
	
	}
	
	@Override
	public int compareTo(RobotDto dto) {
		
		int i = this.getRobot().compareTo(dto.getRobot());
		return i;
	}
	
}
