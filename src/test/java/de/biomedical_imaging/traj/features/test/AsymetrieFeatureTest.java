package de.biomedical_imaging.traj.features.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.AsymmetryFeature;
import de.biomedical_imaging.traJ.simulation.ActiveTransportSimulator;

public class AsymetrieFeatureTest {
	
	

	@Test
	public void testForLinearTracks_shouldbe1() {
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 1000;
		double velocity = 5;
		double angularVelocity = 0;
		
		ActiveTransportSimulator ats = new ActiveTransportSimulator(velocity, angularVelocity, timelag, dimension, numberOfSteps);
		Trajectory b = ats.generateTrajectory();
		
		AsymmetryFeature asm = new AsymmetryFeature(b);
		
		assertEquals(1, asm.evaluate()[0],0.000000001);
	}

}
