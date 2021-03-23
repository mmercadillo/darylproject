package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.RobotsDarwin;
import daryl.system.model.dto.RobotsDarwinDto;

public class RobotsDarwinMapper {

	public static RobotsDarwinDto getDto(RobotsDarwin entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		RobotsDarwinDto dto = modelMapper.map(entity, RobotsDarwinDto.class);
	
		
		return dto;

	}

	
	
}
