package daryl.system.comun.enums;

import lombok.Getter;

public enum Activo {
	XAUUSD(1), AUDCAD(10000), GDAXI(1), NDX(1), EURUSD(10000) ,XTIUSD(1), GBPJPY(1);
	
	@Getter public final int multiplicador;
	
	Activo(int multiplicador) {
		this.multiplicador = multiplicador;
	}
	
	public String getFicheroHistorico() {
		
		if(this == XAUUSD) return "XAUUSD";
		if(this == AUDCAD) return "AUDCAD";
		if(this == GDAXI) return "DEUIDXEUR";
		if(this == EURUSD) return "EURUSD";
		if(this == GBPJPY) return "GBPJPY";
		if(this == NDX) return "USATECHIDXUSD";
		if(this == XTIUSD) return "LIGHTCMDUSD";
		
		return  null;
		
	}
}
