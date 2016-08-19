/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wehi.celcalc.cohort.old;

/**
 *
 * @author pavlyshyn.d
 */
public class XSLReaderTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	System.out.println("started");
        CohortXLSReader reader = new CohortXLSReader();
		reader.parseFile("data/sample data input.xls");
		System.out.println("finished");
    }

}
