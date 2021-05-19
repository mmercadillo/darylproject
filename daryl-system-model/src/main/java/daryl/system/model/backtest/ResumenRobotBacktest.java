package daryl.system.model.backtest;


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
public class ResumenRobotBacktest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private String robot;
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private Activo tipoActivo;
	
	@Getter @Setter
	private String estrategia;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fAlta;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fModificacion;

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
	
	@Column(nullable = true)
	@Getter @Setter
	private String fprimeraOpTxt;
	
	@Column(nullable = true)
	@Getter @Setter
	private String fultimaOpTxt;

	
	@Column(nullable = true)
	@Getter @Setter
	private Long fprimeraOp;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fultimaOp;
	
}
