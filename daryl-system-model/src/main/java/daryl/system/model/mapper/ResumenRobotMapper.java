package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.ResumenRobot;
import daryl.system.model.dto.ResumenRobotDto;

public class ResumenRobotMapper {

	public static ResumenRobotDto getDto(ResumenRobot entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		ResumenRobotDto dto = modelMapper.map(entity, ResumenRobotDto.class);
	
		
		return dto;

	}

	
	
}
