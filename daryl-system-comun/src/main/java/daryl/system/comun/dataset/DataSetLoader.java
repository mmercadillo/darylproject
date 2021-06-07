package daryl.system.comun.dataset;

import java.util.List;

import org.neuroph.core.data.DataSet;

import daryl.system.comun.enums.Mode;



public abstract class DataSetLoader {

	public abstract void loadData(DataSet dataSet, List<Datos> datos, Mode mode, int neuronasEntrada);
	public abstract void loadInputDataOnly(DataSet dataSet, List<Datos> datos, Mode mode);
	public abstract void loadRawData(DataSet dataSet, List<Double> datos, int neuronasEntrada);
	
}