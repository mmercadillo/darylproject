package daryl.arima.combinatoria;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class Imprimir {
	final String BASE_PATH = "C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\"; 
	public void execute(List<String> lista) {

		try {
	        
        	FileWriter fw = new FileWriter(BASE_PATH + "COMBINACIONES.csv", true);
        	PrintWriter pw = new PrintWriter(fw);

    		for (int i = 0; i < lista.size(); i++) {
    			System.out.println((i+1) + " - " + lista.get(i));
        	
    			pw.println(lista.get(i));
    			pw.flush();
    		}
	        pw.close();
        
        }catch (Exception e) {
			e.printStackTrace();
		}

	}
}
