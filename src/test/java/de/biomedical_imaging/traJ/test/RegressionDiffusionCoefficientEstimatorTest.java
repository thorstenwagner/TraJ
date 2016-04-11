package de.biomedical_imaging.traJ.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;

import org.junit.Assert;
import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.DiffusionCoefficientEstimator.RegressionDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;
import de.biomedical_imaging.traJ.simulation.FreeDiffusionSimulator;

public class RegressionDiffusionCoefficientEstimatorTest {
	private static final double DOUBLE_PRECISION = 0.000000001;
	@Test
	public void testGetDiffusionCoefficient1D() {
		/*
		 * When each the distance between each position is sqrt(2), the MSD should be 2.
		 * When fps = 1 and then diffusion coefficient D has to be D = MSD/(2*dt) = 2/(2*1)=1.
		 * The drift correction has to be deactivated! However, this is only valid for a
		 * lag of 1 because for the MSD  for such a track grows quadratically
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.add(new Point3d(i*Math.sqrt(2), y, z));
		}
		
		int minLag = 1;
		int maxLag = 1;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag, maxLag);
		double[] diffC = dcEst.getDiffusionCoefficient(t, 1);
		
		assertEquals(1, diffC[0], DOUBLE_PRECISION);
		
	}
	
	@Test
	public void testGetDiffusionCoefficient2D() {
		/*
		 * When each the distance between each position is sqrt(2), the MSD should be 2.
		 * When fps = 1 and then diffusion coefficient D has to be D = MSD/(2*dt) = 2/(2*1)=1.
		 * The drift correction has to be deactivated! However, this is only valid for a
		 * lag of 1 because for the MSD  for such a track grows quadratically
		 */
		Trajectory t = new Trajectory(1);
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.add(new Point3d(i, i, z));
		}
		
		int minLag = 1;
		int maxLag = 1;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag, maxLag);
		double[] diffC = dcEst.getDiffusionCoefficient(t, 1);
		
		assertEquals(1, diffC[0], DOUBLE_PRECISION);
	}
	
	@Test
	public void testGetDiffusionCoefficient1D_Brownian(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 5;
		double fps = 1;
		int dimension = 1;
		int numberOfSteps = 1000000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, fps, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(1,2);
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient2D_Brownian(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 5;
		double fps = 1;
		int dimension = 2;
		int numberOfSteps = 1000000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, fps, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		int minLag = 1;
		int maxLag = 2;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag,maxLag);
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient2D_Brownian_NMSettings(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 50 * Math.pow(10,-9) * Math.pow(10,-9);
		double fps = 130;
		int dimension = 2;
		int numberOfSteps = 1000000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, fps, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		int minLag = 1;
		int maxLag = 2;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag,maxLag);
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
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
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(1,2);
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient3D_Brownian_CustomSettings(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		double diffusioncoefficient = 100;
		double timelag = 1.0/30;
		int dimension = 3;
		int numberOfSteps = 1000000;
		FreeDiffusionSimulator gen = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = gen.generateTrajectory();
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(1,2);
		double[] result = dcEst.getDiffusionCoefficient(t, 1.0/timelag);
		Assert.assertEquals(diffusioncoefficient, result[0],0.2);
	}
	
	@Test
	public void testGetDiffusionCoefficient3D() {
		/*
		 * When each the distance between each position is sqrt(3), the MSD should be 3.
		 * When fps = 1 and then diffusion coefficient D has to be D = MSD/(2*dt) = 3/(2*1)=1.5.
		 * The drift correction has to be deactivated! However, this is only valid for a
		 * lag of 1 because for the MSD  for such a track grows quadratically
		 */
		Trajectory t = new Trajectory(1);
		for(int i = 0; i < 100; i++){
			t.add(new Point3d(i, i, i));
		}
		
		int minLag = 1;
		int maxLag = 1;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag, maxLag);
		double[] diffC = dcEst.getDiffusionCoefficient(t, 1);
		
		assertEquals(1.5, diffC[0], DOUBLE_PRECISION);
		
	}

}
