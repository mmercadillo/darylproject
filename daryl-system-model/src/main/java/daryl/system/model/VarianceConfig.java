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
public class VarianceConfig implements Serializable{
	
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
	
	@Column
	@Getter @Setter
	private Integer n;
	
	@Column
	@Getter @Setter
	private Integer offset;
	
	
	@Column
	@Getter @Setter
	private Integer m;
	
	@Column
	@Getter @Setter
	private Double alpha;
	
	@Column
	@Getter @Setter
	private Double beta;
	
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fModificacion;
	
	@Column(nullable = false)
	@Getter @Setter
	private String fecha;
	
	@Column(nullable = false)
	@Getter @Setter
	private String hora;
	
	@Column(nullable = true)
	@Getter @Setter
	private Double resultado;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer lastN;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer lastOffset;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer lastM;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer lastAlpha;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer lastBeta;
	
}
