package de.biomedical_imaging.traj.features.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.MeanSpeedFeature;

public class MeanSpeedFeatureTest {

	@Test
	public void MeanSpeedTest() {
		
		//Create trajectory object 
		int dimension=2;
		Trajectory t  = new Trajectory(dimension);
		
		//Add Positions
		int x = 0;
		int y = 0;
		int z = 0; // As the dimension is 2, it will ignore this component.
		t.add(x, y, z);
		for(int i = 1; i < 20; i++){
			y=i;
			t.add(x,y,z);
		}
		
		//Calculate the mean speed of the trajectory
		double timelag = 1.0/30;
		MeanSpeedFeature speed = new MeanSpeedFeature(t, timelag);
		
		double meanspeed = speed.evaluate()[0];
		
		assertEquals(30, meanspeed,0.01);
	}

}
