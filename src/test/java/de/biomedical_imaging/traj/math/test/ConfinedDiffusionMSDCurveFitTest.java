package de.biomedical_imaging.traj.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traj.math.ConfinedDiffusionMSDCurveFit;

public class ConfinedDiffusionMSDCurveFitTest {
	
	@Test
	public void doFitTest_A_SIMPLEX() {
		double A = 0.5;
		double B = 0.7;
		double C = 0.3;
		double D = 0.09;
		double dt = 1.0/30;
		
		int N = 10;
		double[] x = new double[N];
		double[] y = new double[N];
		
		for(int i = 1; i <= N; i++){
			x[i-1] = dt*i;
			y[i-1] = A*(1-B*Math.exp((-4*D)*(x[i-1]/A)*C));
		}
		
		ConfinedDiffusionMSDCurveFit fit = new ConfinedDiffusionMSDCurveFit();
		fit.setInitParameters(new double[]{A,B,C,D});
		fit.doFit(x,y,false);
		
		assertEquals(A, fit.getA(),0.01);
	}
	
	@Test
	public void doFitTest_A_REDUCED_SIMPLEX() {
		double A = 10;
		double B = 1;
		double C = 1;
		double D = 5;
		double dt = 1.0/30;
		
		int N = 10;
		double[] x = new double[N];
		double[] y = new double[N];
		
		for(int i = 1; i <= N; i++){
			x[i-1] = dt*i;
			y[i-1] = A*(1-B*Math.exp((-4*D)*(x[i-1]/A)*C));
		}
		
		ConfinedDiffusionMSDCurveFit fit = new ConfinedDiffusionMSDCurveFit();
		fit.setInitParameters(new double[]{A,B,C,D});
		fit.doFit(x,y,true);
		
		assertEquals(A, fit.getA(),0.01);
	}
	
	@Test
	public void doFitTest_D_REDUCED_SIMPLEX() {
		double A = 0.5;
		double B = 1;
		double C = 1;
		double D = 1000;
		double dt = 1.0/30;
		
		int N = 10;
		double[] x = new double[N];
		double[] y = new double[N];
		
		for(int i = 1; i <= N; i++){
			x[i-1] = dt*i;
			y[i-1] = A*(1-B*Math.exp((-4*D)*(x[i-1]/A)*C));
		}
		
		ConfinedDiffusionMSDCurveFit fit = new ConfinedDiffusionMSDCurveFit();
		fit.setInitParameters(new double[]{A,B,C,D});
		fit.doFit(x,y,true);
		
		assertEquals(D, fit.getD(),0.01);
	}
	
	
	@Test
	public void doFitTest_B_SIMPLEX() {
		double A = 0.5;
		double B = 0.7;
		double C = 0.3;
		double D = 0.09;
		double dt = 1.0/30;
		
		int N = 500;
		double[] x = new double[N];
		double[] y = new double[N];
		
		for(int i = 1; i <= N; i++){
			x[i-1] = dt*i;
			y[i-1] = A*(1-B*Math.exp((-4*D)*(x[i-1]/A)*C));
		}
		
		ConfinedDiffusionMSDCurveFit fit = new ConfinedDiffusionMSDCurveFit();
		
		fit.setInitParameters(new double[]{A,B,C,D});
		fit.doFit(x,y,false);

		assertEquals(B, fit.getB(),0.01);
	}
	
	
	@Test
	public void doFitTest_C_SIMPLEX() {
		double A = 0.5;
		double B = 0.7;
		double C = 0.3;
		double D = 0.09;
		double dt = 1.0/30;
		
		int N = 10;
		double[] x = new double[N];
		double[] y = new double[N];
		
		for(int i = 1; i <= N; i++){
			x[i-1] = dt*i;
			y[i-1] = A*(1-B*Math.exp((-4*D)*(x[i-1]/A)*C));
		}
		
		ConfinedDiffusionMSDCurveFit fit = new ConfinedDiffusionMSDCurveFit();
		fit.setInitParameters(new double[]{A,B,C,D});
		fit.doFit(x,y,false);
		
		assertEquals(C, fit.getC(),0.01);	
	}
	
	
	@Test
	public void doFitTest_D_SIMPLEX() {
		double A = 0.5;
		double B = 0.7;
		double C = 0.3;
		double D = 0.09;
		double dt = 1.0/30;
		
		int N = 10;
		double[] x = new double[N];
		double[] y = new double[N];
		
		for(int i = 1; i <= N; i++){
			x[i-1] = dt*i;
			y[i-1] = A*(1-B*Math.exp((-4*D)*(x[i-1]/A)*C));
		}
		
		ConfinedDiffusionMSDCurveFit fit = new ConfinedDiffusionMSDCurveFit();
		fit.setInitParameters(new double[]{A,B,C,D});
		fit.doFit(x,y,false);
		
		assertEquals(D, fit.getD(),0.01);
	}

}
