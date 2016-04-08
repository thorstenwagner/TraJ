package de.biomedical_imaging.traj.features.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.StraightnessFeature;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;
import de.biomedical_imaging.traJ.simulation.FreeDiffusionSimulator;

public class StraightnessFeatureTest {

	@Test
	public void getStraightness_StraightLine() {
		Trajectory t = new Trajectory(2);
		int Nsteps = 100;
		for(int i = 0; i < Nsteps; i++){
			t.add(i, i, 0);
		}
		StraightnessFeature sf = new StraightnessFeature(t);
		
		assertEquals(1.0, sf.getStraightness(), 0.000001);
	}
	
	@Test
	public void getStraightness_NotStraight(){
	
		Trajectory t = new Trajectory(2);
		
		t.add(0, 0, 0);
		t.add(0, 1, 0);
		t.add(1, 1, 0);
		t.add(1, 0, 0);
		//Sum = 3
		//Distance start-end = 1;
		//Expected Straightness = 1/3;
		
		StraightnessFeature sf = new StraightnessFeature(t);
		assertEquals(1.0/3, sf.getStraightness(), 0.000001);
		
	}

}
