package de.biomedical_imaging.traj.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traj.math.PowerLawCurveFit;

public class PowerLawFitTest {

	
	@Test
	public void PowerLawFeatureTest_SIMPLEX_ALPHA() {
		
		
		double dt = 1.0/30;
		double dc = 0.1;
		double alpha = 0.5;
		double[] x = new double[10];
		double[] y = new double[10];
		
		for(int i = 1; i <= 10; i++){
			x[i-1] = i*dt;
			y[i-1] = 4*dc*Math.pow(i*dt, alpha);
		}
		
		PowerLawCurveFit pwf = new PowerLawCurveFit();
		pwf.doFit(x, y);
		
		assertEquals(alpha, pwf.getAlpha(),0.0001);
	}
	
	@Test
	public void PowerLawFeatureTest_SIMPLEX_DIFFCOEFF() {
		
		double dt = 1.0/30;
		double dc = 0.1;
		double alpha = 0.5;
		double[] x = new double[10];
		double[] y = new double[10];
	
		for(int i = 1; i <= 10; i++){
			x[i-1] = i*dt;
			y[i-1] = 4*dc*Math.pow(i*dt, alpha);
		}
		
		PowerLawCurveFit pwf = new PowerLawCurveFit();
		pwf.doFit(x, y);
		
		assertEquals(dc, pwf.getDiffusionCoefficient(),0.0001);
	}
	

	


}
