package test;

import java.awt.Font;

public class Test_Format extends Test_ {
	public static void main(String[] args) {

		String s;
		
		s = String.format("%s %d", "testing", 3);
		assert(s.equals("testing 3"));
		
        assert(String.format("%s %d-%d", "testing", 3, 4).equals("testing 3-4"));

		s = test(100 * Math.pow(10, -6), 2);
		System.out.println(s);
		assert s.equals("prec=2 0.00 0.00010 1.00e-04");

		s = test(100 * Math.pow(10, 4), 2);
		System.out.println(s);
		assert s.equals("prec=2 1000000.00 1.0e+06 1.00e+06");
		
		System.out.println("Test_Format OK");		
	}

	private static String test(double value, int prec) {
		System.out.println(value);
    String s = "prec=" + prec + " " + String.format("%1." + prec + "f", value) + " " +
				String.format("%1." + prec + "g", value) + " " 
    		+ String.format("%1." + prec + "e", value);

    return s;
	}

}
