package daryl.system.robots.ann.calculator.forecaster;

public class Simulation {

    public Simulation(){

        RunSimulation("NFLX_modified", 3524, 3273);
    }

    public void RunSimulation(String fileName, int total_array_size, int training_size){

        System.out.println(">>> " + fileName);

        String dataFilePath = "data/" + fileName + ".csv";

        // Run buy and hold strategy
        //new BuyAndHold(fileName, total_array_size, training_size, total_array_size);

        // Train ANN model for each asset
        new BuildANNModel(fileName, dataFilePath, total_array_size, training_size);

        // Run algorithmic trading strategy
        //new TradingSystem(dataFilePath, total_array_size, training_size);

        // Run hybrid trading strategy
        //new HybridSystem(fileName,total_array_size, training_size);

        // Traing Genetic Algorithms and get best performing chromosome
        //GA_Weighting ga = new GA_Weighting();
        //int[] indicatorWeights = ga.runSimulation(fileName,total_array_size, training_size);

        // Run hybrid trading strategy with GA
        //GAHybridSystem hybridSystem = new GAHybridSystem(fileName,total_array_size, 0, training_size, false);
        //hybridSystem.tradingSimulation(indicatorWeights);
    }

}