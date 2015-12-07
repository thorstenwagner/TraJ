package de.biomedical_imaging.traJ.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;

import org.junit.Assert;
import org.junit.Test;

import de.biomedical_imaging.traJ.RandomBrownianTrackGenerator;
import de.biomedical_imaging.traJ.RegressionDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.Trajectory;

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
			t.addPosition(new Point3d(i*Math.sqrt(2), y, z));
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
			t.addPosition(new Point3d(i, i, z));
		}
		
		int minLag = 1;
		int maxLag = 1;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag, maxLag);
		double[] diffC = dcEst.getDiffusionCoefficient(t, 1);
		
		assertEquals(1, diffC[0], DOUBLE_PRECISION);
	}
	
	@Test
	public void testGetDiffusionCoefficient1D_Brownian(){
		RandomBrownianTrackGenerator gen = new RandomBrownianTrackGenerator(3);
		double diffusioncoefficient = 5;
		double fps = 1;
		int dimension = 1;
		double[] drift = {0,0,0};
		int numberOfSteps = 1000000;
		Trajectory t = gen.calculateBrownianTrack(diffusioncoefficient, fps, dimension, drift, numberOfSteps);
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(1,2);
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient2D_Brownian(){
		RandomBrownianTrackGenerator gen = new RandomBrownianTrackGenerator(3);
		double diffusioncoefficient = 5;
		double fps = 1;
		int dimension = 2;
		double[] drift = {0,0,0};
		int numberOfSteps = 1000000;
		Trajectory t = gen.calculateBrownianTrack(diffusioncoefficient, fps, dimension, drift, numberOfSteps);
		int minLag = 1;
		int maxLag = 2;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag,maxLag);
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient3D_Brownian(){
		RandomBrownianTrackGenerator gen = new RandomBrownianTrackGenerator(3);
		double diffusioncoefficient = 5;
		double fps = 1;
		int dimension = 3;
		double[] drift = {0,0,0};
		int numberOfSteps = 1000000;
		Trajectory t = gen.calculateBrownianTrack(diffusioncoefficient, fps, dimension, drift, numberOfSteps);
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(1,2);
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient3D_Brownian_CustomSettings(){
		RandomBrownianTrackGenerator gen = new RandomBrownianTrackGenerator(3);
		double diffusioncoefficient = 100;
		double fps = 30;
		int dimension = 3;
		double[] drift = {0,0,0};
		int numberOfSteps = 1000000;
		Trajectory t = gen.calculateBrownianTrack(diffusioncoefficient, fps, dimension, drift, numberOfSteps);
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(1,2);
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
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
			t.addPosition(new Point3d(i, i, i));
		}
		
		int minLag = 1;
		int maxLag = 1;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag, maxLag);
		double[] diffC = dcEst.getDiffusionCoefficient(t, 1);
		
		assertEquals(1.5, diffC[0], DOUBLE_PRECISION);
		
	}

}
