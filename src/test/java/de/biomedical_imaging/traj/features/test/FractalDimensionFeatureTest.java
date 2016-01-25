package de.biomedical_imaging.traj.features.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.FractalDimensionFeature;
import de.biomedical_imaging.traJ.simulation.ActiveTransportSimulator;
import de.biomedical_imaging.traJ.simulation.FreeDiffusionSimulator;

public class FractalDimensionFeatureTest {

	@Test
	public void pureBrownianMotion_99Step_test() {
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 99;
		FreeDiffusionSimulator sim = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = sim.generateTrajectory();
		FractalDimensionFeature feature= new FractalDimensionFeature(t);
		t.showTrajectory();
		double f = feature.evaluate()[0];
		/**
		 * According Table 1 of 
		 * M. J. Katz and E. B. George, 
		 * “Fractals and the analysis of growth paths,” 
		 * Bull. Math. Biol., vol. 47, no. 2, pp. 273–286, 1985.
		 */
		assertEquals(0.587, Math.log(f),3*0.108);
	}
	
	@Test
	public void pureBrownianMotion_29Steps_test() {
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 29;
		FreeDiffusionSimulator sim = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = sim.generateTrajectory();
		FractalDimensionFeature feature= new FractalDimensionFeature(t);
		t.showTrajectory();
		double f = feature.evaluate()[0];
		assertEquals(0.592, Math.log(f),3*0.141);
	}
	
	@Test
	public void pureBrownianMotion_Straight_test() {
		double velocity = 1;
		double angularVelocity = 0;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 100;
		ActiveTransportSimulator sim = new ActiveTransportSimulator(velocity, angularVelocity, timelag, dimension, numberOfSteps);
		Trajectory t = sim.generateTrajectory();
		FractalDimensionFeature feature= new FractalDimensionFeature(t);
		t.showTrajectory();
		double f = feature.evaluate()[0];
		assertEquals(1, f, 0.001);
	}

}
