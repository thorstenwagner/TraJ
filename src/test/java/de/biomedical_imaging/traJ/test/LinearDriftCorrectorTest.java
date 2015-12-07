package de.biomedical_imaging.traJ.test;

import javax.vecmath.Point3d;

import org.junit.Test;

import static org.junit.Assert.*;
import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.drift.LinearDriftCorrector;

public class LinearDriftCorrectorTest {
	
	@Test
	public void removeDriftTest_1d(){
		Trajectory t = new Trajectory(1);
		Trajectory tWithDrift = new Trajectory(1);
		int z = 0;
		int y = 0;
		double[] drift = {1,0,0};
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, y, z));
			tWithDrift.addPosition(new Point3d(i+ i*drift[0], y, z));
		}
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	@Test
	public void removeDriftTest_2d(){
		Trajectory t = new Trajectory(2);
		Trajectory tWithDrift = new Trajectory(2);
		int z = 0;
		double[] drift = {1,2,0};
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, z));
			tWithDrift.addPosition(new Point3d(i+ i*drift[0], i+i*drift[1], z));
		}
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	@Test
	public void removeDriftTest_2d_WithGaps(){
		Trajectory t = new Trajectory(2);
		Trajectory tWithDrift = new Trajectory(2);
		int z = 0;
		double[] drift = {1,2,0};
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.addPosition(null);
				tWithDrift.addPosition(null);
			}
			else
			{
				t.addPosition(new Point3d(i, i, z));
				tWithDrift.addPosition(new Point3d(i+ i*drift[0], i+i*drift[1], z));
			}
		}
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	@Test
	public void removeDriftTest_3d(){
		Trajectory t = new Trajectory(2);
		Trajectory tWithDrift = new Trajectory(2);
		double[] drift = {1,2,3};
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, i));
			tWithDrift.addPosition(new Point3d(i+ i*drift[0], i+i*drift[1], i+i*drift[2]));
		}
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	

}
