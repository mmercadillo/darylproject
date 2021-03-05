
public class A {

	public static void main(String[] args) {
		
		String[] valores = {
			"1.0", "-1.0",
			"0.9", "-0.9",
			"0.8", "-0.8",
			"0.7", "-0.7",
			"0.6", "-0.6",
			"0.5", "-0.5",
			"0.4", "-0.4",
			"0.3", "-0.3",
			"0.2", "-0.2",
			"0.1", "-0.1",
			"0.01", "-0.01",
			"0.001", "-0.001"
		};
		
       for (int i=0;i<valores.length;i++) {
           countAllCombinations(String.valueOf(valores[i]), i, valores);
       }
		
		
	}
	
	private static void countAllCombinations (String input,int idx, String[] options) {
	       for(int i = idx ; i < options.length; i++) {
	           String output = input+","+options[i];
	           System.out.println(output);
	           countAllCombinations(output,++idx, options);
	       }
	}


}
