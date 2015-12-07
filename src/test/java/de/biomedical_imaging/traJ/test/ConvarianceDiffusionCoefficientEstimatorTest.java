package de.biomedical_imaging.traJ.test;


import javax.vecmath.Point3d;

import org.junit.Assert;
import org.junit.Test;

import de.biomedical_imaging.traJ.CovarianceDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.LinearDriftCorrector;
import de.biomedical_imaging.traJ.RandomBrownianTrackGenerator;
import de.biomedical_imaging.traJ.Trajectory;

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
			t.addPosition(new Point3d(Math.pow(-1, i)*Math.sqrt(2)/2, y, z));
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
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(Math.pow(-1, i)*Math.sqrt(2)/2 , y, z));
			}
		}

		int fps = 1;
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(-1, result[0],0.00000001);
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
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
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
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient2D_Brownian_WithDrift(){
		RandomBrownianTrackGenerator gen = new RandomBrownianTrackGenerator(3);
		double diffusioncoefficient = 5;
		double fps = 1;
		int dimension = 2;
		double[] drift = {1,1,0};
		int numberOfSteps = 1000000;
		Trajectory t = gen.calculateBrownianTrack(diffusioncoefficient, fps, dimension, drift, numberOfSteps);
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		double[] result = dcEst.getDiffusionCoefficient(dcorr.removeDrift(t), fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	@Test
	public void testGetDiffusionCoefficient2D_Brownian_WithDrift_CustomSettings(){
		RandomBrownianTrackGenerator gen = new RandomBrownianTrackGenerator(3);
		double diffusioncoefficient = 100;
		double fps = 30;
		int dimension = 2;
		double[] drift = {5,2,0};
		int numberOfSteps = 1000000;
		Trajectory t = gen.calculateBrownianTrack(diffusioncoefficient, fps, dimension, drift, numberOfSteps);
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		double[] result = dcEst.getDiffusionCoefficient(dcorr.removeDrift(t), fps);
		Assert.assertEquals(diffusioncoefficient, result[0],1);
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
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps);
		Assert.assertEquals(diffusioncoefficient, result[0],0.1);
	}
	
	
}
