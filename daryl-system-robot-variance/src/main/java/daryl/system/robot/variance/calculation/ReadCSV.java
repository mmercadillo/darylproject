package daryl.system.robot.variance.calculation;
import java.util.List;

import daryl.system.comun.dataset.Datos;
import daryl.system.comun.enums.Activo;

/**
 * Created by Hon on 2/28/2015.
 */
public class ReadCSV {
    private int N;
    private double[][] price;
    private String[] date;
    private String[][] info;
    private Activo activo;
    private int offset;
    private int position;

    public ReadCSV(int N, Activo activo, int offset, List<Datos> datosForecast){
        this.N = N;
        price = new double[N][1];
        date = new String[N];
        this.activo = activo;
        this.offset = offset;

        info = new String[41700][2];
        try {
        	int count = 0;
        	for (Datos dato : datosForecast) {
				
        		info[count][0] = String.valueOf(dato.getCierre());
                info[count][1] = dato.getFecha()+" "+dato.getHora();
                ++count;
			}

        } catch (Exception e) {
            e.printStackTrace();
        }

        
        for(int i = N - 1; i >= 0; i--){
            price[i][0] = Double.parseDouble(info[N - i + offset][0]);
            date[i] = info[N - i + offset][1];
        }
    }
    


    public double[][] readPrice() {

        return price;
    }

    public String[] readDate() {

        return date;
    }

    public String readActualDate() {

        return info[position + offset][1];
    }

    public Double readActualPrice() {

        return Double.parseDouble(info[position + offset][0]);
    }
}
