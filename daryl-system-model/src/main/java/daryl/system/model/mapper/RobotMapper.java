package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.Robot;
import daryl.system.model.dto.RobotDto;

public class RobotMapper {

	public static RobotDto getDto(Robot entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		RobotDto dto = modelMapper.map(entity, RobotDto.class);
	
		
		return dto;

	}

	
	
}
