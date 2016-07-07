package de.biomedical_imaging.traj.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traj.math.ActiveTransportMSDLineFit;

public class ActiveTransportMSDCurveFitTest {

	@Test
	public void doFitTest_DiffusionCoefficient() {
		double D = 5;
		double v = 2;
		double dt = 1.0/30;
		int N = 10;

		double[] x = new double[N];
		double[] y = new double[N];
		
		for(int i = 1; i <= N; i++){
			x[i-1] = dt*i;
			y[i-1] = 4*D*dt*i + Math.pow(v*dt*i, 2);
		}
		
		ActiveTransportMSDLineFit fit = new ActiveTransportMSDLineFit();
		fit.doFit(x, y);
		
		assertEquals(D, fit.getDiffusionCoefficient(),0.01);
	}
	
	@Test
	public void doFitTest_Velocity(){
		double D = 5;
		double v = 2;
		double dt = 1.0/30;
		int N = 10;

		double[] x = new double[N];
		double[] y = new double[N];
		
		for(int i = 1; i <= N; i++){
			x[i-1] = dt*i;
			y[i-1] = 4*D*dt*i + Math.pow(v*dt*i, 2);
		}
		
		ActiveTransportMSDLineFit fit = new ActiveTransportMSDLineFit();
		fit.doFit(x, y);
		
		assertEquals(v, fit.getVelocity(),0.01);
	}

}
