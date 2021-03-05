package daryl.arima.combinatoria;


public class Logica {

	public void start(String[] args) {
		Combinaciones com = new Combinaciones(args);
		com.combinar();

		Ordenacion ord = new Ordenacion(com.getCombinaciones());
		ord.ordenar();

		Imprimir imp = new Imprimir();
		imp.execute(ord.getLista());
	}
}
