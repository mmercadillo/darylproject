package org.espy.arima;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultArimaProcessRealization implements ArimaProcessRealization {

    private ArimaProcess arimaProcess;
    private DifferentiatedObservationWindow differentiatedObservationWindow;
    private ObservationErrorWindow observationErrorWindow;
    private ArmaFormula armaFormula;

    public DefaultArimaProcessRealization(ArimaProcess arimaProcess) {
        this(arimaProcess, ThreadLocalRandom.current());
    }

    public DefaultArimaProcessRealization(ArimaProcess arimaProcess, Random random) {
        this.arimaProcess = arimaProcess;
        this.differentiatedObservationWindow =
                new DifferentiatedObservationWindow(arimaProcess.getIntegrationOrder(), arimaProcess.getArOrder());
        this.observationErrorWindow = new ObservationErrorWindow(arimaProcess.getMaOrder());
        armaFormula = new ArmaFormula(random);
        armaFormula.setArCoefficients(arimaProcess.getArCoefficients());
        armaFormula.setMaCoefficients(arimaProcess.getMaCoefficients());
        armaFormula.setExpectation(arimaProcess.getShockExpectation());
        armaFormula.setStandardDeviation(Math.sqrt(arimaProcess.getShockVariation()));
        armaFormula.setConstant(arimaProcess.getConstant());
    }

    public double[] getArCoefficients() {
        return arimaProcess.getArCoefficients();
    }

    public double[] getMaCoefficients() {
        return arimaProcess.getMaCoefficients();
    }

    public double getShockExpectation() {
        return arimaProcess.getShockExpectation();
    }

    public double getShockVariation() {
        return arimaProcess.getShockVariation();
    }

    public double getConstant() {
        return arimaProcess.getConstant();
    }

    public int getArOrder() {
        return arimaProcess.getArOrder();
    }

    public int getIntegrationOrder() {
        return arimaProcess.getIntegrationOrder();
    }

    public int getMaOrder() {
        return arimaProcess.getMaOrder();
    }

    public double[] next(int size) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = next();
        }
        return result;
    }

    public double next() {
        double[] arArguments = differentiatedObservationWindow.getDifferentiatedObservations();
        double[] maArguments = observationErrorWindow.getObservationErrors();
        ArmaFormula.Result result = armaFormula.evaluate(arArguments, maArguments);
        observationErrorWindow.pushObservationError(result.observationError);
        return differentiatedObservationWindow.pushDifferentiatedObservation(result.observation);
    }

    public String toString() {
        return "ArimaProcessRealization{" +
                "arimaProcess=" + arimaProcess +
                '}';
    }

	@Override
	public int getStd() {
		// TODO Auto-generated method stub
		return arimaProcess.getStd();
	}
}
