package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.ArimaConfig;
import daryl.system.model.dto.ArimaConfigDto;

public class ArimaConfigMapper {

	public static ArimaConfigDto getDto(ArimaConfig entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		ArimaConfigDto dto = modelMapper.map(entity, ArimaConfigDto.class);
	
		
		return dto;

	}

	
	
}
