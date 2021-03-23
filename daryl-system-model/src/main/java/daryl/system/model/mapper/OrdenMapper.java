package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.Orden;
import daryl.system.model.dto.OrdenDto;

public class OrdenMapper {

	public static OrdenDto getDto(Orden entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		OrdenDto dto = modelMapper.map(entity, OrdenDto.class);
	
		
		return dto;

	}

	
	
}
