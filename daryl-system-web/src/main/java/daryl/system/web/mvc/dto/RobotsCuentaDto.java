package daryl.system.web.mvc.dto;

import java.util.List;

import javax.persistence.Column;

import daryl.system.model.RobotsCuenta;
import lombok.Getter;
import lombok.Setter;

public class RobotsCuentaDto{


	@Getter @Setter
	private String cuenta;
	@Getter @Setter
	private List<String> robots;
	
}
