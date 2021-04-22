package otro;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Hon on 2/28/2015.
 */
public class ReadCSV {
    private int N;
    private double[][] price;
    private String[] date;
    private String[][] info;
    private String symbol;
    private int offset;
    private int position;

    public ReadCSV(int N, String symbol, int offset, List<String> cotizs){
        this.N = N;
        price = new double[N][1];
        date = new String[N];
        this.symbol = symbol;
        this.offset = offset;

        info = new String[100000][2];
        try {
        	int count = 0;
        	for (String line : cotizs) {
				
        		String item[] = line.split(",");
        		info[count][0] = item[5];
                info[count][1] = item[0]+" "+item[1];
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
    
    public ReadCSV(int N, String symbol, int offset){
        this.N = N;
        price = new double[N][1];
        date = new String[N];
        this.symbol = symbol;
        this.offset = offset;

        //String fileName = "StockData/" + symbol + "_history.csv";
        String fileName = "StockData/" + symbol + ".csv";

        info = new String[41700][2];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader (new ReverseLineInputStream(new File(fileName))));
            String line = null;
            int count = 0;
            while((line = reader.readLine()) != null){
                String item[] = line.split(",");
                info[count][0] = item[5];
                info[count][1] = item[0]+" "+item[1];
//                System.out.printf("%s %s\n", info[count][0], info[count][1]);
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
