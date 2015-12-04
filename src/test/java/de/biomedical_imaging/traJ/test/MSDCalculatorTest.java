package de.biomedical_imaging.traJ.test;

import javax.vecmath.Point3d;

import org.junit.Assert;
import org.junit.Test;

import de.biomedical_imaging.traJ.MSDCaclulator;
import de.biomedical_imaging.traJ.Trajectory;

public class MSDCalculatorTest {
	@Test
	public void testGetMeanSquaredDisplacment1D() {
		/*
		 * This test generates a straight 1D trajectory with no drift. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i*Math.sqrt(2), y, z));
		}
		
		double[] drift = {0,0,0};
		int timelag = 1;
		double[] result = MSDCaclulator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(2.0, result[0], 0);

	}
	
	@Test
	public void testGetMeanSquaredDisplacment2D() {
		/*
		 * This test generates a straight 2D trajectory with no drift. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(2);
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, z));
		}
		
		double[] drift = {0,0,0};
		int timelag = 1;
		double[] result = MSDCaclulator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(2.0, result[0], 0);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment2D_Timelag2() {
		/*
		 * This test generates a straight 2D trajectory with no drift. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(2);
		int z = 0;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, z));
		}
		
		double[] drift = {0,0,0};
		int timelag = 2;
		double[] result = MSDCaclulator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(8, result[0], 0);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment3D() {
		/*
		 * This test generates a straight 3D trajectory with no drift. The distance
		 * between each position is sqrt(3). The mean squared displacment
		 * should be n^2 * 3 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(3);
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, i));
		}
		
		double[] drift = {0,0,0};
		int timelag = 1;
		double[] result = MSDCaclulator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(3.0, result[0], 0);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment3D_Timelag2() {
		/*
		 * This test generates a straight 3D trajectory with no drift. The distance
		 * between each position is sqrt(3). The mean squared displacment
		 * should be n^2 * 3 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(3);
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, i));
		}
		
		double[] drift = {0,0,0};
		int timelag = 2;
		double[] result = MSDCaclulator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(4*3, result[0], 0);
	}
}
