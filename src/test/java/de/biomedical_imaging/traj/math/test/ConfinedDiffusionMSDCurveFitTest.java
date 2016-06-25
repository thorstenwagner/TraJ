package de.biomedical_imaging.traj.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traj.math.ConfinedDiffusionMSDCurveFit;
import de.biomedical_imaging.traj.math.ConfinedDiffusionMSDCurveFit.FitMethod;

public class ConfinedDiffusionMSDCurveFitTest {
	
	

	@Test
	public void doFitTest_A_JOM() {
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
		fit.doFit(x,y,FitMethod.JOM_CONSTRAINED);
		
		assertEquals(A, fit.getA(),0.01);
	}
	
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
		fit.doFit(x,y,FitMethod.SIMPLEX);
		
		assertEquals(A, fit.getA(),0.01);
	}
	
	
	@Test
	public void doFitTestWithInitParamError_A_JOM() {
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
		fit.setInitParameters(new double[]{A*(0.8+Math.random()*0.2),B*(0.8+Math.random()*0.2),C*(0.8+Math.random()*0.2),D*(0.8+Math.random()*0.2)});
		fit.doFit(x,y,FitMethod.JOM_CONSTRAINED);
		
		assertEquals(A, fit.getA(),0.05);
		
	}
	
	@Test
	public void doFitTest_B_JOM() {
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
		fit.doFit(x,y,FitMethod.JOM_CONSTRAINED);

		assertEquals(B, fit.getB(),0.01);
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
		fit.doFit(x,y,FitMethod.SIMPLEX);

		assertEquals(B, fit.getB(),0.01);
	}
	
	@Test
	public void doFitTestWithInitParamError_B_JOM() {
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
		
		fit.setInitParameters(new double[]{A*(0.8+Math.random()*0.2),B*(0.8+Math.random()*0.2),C*(0.8+Math.random()*0.2),D*(0.8+Math.random()*0.2)});
		fit.doFit(x,y,FitMethod.JOM_CONSTRAINED);

		assertEquals(B, fit.getB(),0.01);
	}
	
	
	@Test
	public void doFitTest_C_JOM() {
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
		fit.doFit(x,y,FitMethod.JOM_CONSTRAINED);
		
		assertEquals(C, fit.getC(),0.01);	
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
		fit.doFit(x,y,FitMethod.SIMPLEX);
		
		assertEquals(C, fit.getC(),0.01);	
	}
	
	@Test
	public void doFitTestWithInitParamError_C_JOM() {
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
		fit.setInitParameters(new double[]{A*(0.8+Math.random()*0.2),B*(0.8+Math.random()*0.2),C*(0.8+Math.random()*0.2),D*(0.8+Math.random()*0.2)});
		fit.doFit(x,y,FitMethod.JOM_CONSTRAINED);
		
		assertEquals(C, fit.getC(),0.05);

		
	}
	
	@Test
	public void doFitTest_D_JOM() {
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
		fit.doFit(x,y,FitMethod.JOM_CONSTRAINED);
		
		assertEquals(D, fit.getD(),0.01);
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
		fit.doFit(x,y,FitMethod.SIMPLEX);
		
		assertEquals(D, fit.getD(),0.01);
	}
	
	@Test
	public void doFitTestWithInitParamError_D_JOM() {
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
		
		fit.setInitParameters(new double[]{A*(0.8+Math.random()*0.2),B*(0.8+Math.random()*0.2),C*(0.8+Math.random()*0.2),D*(0.8+Math.random()*0.2)});
		fit.doFit(x,y,FitMethod.JOM_CONSTRAINED);
		
		assertEquals(D, fit.getD(),0.02);
		
	}

}
