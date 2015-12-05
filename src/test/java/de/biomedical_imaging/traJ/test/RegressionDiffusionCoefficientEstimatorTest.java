package de.biomedical_imaging.traJ.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;

import org.junit.Test;

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
		double[] drift = {0,0,0};
		
		int minLag = 1;
		int maxLag = 1;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag, maxLag);
		double[] diffC = dcEst.getDiffusionCoefficient(t, 1, drift);
		
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
		int y = 0;
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, z));
		}
		double[] drift = {0,0,0};
		
		int minLag = 1;
		int maxLag = 1;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag, maxLag);
		double[] diffC = dcEst.getDiffusionCoefficient(t, 1, drift);
		
		assertEquals(1, diffC[0], DOUBLE_PRECISION);
		
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
		int y = 0;
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, i));
		}
		double[] drift = {0,0,0};
		
		int minLag = 1;
		int maxLag = 1;
		RegressionDiffusionCoefficientEstimator dcEst = new RegressionDiffusionCoefficientEstimator(minLag, maxLag);
		double[] diffC = dcEst.getDiffusionCoefficient(t, 1, drift);
		
		assertEquals(1.5, diffC[0], DOUBLE_PRECISION);
		
	}

}
