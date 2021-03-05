package daryl.arima.gen;
import java.util.*;

public class AR {
	
	double[] stdoriginalData={};
	int p;
	ARMAMath armamath=new ARMAMath();
	
	/**
	 * 
	 * @param stdoriginalData
	 * @param p 
	 */
	public AR(double [] stdoriginalData,int p)
	{
		this.stdoriginalData=stdoriginalData;
		this.p=p;
	}
/**
 * 
 * @return
 */
	public Vector<double[]> ARmodel()
	{
		Vector<double[]> v=new Vector<double[]>();
		v.add(armamath.parcorrCompute(stdoriginalData, p, 0));
		return v;
	
	}
	
}
