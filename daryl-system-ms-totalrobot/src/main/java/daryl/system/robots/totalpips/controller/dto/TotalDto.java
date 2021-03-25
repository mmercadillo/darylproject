package daryl.system.robots.totalpips.controller.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class TotalDto{

	@Getter @Setter
	private String robot;
	@Getter @Setter
	private String fecha;
	@Getter @Setter
	private Long total = 0L;

	
	
	public static TotalDto getDto(String robot, Long total) {
		
		TotalDto totalDto = new TotalDto();
			totalDto.setRobot(robot);
			totalDto.setFecha(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
			totalDto.setTotal(total);
			
			
		return totalDto;
		
	}

	
}
