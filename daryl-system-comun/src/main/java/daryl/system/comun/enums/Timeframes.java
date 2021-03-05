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
}
