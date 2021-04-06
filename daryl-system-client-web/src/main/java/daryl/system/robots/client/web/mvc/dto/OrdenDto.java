package daryl.system.robots.client.web.mvc.dto;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Estrategia;
import daryl.system.comun.enums.TipoOrden;
import lombok.Getter;
import lombok.Setter;

public class OrdenDto{

	@Getter @Setter
	private Long id;
	@Getter @Setter
	private TipoOrden tipoOrden;
	@Getter @Setter
	private Activo tipoActivo;
	@Getter @Setter
	private String estrategia;
	@Getter @Setter
	private String fAlta;
	@Getter @Setter
	private String fBaja;
	@Getter @Setter
	private String robot;
	@Getter @Setter
	private String fecha;
	@Getter @Setter
	private String hora;
	
	
}
