package daryl.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import daryl.system.comun.enums.Activo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@ToString
public class AnnConfigCalcs implements Serializable{
	
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
	private String ficheroAnn;
	
	@Lob
    @Column(columnDefinition="BLOB")
	@Getter @Setter
    private byte[] ann;

	
	@Column(nullable = true)
	@Getter @Setter
	private Double resultado;

	
	@Column(nullable = true)
	@Getter @Setter
	private Double accuracy;
	
	@Column(nullable = true)
	@Getter @Setter
	private Long fModificacion;
	
	@Column(nullable = false)
	@Getter @Setter
	private String fecha;
	
	@Column(nullable = false)
	@Getter @Setter
	private String hora;
	
	////
	

	@Column(nullable = true)
	@Getter @Setter
	private Integer lastPasoLearnigRate;

	@Column(nullable = true)
	@Getter @Setter
	private Integer lastPasoMomentum;

	@Column(nullable = true)
	@Getter @Setter
	private Integer lastHiddenNeurons;
	
	@Column(nullable = true)
	@Getter @Setter
	private Integer lastTransferFunctionType;

	
	
}
