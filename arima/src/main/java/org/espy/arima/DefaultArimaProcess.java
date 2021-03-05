package org.espy.arima;

import java.util.Arrays;

import static org.espy.arima.DoubleUtils.EMPTY_DOUBLE_ARRAY;

public class DefaultArimaProcess implements ArimaProcess {
    private double[] arCoefficients = EMPTY_DOUBLE_ARRAY;
    private double[] maCoefficients = EMPTY_DOUBLE_ARRAY;
    private int integrationOrder;
    private double shockExpectation;
    private double shockVariation = 1;
    private double constant;
    private int std;

    public int getStd() {
		return std;
	}
    
    public void setStd(int std) {
		this.std = std;
	}

    public int getArOrder() {
        return arCoefficients.length;
    }

    public int getIntegrationOrder() {
        return integrationOrder;
    }

    public int getMaOrder() {
        return maCoefficients.length;
    }

    public void setIntegrationOrder(int integrationOrder) {
        this.integrationOrder = integrationOrder;
    }

    public double[] getArCoefficients() {
        return arCoefficients;
    }

    public void setArCoefficients(double... arCoefficients) {
        this.arCoefficients = arCoefficients;
    }

    public double[] getMaCoefficients() {
        return maCoefficients;
    }

    public void setMaCoefficients(double... maCoefficients) {
        this.maCoefficients = maCoefficients;
    }

    public double getShockExpectation() {
        return shockExpectation;
    }

    public void setShockExpectation(double shockExpectation) {
        this.shockExpectation = shockExpectation;
    }

    public double getShockVariation() {
        return shockVariation;
    }

    public void setShockVariation(double shockVariation) {
        this.shockVariation = shockVariation;
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    public String toString() {
        return "ArimaProcess{" +
                "arCoefficients=" + Arrays.toString(arCoefficients) +
                ", maCoefficients=" + Arrays.toString(maCoefficients) +
                ", Standar Deviation=" + std +
                ", integrationOrder=" + integrationOrder +
                ", shockExpectation=" + shockExpectation +
                ", shockVariation=" + shockVariation +
                ", constant=" + constant +
                '}';
    }
}
