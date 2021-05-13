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
import daryl.system.comun.enums.TipoOrden;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@ToString
public class OperacionBacktest implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private TipoOrden tipo;

	@Column(nullable = false)
	@Getter @Setter
	private Double cierre;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double profit;
	
	@Column(nullable = false)
	@Getter @Setter
	private Double apertura;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double swap;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double comision;
	
	@Column(nullable = false)
	@Getter @Setter
	private String fcierreTxt;
	
	@Column(nullable = false)
	@Getter @Setter
	private String faperturaTxt;
	
	@Column(nullable = false)
	@Getter @Setter
	private String robot;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fcierre;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fapertura;
	
	
}
