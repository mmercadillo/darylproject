package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.historicos.HistNdx;
import daryl.system.model.historicos.dto.HistNdxDto;

public class HistNdxMapper {

	public static HistNdxDto getDto(HistNdx entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		HistNdxDto dto = modelMapper.map(entity, HistNdxDto.class);
	
		
		return dto;

	}

	
	
}
