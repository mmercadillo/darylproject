package daryl.system.comun.dataset;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode(of = {"fecha", "hora"})
public class Datos {

	@Getter private String fecha;
	@Getter private String hora;
	@Getter private Double apertura;
	@Getter private Double minimo;
	@Getter private Double maximo;
	@Getter private Double cierre;
	@Getter private Double volumen;

	
	public static List<Double> getListaCierres(List<Datos> datos){
		
		List<Double> cierres = new ArrayList<Double>();
		
		for (Datos dato : datos) {
			cierres.add(dato.getCierre());
		}
		
		return cierres;
		
	}

	
	
	
}
