package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.Darwin;
import daryl.system.model.dto.DarwinDto;

public class DarwinMapper {

	public static DarwinDto getDto(Darwin entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		DarwinDto dto = modelMapper.map(entity, DarwinDto.class);
	
		
		return dto;

	}

	
	
}
