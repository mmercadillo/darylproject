package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.Prediccion;
import daryl.system.model.dto.PrediccionDto;

public class PrediccionMapper {

	public static PrediccionDto getDto(Prediccion entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		PrediccionDto dto = modelMapper.map(entity, PrediccionDto.class);
	
		
		return dto;

	}

	
	
}
