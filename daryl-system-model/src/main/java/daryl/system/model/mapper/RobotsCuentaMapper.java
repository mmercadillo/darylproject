package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.RobotsCuenta;
import daryl.system.model.dto.RobotsCuentaDto;

public class RobotsCuentaMapper {

	public static RobotsCuentaDto getDto(RobotsCuenta entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		RobotsCuentaDto dto = modelMapper.map(entity, RobotsCuentaDto.class);
	
		
		return dto;

	}

	
	
}
