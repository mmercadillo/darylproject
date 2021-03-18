package daryl.system.control.contizaciones.zeromq.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class Cotizacion {

	private Cotizacion() {
	}
	
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
	
	@Getter @Setter
	private Timeframes timeframe;
	
	@Getter @Setter
	private Activo activo;
	
	public static Cotizacion getCotizacionFromZeroMQ(String txtRecibido) {
		
		String[] datosRecibidos = txtRecibido.split(",");

		
		String activoRecibido = datosRecibidos[0];
		String tfRecibido = datosRecibidos[1];
		String fechaRecibida = datosRecibidos[2];
		String horaRecibida = datosRecibidos[3];
		String aperturaRecibida = datosRecibidos[4];
		String maximoRecibido = datosRecibidos[5];
		String minimoRecibido = datosRecibidos[6];
		String cierreRecibido = datosRecibidos[7];
		String volumenRecibido = datosRecibidos[8];
		
		
		Cotizacion ctzcn = new Cotizacion();
			ctzcn.setActivo(Activo.valueOf(activoRecibido));
			ctzcn.setApertura(Double.parseDouble(aperturaRecibida));
			ctzcn.setCierre(Double.parseDouble(cierreRecibido));
			ctzcn.setFecha(fechaRecibida);
			ctzcn.setHora(horaRecibida);
			ctzcn.setMaximo(Double.parseDouble(maximoRecibido));
			ctzcn.setMinimo(Double.parseDouble(minimoRecibido));
			ctzcn.setTimeframe(Timeframes.getTimeframe(Integer.parseInt(tfRecibido)));
			ctzcn.setVolumen(Double.parseDouble(volumenRecibido));
			
		return ctzcn;
		
	}

}
