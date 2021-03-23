package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.historicos.HistAudCad;
import daryl.system.model.historicos.dto.HistAudCadDto;

public class HistAudCadMapper {

	public static HistAudCadDto getDto(HistAudCad entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		HistAudCadDto dto = modelMapper.map(entity, HistAudCadDto.class);
	
		
		return dto;

	}

	
	
}
