package de.biomedical_imaging.traJ.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.simulation.AnomalousDiffusionScene;
import de.biomedical_imaging.traJ.simulation.AnomalousDiffusionSimulator;
import de.biomedical_imaging.traJ.simulation.ImmobileSphereObstacle;

public class AnomalousDiffusionSimulatorTest {

	@Test
	public void generateTrajectory_CorrectNumberOfSteps() {
		double[] size = {512,512};
		AnomalousDiffusionScene scene = new AnomalousDiffusionScene(size,2);
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 10;
		AnomalousDiffusionSimulator sim = new AnomalousDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps, scene);
		Trajectory t = sim.generateTrajectory();
		assertEquals(numberOfSteps, t.size()-1);	
	}
	
	@Test
	public void generateTrajectory_NoCollisionWithLargeSphere(){
		double[] size = {512,512};
		AnomalousDiffusionScene scene = new AnomalousDiffusionScene(size,2);
		double[] pos = {255,255,0};
		double obstacleRadius = 200;
		ImmobileSphereObstacle obstacle = new ImmobileSphereObstacle(pos, obstacleRadius,2);
		scene.addObstacle(obstacle);
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 100000;
		AnomalousDiffusionSimulator sim = new AnomalousDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps, scene);
		Trajectory t = sim.generateTrajectory();
		double minDistance = Double.MAX_VALUE;
		for(int i = 0; i < t.size(); i++){
			double d = t.get(i).distance(new Point3d(pos));
			if(d<minDistance){
				minDistance = d;
			}
		}
		System.out.println("d " + minDistance);
		assertTrue("The distance should not be smaller than the sphere radius", minDistance>obstacleRadius);
	}
	
	@Test
	public void generateTrajectory_WithDrift_NoCollisionWithLargeSphere(){
		double[] size = {512,512};
		AnomalousDiffusionScene scene = new AnomalousDiffusionScene(size,2);
		double[] pos = {255,255,0};
		double obstacleRadius = 200;
		ImmobileSphereObstacle obstacle = new ImmobileSphereObstacle(pos, obstacleRadius,2);
		scene.addObstacle(obstacle);
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 1000;
		AnomalousDiffusionSimulator sim = new AnomalousDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps, scene,0.25,Math.PI/4);
		Trajectory t = sim.generateTrajectory();
		double minDistance = Double.MAX_VALUE;
		for(int i = 0; i < t.size(); i++){
			double d = t.get(i).distance(new Point3d(pos));
			if(d<minDistance){
				minDistance = d;
			}
		}
		t.showTrajectory();
		System.out.println("d " + minDistance);
		assertTrue("The distance should not be smaller than the sphere radius", minDistance>obstacleRadius);
	}

}
