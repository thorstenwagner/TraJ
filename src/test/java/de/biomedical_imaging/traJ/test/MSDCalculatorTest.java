package de.biomedical_imaging.traJ.test;

import javax.vecmath.Point3d;

import org.junit.Assert;
import org.junit.Test;

import de.biomedical_imaging.traJ.MSDCalculator;
import de.biomedical_imaging.traJ.Trajectory;

public class MSDCalculatorTest {
	
	private static final double DOUBLE_PRECISION = 0.000000001;
	
	/*
	 * #####################################
	 * 1D Tests
	 * #####################################
	 */
	
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
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(2.0, result[0], DOUBLE_PRECISION);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment1D_Timelag2() {
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
		int timelag = 2;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(8, result[0], DOUBLE_PRECISION);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment1D_Drift1() {
		/*
		 * This test generates a straight 1D trajectory with drift of 1. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		int driftx = 1;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i*Math.sqrt(2)+i*driftx, y, z));
		}
		
		double[] drift = {1,0,0};
		int timelag = 1;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(2.0, result[0], DOUBLE_PRECISION);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment1D_Drift1_Timelag2() {
		/*
		 * This test generates a straight 1D trajectory with drift of 1. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		int driftx = 1;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i*Math.sqrt(2)+i*driftx, y, z));
		}
		
		double[] drift = {1,0,0};
		int timelag = 2;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(8.0, result[0], DOUBLE_PRECISION);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment1D_WithGaps() {
		/*
		 * This test generates a straight 1D trajectory with no drift but with gaps. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(i*Math.sqrt(2), y, z));
			}
		}
		
		double[] drift = {0,0,0};
		int timelag = 1;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(2.0, result[0], DOUBLE_PRECISION);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment1D_Drift1_Timelag2_WithGaps() {
		/*
		 * This test generates a straight 1D trajectory with drift of 1. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(1);
		int y = 0;
		int z = 0;
		int driftx = 1;
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(i*Math.sqrt(2)+i*driftx, y, z));
			}
		}
		
		double[] drift = {1,0,0};
		int timelag = 2;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(8.0, result[0], DOUBLE_PRECISION);
	}
	

	
	/*
	 * #####################################
	 * 2D Tests
	 * #####################################
	 */
	
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
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
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
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(8, result[0], 0);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment2D_WithDrift_Timelag2() {
		/*
		 * This test generates a straight 2D trajectory with no drift. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(2);
		int z = 0;
		int driftx = 1;
		int drifty = 1;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i + i*driftx, i+i*drifty, z));
		}
		
		double[] drift = {1,1,0};
		int timelag = 2;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(8, result[0], 0);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment2D_WithDrift_Timelag2_WithGaps() {
		/*
		 * This test generates a straight 2D trajectory with no drift. The distance
		 * between each position is sqrt(2). The mean squared displacment
		 * should be n^2 * 2 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(2);
		int z = 0;
		int driftx = 1;
		int drifty = 1;
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(i + i*driftx, i+i*drifty, z));
			}
		}
		
		double[] drift = {1,1,0};
		int timelag = 2;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(8, result[0], 0);
	}
	
	/*
	 * #####################################
	 * 3D Tests
	 * #####################################
	 */
	
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
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
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
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(4*3, result[0], 0);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment3D_Timelag2_WithDrift() {
		/*
		 * This test generates a straight 3D trajectory with no drift. The distance
		 * between each position is sqrt(3). The mean squared displacment
		 * should be n^2 * 3 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(3);
		int driftx = 1;
		int drifty = 2;
		int driftz = 3;
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i+i*driftx, i+i*drifty, i+i*driftz));
		}
		
		double[] drift = {driftx,drifty,driftz};
		int timelag = 2;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(4*3, result[0], 0);
	}
	
	@Test
	public void testGetMeanSquaredDisplacment3D_Timelag2_WithDrift_WithGaps() {
		/*
		 * This test generates a straight 3D trajectory with no drift. The distance
		 * between each position is sqrt(3). The mean squared displacment
		 * should be n^2 * 3 where n is the timelag >= 1.
		 */
		Trajectory t = new Trajectory(3);
		int driftx = 1;
		int drifty = 2;
		int driftz = 3;
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
			}else{
				t.addPosition(new Point3d(i+i*driftx, i+i*drifty, i+i*driftz));
			}
		}
		
		double[] drift = {driftx,drifty,driftz};
		int timelag = 2;
		double[] result = MSDCalculator.getMeanSquaredDisplacment(t, timelag, drift);
		Assert.assertEquals(4*3, result[0], 0);
	}
}
