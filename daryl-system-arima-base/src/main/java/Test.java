import java.util.Arrays;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;

public class Test {

    public static void main(String[] args) {
        DefaultArimaProcess arimaProcess = new DefaultArimaProcess();
        arimaProcess.setMaCoefficients(-0.7);
        //arimaProcess.setStd(0);
        //arimaProcess.setConstant(0.0);
        arimaProcess.setIntegrationOrder(1);
        //arimaProcess.setShockExpectation(0.0);
       arimaProcess.setArCoefficients(10.0, 9.0);
        //arimaProcess.setShockVariation(1.0);
        
        
        double[] observations =
                new double[]{ 13811.699, 13805.257, 13863.289, 13949.279, 13954.779, 13979.769, 13958.279, 
                		13973.269, 13908.759, 13907.399, 13978.249, 13988.159, 13992.679, 14018.699, 14012.679, 
                		14011.269, 14032.799, 14050.949, 14033.089, 14028.099, 14049.699, 14062.559, 14067.079, 
                		14059.599, 14062.259, 14049.299, 13969.399, 13978.299};

        ArimaForecaster arimaForecaster = new DefaultArimaForecaster(arimaProcess, observations);
        double forecast = arimaForecaster.next();

        System.out.println("Forecast: " + forecast);
    }
}