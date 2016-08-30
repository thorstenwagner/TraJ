package de.biomedical_imaging.traj.features.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.MeanSpeedFeature;

public class MeanSpeedFeatureTest {

	@Test
	public void MeanSpeedTest() {
		Trajectory t  = new Trajectory(2);
		
		t.add(0, 0, 0);
	
		for(int i = 1; i < 20; i++){
			t.add(0,i,0);
		}
		double timelag = 1.0/30;
		MeanSpeedFeature speed = new MeanSpeedFeature(t, timelag);
		
		double meanspeed = speed.evaluate()[0];
		
		assertEquals(30, meanspeed,0.01);
	}

}
