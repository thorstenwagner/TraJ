package de.biomedical_imaging.traj.simulation.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.simulation.ConfinedDiffusionSimulator;

public class ConfinedDiffusionSimulatorTest {

	@Test
	public void generateTrajectoryTest2D_maxDistanceSmallerThenRadius() {
		
		double diffusioncoefficient = 1;
		double timelag = 1;
		double radius = 2;
		int dimension = 2;
		int numberOfSteps = 1000;
		ConfinedDiffusionSimulator cs = new ConfinedDiffusionSimulator(diffusioncoefficient, timelag, radius, dimension, numberOfSteps);
		Trajectory t = cs.generateTrajectory();
		double maxDistance = 0;
		Point3d center = new Point3d(0, 0, 0);
		
		for(int i = 0; i < t.size(); i++){
			double distance = center.distance(t.get(i));
			if(distance>maxDistance){
				maxDistance = distance;
			}
		}
		
		assertTrue(maxDistance<=radius);
		
	}

}
