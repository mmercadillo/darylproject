package daryl.variance;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 * Created by Hon on 2/27/2015.
 */
public class StockPredict {

    private int n;
    private int offset;
    private int M = 5;
    private double alpha = 0.005;
    private double beta = 11.1;
    private List<Double> prices = new ArrayList<Double>();;

    public StockPredict(List<Double> datosForecast, int offset, int n, double alpha, double beta, int M){
        
        this.n = n;
        //this.alpha = alpha;
        //this.beta = beta;
        //this.M = M;
        this.offset = offset;

        for(int i = 0; i < this.n; i++) {
        	this.prices.add(datosForecast.get(datosForecast.size()- this.n - this.offset) + i);
        }
        //System.out.println("Prices: " + this.prices);
        
    }
    
    public double[] getPriceVariance() throws Exception{

        List<Double> x = new ArrayList<Double>();
        double[][] a = new double[this.M + 1][1];
        double[][] b = new double[1][this.M + 1];
        
        double lt[][] = new double[this.M + 1][1];
        double predictprice[][];

        /*---------------initialize the training data---------------*/
        for(int i = 0; i <= this.n; ++i){
            x.add((double)i + 1);
        }
        /*--------------calculate SUM-φ(xn)-------------------*/
        for(int i = 0; i < this.n; i++){
            for(int j = 0; j <= this.M; j++){
                a[j][0] += Math.pow(x.get(i), j);
            }
        }

        Matrix A = new Matrix(a);
        /*-----------------initialize φ(x)T------------------*/
        for(int i = 0; i <= this.M; i++){
        	b[0][i] = Math.pow(x.get(this.n), i);
        }

        Matrix B = new Matrix(b);
        /*------------calculate the matrix S-------------*/
        Matrix S = A.times(B).times(this.beta);
        double[][] s = S.getArray();
        for(int i = 0; i <= this.M; i++){
            for(int j = 0; j <= this.M; j++){
                if(i == j){
                    s[i][j] += this.alpha; // + Alpha * I
                }
            }
        }

        /*------------calculate the inversion of matrix S-------------*/

        S = S.inverse();
        /*-------------last two parts multiply------------*/
        for(int i = 0; i < this.n; i++){
            for(int j = 0; j <= this.M; j++){
                lt[j][0] += Math.pow(x.get(i), j) * this.prices.get(i);
            }
        }

        Matrix LT = new Matrix(lt);
        /*-------------first two parts matrix multiply------------*/
        Matrix FT = B.times(S);
        /*----------combine together--------------*/
        Matrix PP = FT.times(LT).times(this.beta);
        predictprice = PP.getArray();

        double variance = 1/this.beta + B.times(S).times(B.transpose()).get(0, 0);
        variance = Math.sqrt(variance);

        double[] priceVariance = {predictprice[0][0], variance};

        return priceVariance;
    }    
 

}