package de.biomedical_imaging.traj.math.test;

import static org.junit.Assert.*;
import ij.IJ;

import org.junit.Test;
import org.omg.PortableInterceptor.SUCCESSFUL;

import de.biomedical_imaging.traJ.features.PowerLawFeature;
import de.biomedical_imaging.traj.math.PowerLawCurveFit;
import de.biomedical_imaging.traj.math.PowerLawCurveFit.FitMethod;

public class PowerLawFitTest {

	public static boolean isIpotInstalled(){
		try{
			System.loadLibrary("ipopt");
		}
		catch(UnsatisfiedLinkError e){
			return false;
		}
		return true;
	}
	
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
		pwf.doFit(x, y, FitMethod.SIMPLEX);
		
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
		pwf.doFit(x, y, FitMethod.SIMPLEX);
		
		assertEquals(dc, pwf.getDiffusionCoefficient(),0.0001);
	}
	
	@Test
	public void PowerLawFeatureTest_JOM_ALPHA() {
		if(isIpotInstalled()){
			double dt = 1.0/30;
			double dc = 0.1;
			double alpha = 0.2;
			double[] x = new double[10];
			double[] y = new double[10];
			
			for(int i = 1; i <= 10; i++){
				x[i-1] = i*dt;
				y[i-1] = 4*dc*Math.pow(i*dt, alpha);
			}
			
			PowerLawCurveFit pwf = new PowerLawCurveFit();
			pwf.doFit(x, y, FitMethod.JOM_CONSTRAINED);
			assertEquals(alpha, pwf.getAlpha(),0.0001);
		}
		else{
			assertTrue("Was not tested", true);
		}
	}
	
	@Test
	public void PowerLawFeatureTest_JOM_DIFFCOEFF() {
		if(isIpotInstalled()){
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
			pwf.doFit(x, y, FitMethod.JOM_CONSTRAINED);
			
			assertEquals(dc, pwf.getDiffusionCoefficient(),0.0001);
		}
		else{
			assertTrue("Was not tested", true);
		}
	}

}
