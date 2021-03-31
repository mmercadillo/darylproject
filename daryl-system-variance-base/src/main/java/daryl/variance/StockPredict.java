package daryl.variance;
import java.util.List;

import Jama.Matrix;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.enums.Activo;

/**
 * Created by Hon on 2/27/2015.
 */
public class StockPredict {

    private Activo activo;
    private int offset;
    //define the parameters for Bayesian Curve Fitting
    private int n;
    private int M = 5;
    private double alpha = 0.005;
    private double beta = 11.1;
    private double t[][];
    private double actualPrice;
    private String actualDate;

    public String[] date = new String[n];

    public StockPredict(List<Datos> datosForecast, Activo activo, int offset, int n, double alpha, double beta){
        this.activo = activo;
        this.offset = offset;
        ReadCSV readData = new ReadCSV(n, activo, offset, datosForecast);
        t = readData.readPrice();
        date = readData.readDate();
        actualPrice = readData.readActualPrice();
        actualDate = readData.readActualDate();
        this.n = n;
        this.alpha = alpha;
        this.beta = beta;
    }

    public double[] getPriceVariance() {

        int i,j;
        double x[] = new double[n + 1];
        double a[][] = new double[M + 1][1];
        double b[][] = new double[1][M + 1];
        double s[][];
        double lt[][] = new double[M + 1][1];
        double predictprice[][];


        /*---------------initialize the training data---------------*/

        for(i = 0; i <= n; ++i)
        {
            x[i] = i + 1;
        }


        /*--------------calculate SUM-φ(xn)-------------------*/

        for(i = 0; i < n; i++)
        {
            for(j = 0; j <= M; j++)
            {
                a[j][0] += Math.pow(x[i], j);
            }
        }


        Matrix A = new Matrix(a);

        /*-----------------initialize φ(x)T------------------*/

        for(i = 0; i <= M; i++)
        {
            b[0][i] = Math.pow(x[n], i);
        }


        Matrix B = new Matrix(b);

        /*------------calculate the matrix S-------------*/

        Matrix S = A.times(B).times(beta);
        s = S.getArray();

        for(i = 0; i <= M; i++)
        {
            for(j = 0; j <= M; j++)
            {
                if(i == j)
                {
                    s[i][j] += alpha; // + Alpha * I
                }
            }
        }

        /*------------calculate the inversion of matrix S-------------*/

        S = S.inverse();


        /*-------------last two parts multiply------------*/

        for(i = 0; i < n; i++)
        {
            for(j = 0; j <= M; j++)
            {
                lt[j][0] += Math.pow(x[i], j) * t[i][0];
            }
        }

        Matrix LT = new Matrix(lt);

        /*-------------first two parts matrix multiply------------*/

        Matrix FT = B.times(S);

        /*----------combine together--------------*/

        Matrix PP = FT.times(LT).times(beta);
        predictprice = PP.getArray();

        double variance = 1/beta + B.times(S).times(B.transpose()).get(0, 0);
        variance = Math.sqrt(variance);

        double[] priceVariance = {predictprice[0][0], variance};

        return priceVariance;
    }

    public String getActualDate(){

        return actualDate;
    }

    public double getActualPrice(){

        return actualPrice;
    }
}