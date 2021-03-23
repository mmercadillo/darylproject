package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.historicos.HistGdaxi;
import daryl.system.model.historicos.dto.HistGdaxiDto;

public class HistGdaxiMapper {

	public static HistGdaxiDto getDto(HistGdaxi entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		HistGdaxiDto dto = modelMapper.map(entity, HistGdaxiDto.class);
	
		
		return dto;

	}

	
	
}
