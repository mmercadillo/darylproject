package daryl.system.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import daryl.system.comun.enums.Activo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@ToString
public class ResumenRobotDemolab implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private String robot;

	
	@Column(nullable = true)
	@Getter @Setter
	private Long fAlta;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fModificacion;
	
	@Column(nullable = true)
	@Getter @Setter
	private String fUltimoCierre;
	
	@Column(nullable = true)
	@Getter @Setter
	private String version;	
	
	@Column(nullable = true)
	@Getter @Setter
	private Long ultimoTicket;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long numOperaciones = 0L;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double totalPerdidas = 0.0;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double totalGanancias = 0.0;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double total = 0.0;
	

	@Column(nullable = true)
	@Getter @Setter
	private Double maximaPerdidaConsecutiva = 0.0;
	

	@Column(nullable = true)
	@Getter @Setter
	private Double maximo = 0.0;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double minimo = 0.0;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double difMaxMin = 0.0;

	@Column(nullable = true)
	@Getter @Setter
	private Double totalAnterior = 0.0;
	
	
	@Column(nullable = true)
	@Getter @Setter
	private Long numOpsGanadoras = 0L;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long numOpsPerdedoras = 0L;
	
	@Getter @Setter
	private Double pctOpsGanadoras = 0.0;
	@Getter @Setter
	private Double pctOpsPerdedoras = 0.0;
	@Getter @Setter
	private Double gananciaMediaPorOpGanadora = 0.0;
	@Getter @Setter
	private Double perdidaMediaPorOpPerdedora = 0.0;
	
	//Esperanza matemática
	@Column(nullable = true)
	@Getter @Setter
	private Double espmat = 0.0;

	@Column(nullable = true)
	@Getter @Setter
	private Double espmatAnterior = 0.0;
	
	//Z-score
	@Column(nullable = true)
	@Getter @Setter
	private Double zScore = 0.0;
	
}
