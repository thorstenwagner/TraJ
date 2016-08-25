package de.biomedical_imaging.traJ.test;


import org.scijava.vecmath.Point3d;

import org.junit.Assert;
import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectoryUtil;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.CovarianceDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.drift.StaticDriftCorrector;
import de.biomedical_imaging.traJ.simulation.ActiveTransportSimulator;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;
import de.biomedical_imaging.traJ.simulation.FreeDiffusionSimulator;

public class ConvarianceDiffusionCoefficientEstimatorTest {

	@Test
	public void testGetDiffusionCoefficient1D(){
		//TODO: Test scheint noch kein track mit covarianz von 0 zu erzeugen!
		/*
		 * Tes test generates a track with a covariance of -2 and a mean squared displacment of 2. 
		 * When the timelag is 1 then the diffusion coefficient D has to be D = MSD/2*TIMELAG+COV*TIMELAG = -1
		 * This actually makes no sense but it is an easy example.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.add(new Point3d(Math.pow(-1, i)*Math.sqrt(2)/2, y, z));
		}
		int fps = 1;
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(-1, result[0],0.00000001);
	}
	
	
	@Test
	public void testGetDiffusionCoefficient1D_WithGaps(){
		/*
		 * Test generates a track with a covariance of -2 and a mean squared displacment of 2. 
		 * When the timelag is 1 then the diffusion coefficient D has to be D = MSD/2*TIMELAG+COV*TIMELAG = -1
		 * This actually makes no sense but it is an easy example.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
	
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.add(null);
			}else{
				t.add(new Point3d(Math.pow(-1, i)*Math.sqrt(2)/2 , y, z));
			}
		}

		int fps = 1;
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(-1, result[0],0.00000001);
	}
	
	@Test
	public void testGetDiffusionCoefficient1D_Brownian(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 5;
		double timelag = 1;
		int dimension = 1;
		int numberOfSteps = 1000000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, timelag);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient2D_Brownian(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 5;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 1000000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, timelag);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient2D_Brownian_WithDrift(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 5;
		double timelag = 1;
		int dimension = 2;
		
		int numberOfSteps = 1000000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		
		double velocity = 100;
		double angularVelocity = 0;
		double direction = 0;
		ActiveTransportSimulator atg = new ActiveTransportSimulator(velocity, angularVelocity, direction, timelag, dimension, numberOfSteps);
		Trajectory pureDrift = atg.generateTrajectory();
		t = TrajectoryUtil.combineTrajectory(t, pureDrift);
		double[] drift = {velocity*timelag,0,0};
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(dcorr.removeDrift(t), timelag);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient2D_Brownian_WithDrift_CustomSettings(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 100;
		double timelag = 1.0/30;
		int dimension = 2;
		/*
		 * This drift corresponds to a velocity of 2 length units per second
		 * In 1/FPS seconds the particle moves 2 length units in a specific direction.
		 * The velocity is there for 2 * FPS (Lengths units per seconds)
		 */
		
		int numberOfSteps = 100000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		
		double velocity = 1000;
		double angularVelocity = 0;
		double direction = 0;
		ActiveTransportSimulator at = new ActiveTransportSimulator(velocity, angularVelocity, direction, timelag, dimension, numberOfSteps);
		Trajectory activeTransport = at.generateTrajectory();
		t=TrajectoryUtil.combineTrajectory(t, activeTransport);
		
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] drift = {velocity*timelag,0,0}; 
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		double[] result = dcEst.getDiffusionCoefficient(dcorr.removeDrift(t), 1/timelag);
		Assert.assertEquals(diffusioncoefficient, result[0],1);
	}
	
	@Test
	public void testGetDiffusionCoefficient3D_Brownian(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 5;
		double fps = 1;
		int dimension = 3;
		int numberOfSteps = 1000000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, fps, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	
}
