package de.biomedical_imaging.traJ.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;

import org.junit.Assert;
import org.junit.Test;

import de.biomedical_imaging.traJ.CovarianceDiffusionCoefficientEstimator;
import de.biomedical_imaging.traJ.MSDCaclulator;
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
		double[] drift = {0,0,0};
		int fps = 1;
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps, drift);
		Assert.assertEquals(-1, result[0],0.00000001);
	}
	
	@Test
	public void testGetDiffusionCoefficient1D_WithDrift(){
		/*
		 * Tes test generates a track with a covariance of -2 and a mean squared displacment of 2. 
		 * When the timelag is 1 then the diffusion coefficient D has to be D = MSD/2*TIMELAG+COV*TIMELAG = -1
		 * This actually makes no sense but it is an easy example.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		int driftx = 1;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(Math.pow(-1, i)*Math.sqrt(2)/2 + i*driftx, y, z));
		}
		double[] drift = {driftx,0,0};
		int fps = 1;
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps, drift);
		Assert.assertEquals(-1, result[0],0.00000001);
	}
	
	@Test
	public void testGetDiffusionCoefficient1D_WithDrift_WithGaps(){
		/*
		 * Tes test generates a track with a covariance of -2 and a mean squared displacment of 2. 
		 * When the timelag is 1 then the diffusion coefficient D has to be D = MSD/2*TIMELAG+COV*TIMELAG = -1
		 * This actually makes no sense but it is an easy example.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		int driftx = 1;
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(Math.pow(-1, i)*Math.sqrt(2)/2 + i*driftx, y, z));
			}
		}
		double[] drift = {driftx,0,0};
		int fps = 1;
		CovarianceDiffusionCoefficientEstimator dcEst = new CovarianceDiffusionCoefficientEstimator();
		double[] result = dcEst.getDiffusionCoefficient(t, fps, drift);
		Assert.assertEquals(-1, result[0],0.00000001);
	}
}
