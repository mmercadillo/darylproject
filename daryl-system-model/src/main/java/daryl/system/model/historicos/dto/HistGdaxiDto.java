package daryl.system.model.historicos.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import daryl.system.comun.enums.Timeframes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@ToString
public class HistGdaxiDto {
	

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
	


}
