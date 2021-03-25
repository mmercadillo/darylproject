package daryl.system.model.historicos.dto;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class HistoricoDto {
	
	@Getter @Setter
	private Long id;

	@Getter @Setter
	private Long fechaHora;

	@Getter @Setter
	private String fecha;
	
	@Getter @Setter
	private String hora;
	
	@Getter @Setter
	private Double apertura;
	
	@Getter @Setter
	private Double maximo;
	
	@Getter @Setter
	private Double minimo;
	
	@Getter @Setter
	private Double cierre;
	
	@Getter @Setter
	private Double volumen;	
	
	@Getter @Setter
	private Timeframes timeframe;
	
	@Getter @Setter
	private Activo activo;

}
