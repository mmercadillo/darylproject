package daryl.arima.combinatoria;


import java.util.Collections;
import java.util.List;

public class Ordenacion {

	private List<String> lista;

	public Ordenacion(List<String> list) {
		super();
		this.lista = list;
	}

	public void ordenar() {
		Collections.sort(lista);
		//imrpimir();
	}

	private void imrpimir() {
		for (int i = 0; i < this.lista.size(); i++) {
			System.out.println((i+1) + " - " + this.lista.get(i));
		}
	}

	public List<String> getLista() {
		return lista;
	}
	
}
