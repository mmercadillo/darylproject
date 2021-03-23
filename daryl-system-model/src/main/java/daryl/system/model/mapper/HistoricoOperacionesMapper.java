package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.HistoricoOperaciones;
import daryl.system.model.dto.HistoricoOperacionesDto;

public class HistoricoOperacionesMapper {

	public static HistoricoOperacionesDto getDto(HistoricoOperaciones entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		HistoricoOperacionesDto dto = modelMapper.map(entity, HistoricoOperacionesDto.class);
	
		
		return dto;

	}

	
	
}
