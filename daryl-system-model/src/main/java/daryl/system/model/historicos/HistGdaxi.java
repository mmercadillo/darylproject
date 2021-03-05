package daryl.system.model.historicos;

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
public class HistGdaxi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	
	@Column(nullable = false)
	@Getter @Setter
	private Long fechaHora;
	
	@Column(nullable = false)
	@Getter @Setter
	private String fecha;
	
	@Column(nullable = false)
	@Getter @Setter
	private String hora;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double apertura;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double maximo;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double minimo;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double cierre;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double volumen;	
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private Timeframes timeframe;
	


}
