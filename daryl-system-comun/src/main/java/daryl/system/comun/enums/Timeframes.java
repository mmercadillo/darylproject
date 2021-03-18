package daryl.system.comun.enums;

import lombok.Getter;

public enum Timeframes {
	PERIOD_M1(1), PERIOD_M5(5), PERIOD_M15(15), PERIOD_M30(30), PERIOD_H1(60), PERIOD_H4(240), PERIOD_D1(1440), PERIOD_W1(10080);

	@Getter public final int valor;
	
	Timeframes(int valor) {
		this.valor = valor;
	}
	
	public static Enum<Timeframes> getName(int valor){
		
		for(Timeframes tf : Timeframes.values()) {
			if(tf.getValor() == valor) return tf;
		}
		throw new IllegalArgumentException("Timeframe no válido");
		
	}
	
	public static Timeframes getTimeframe(Integer valor) {
		
		if(valor == 1) return Timeframes.PERIOD_M1;
		if(valor == 5) return Timeframes.PERIOD_M5;
		if(valor == 15) return Timeframes.PERIOD_M15;
		if(valor == 30) return Timeframes.PERIOD_M30;
		if(valor == 60) return Timeframes.PERIOD_H1;
		if(valor == 240) return Timeframes.PERIOD_H4;
		if(valor == 1440) return Timeframes.PERIOD_D1;
		if(valor == 10080) return Timeframes.PERIOD_W1;
		return  null;
		
	}
	
}
