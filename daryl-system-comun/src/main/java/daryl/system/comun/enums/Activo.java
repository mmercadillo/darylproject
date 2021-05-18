package daryl.system.comun.enums;

import lombok.Getter;

public enum Activo {
	XAUUSD(1), AUDCAD(10000), GDAXI(1), NDX(1), EURUSD(10000) ,XTIUSD(1);
	
	@Getter public final int multiplicador;
	
	Activo(int multiplicador) {
		this.multiplicador = multiplicador;
	}
	
}
