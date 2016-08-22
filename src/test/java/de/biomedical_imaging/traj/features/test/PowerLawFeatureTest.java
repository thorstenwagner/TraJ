package de.biomedical_imaging.traj.features.test;

import static org.junit.Assert.*;
import ij.IJ;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.PowerLawFeature;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;
import de.biomedical_imaging.traJ.simulation.FreeDiffusionSimulator;

public class PowerLawFeatureTest {

	@Test
	public void PowerLawTest_ReasonableValue_FreeDiffusion() {
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 10000;
		CentralRandomNumberGenerator.getInstance().setSeed(12);
		FreeDiffusionSimulator sim = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = sim.generateTrajectory();
		PowerLawFeature plf = new PowerLawFeature(t, 1/timelag,1, t.size()/200);
		double alpha = plf.evaluate()[0];
		assertEquals(1.0, alpha,0.01);
		
	}

}
