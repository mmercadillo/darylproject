
package daryl.ann;

import static java.lang.Math.round;

import java.io.Serializable;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;


public class ANN implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int counter = 0;    
    private double[] hidden;
    private double[][] OUT;
    private double[] error;
    private double[] delta;
    private double[][] WH;
    private double[][] WO;
    //Weights to hold delta values used for momentum in back propogation
    private double[][] WH_delta;
    private double[][] WO_delta;
    
    private double newDelta = 0;
    private int badEpoch = 5000;
    private boolean badFact = false;
    
    //default values, can be changed with public setters
    @Setter @Getter
    private double lrc;
    @Setter @Getter
    private double err = 0.2;
    @Setter @Getter
    private double momentum;
    @Setter @Getter
    private int hiddenNeurons;
    @Setter @Getter
    private double testing_ratio;
    @Setter @Getter
    private boolean details;
    @Setter @Getter
    private boolean modifyValues;
    @Setter @Getter
    private double modifyRate;
    @Setter @Getter
    private double convergenceLimit;
    @Setter @Getter
    private boolean useSigmoid;
    @Setter @Getter
    private boolean useTanh;
    
    @Setter @Getter
    private FischerTransform ft_ann;
    @Setter @Getter
    private MovingAverages ma;
    
    
    
    //***************************** Training **********************************           
    public double[][] train(double[][] input, double[][] target){
        
        if (input.length != target.length)
            System.out.println("Input and target must be of same length");
        else{

            hidden = new double[hiddenNeurons];
            OUT = new double[target.length][target[0].length];
            error = new double[target[0].length];

            //randomize weights
            WH = randomizeWeights(input[0].length,hiddenNeurons,-1,1);
            WO = randomizeWeights(hiddenNeurons,target[0].length,-1,1);
            
            //These are initially set to 0
            WH_delta = randomizeWeights(input[0].length,hiddenNeurons,0,0);
            WO_delta = randomizeWeights(hiddenNeurons,target[0].length,0,0);

            shuffleArray(input, target);
            
            //split number between testing and training based on ratio
            int n_training = (int) round(input.length * (1 - testing_ratio));

            
            //***********Begin training neural network
            while (badEpoch > convergenceLimit && counter < 5000){
                if (details) System.out.println("\n******  Epoch Number: " + counter + " *********");
                badEpoch = 0;
                counter++;
                if (modifyValues){
                    if (err < 0.3) err += modifyRate;
                    if (momentum > 0.1) momentum -= modifyRate;
                }
                for (int count=0;count<n_training;count++){
                    badFact = false;
                    delta = new double[target[0].length];
                    
                    //program carries out first forward pass
                    hidden = forwardPass(input[count], WH, false);
                    OUT[count] = forwardPass(hidden, WO, true);
                                        
                    //computes error value and delta for each output neuron
                    if (details) System.out.println("Vector: " + count + " de -> " + n_training);
                    for (int i=0;i<target[0].length;i++){
                        error[i] = (target[count][i] - OUT[count][i]);
                        delta[i] = OUT[count][i]*(1-OUT[count][i])*error[i];
                        if (Math.abs(error[i]) > err) badFact = true;
                        if (details){
                            System.out.println("OUT: " + OUT[count][i] + " Target: " + target[count][i] + " Error: " + error[i] + " Delta: " + delta[i]);
                        }
                    }

                    if (badFact){
                        badEpoch++;
                        //passes delta values and performs first backward pass
                            backwardPass(WO, WO_delta, hidden, delta);
                        //computes new delta values for hidden layer, using old delta values
                            delta = newDelta(delta, WO, hidden);
                        //performs second backward pass using new delta values
                            backwardPass(WH, WH_delta, input[count], delta);
                    }
                }
                if (details){
                    System.out.println("Layer 1 weights: ");
                    printWeights(WH);
                    System.out.println("Layer 2 weights: ");
                    printWeights(WO);
                }
                //updateGraph();
                //System.out.println("EPOCH: " + counter + " DE 5000");
            } 
            
            System.out.println("Number of epochs: "+counter);

            //Run test with remaining testing values
            //run(Arrays.copyOfRange(input,n_training,input.length), Arrays.copyOfRange(target,n_training,target.length));
            
            return OUT;
        }
        return null;
    }

    
    //****************************** Testing *********************************
    public int test(double[] input, double[] target){
        
        double[] hid;
        double[] out;
        double total_err = 0;
        int wrong = 0;
        int j = 0;        

        //System.out.print("Input: " + printArray(input));

        //almost same process as before, however only forward pass is done (no backward pass)
        hid = forwardPass(input,WH, false);
        out = forwardPass(hid, WO, true);

        //System.out.print("\tExpected: " + printArray(target));

        //System.out.print("\tActual:  ");
            for(double d : out){
                //System.out.print(d + "   ");
                total_err += target[j] - out[j];
                if (Math.abs(total_err) > 0.4) wrong++;
                j++;
            }
        //System.out.print("\n");
        
        return wrong;
    }

    
    //************************* Run **********************
    public double[] run(double[] input){
        double[] hid;
        double[] out;     

        hid = forwardPass(input,WH,false);
        out = forwardPass(hid,WO,true);

        return out;
    }
    
    
    //******************************** Functions ****************************
    
    // Implementing Fisher-Yates shuffle
    private void shuffleArray(double[][] ar1, double[][] ar2)
    {
      Random rnd = new Random();
      for (int i = ar1.length - 1; i > 0; i--)
      {
        int index = rnd.nextInt(i + 1);
        // Simple swap
        double[] a0 = ar1[index];
        double[] a1 = ar2[index];
        ar1[index] = ar1[i];
        ar2[index] = ar2[i];
        ar1[i] = a0;
        ar2[i] = a1;
      }
    }
    
    private double[][] randomizeWeights(int i, int j, double min, double max){
        Random rand = new Random();
        double[][] c = new double[i][j];
        for (int cnt1 = 0; cnt1<i; cnt1++){
            for (int cnt2 = 0; cnt2<j; cnt2++){
                c[cnt1][cnt2] = min + (rand.nextDouble()*(max - min));
            }
        }
        return c;
    }

    
    private double[] forwardPass(double[] in_vector, double[][] WH, boolean hidden){
        double[] out_vector = new double[WH[0].length];
        for(int i=0;i<out_vector.length;i++){
            double x=0;
            for(int j=0;j<in_vector.length;j++){
                x += (in_vector[j] * WH[j][i]);
            }
            out_vector[i] = x;
        }
        
        for(int n=0;n<out_vector.length;n++){
            if (hidden) {
                if(useSigmoid == Boolean.TRUE) {
                	out_vector[n] = sigmoid(out_vector[n]);
                }else if(useTanh == Boolean.TRUE) {
                	out_vector[n] = tanh(out_vector[n]);
                }
            }else {
            	if(useSigmoid == Boolean.TRUE) {
            		out_vector[n] = sigmoid(out_vector[n]);
            	}else  if(useTanh == Boolean.TRUE) {
            		out_vector[n] = tanh(out_vector[n]);
            	}
            }
        }
        return out_vector;
    }
    
    private void backwardPass(double[][] w, double[][] w_delta, double[] layer_in, double[] delta){
        double temp;
        for (int i=0; i<layer_in.length; i++){
            for(int j=0;j<w[0].length;j++){
                temp = (lrc * layer_in[i] * delta[j]) + (momentum * w_delta[i][j]);
                w[i][j] = w[i][j] + temp;
                w_delta[i][j] = temp;
            }
        }
    }
    
    //Calculate hidden layer delta values during back-propogation
    private double[] newDelta(double[] delta, double[][] WO, double[] hidden){
        int dlength = hidden.length;
        double[] d = new double[dlength];
        for (int i=0;i<dlength;i++){            
            for (int j=0;j<delta.length;j++){
                d[i] += delta[j] * WO[i][j];
            }
            d[i] = d[i] * (hidden[i] * (1 - hidden[i]));
        }
        return d;
    }
    
    //Squashing function
    private double sigmoid(double d){
        return (1 / (1 + Math.exp(-d)));
    }
    
    private double tanh(double d){
        return (Math.exp(d) - Math.exp(-d))/(Math.exp(d) + Math.exp(-d));
    }
    
    
    //***************************** Display functions ************************
    public void printWeights(double[][] w){
        for (int i=0;i<w.length;i++){
            for(int j=0;j<w[0].length;j++){
                System.out.print(w[i][j] + " ");
            }
            System.out.println("");
        }
    }
    
    public void printInputs(double[][] input, double[][] target){
        for (int i=0; i<input.length;i++){
            for (int j=0; j<input[0].length; j++)
                System.out.print(input[i][j] + " - ");
            System.out.println(" | ");
            for (int j=0; j<target[0].length; j++)
                System.out.print(target[i][j] + " - ");
            System.out.println("");
        }
    }


    
}
