package de.biomedical_imaging.traj.simulation.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.scijava.vecmath.Point3d;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.simulation.AbstractSphereObstacle;
import de.biomedical_imaging.traJ.simulation.AnomalousDiffusionScene;
import de.biomedical_imaging.traJ.simulation.AnomalousDiffusionSimulator;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;
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
		assertTrue("The distance should not be smaller than the sphere radius", minDistance>obstacleRadius);
	}
	
	@Test
	public void generateTrajectory_WithDrift_NoCollisionWithManySphere(){
		double[] size = {4,4};
		AnomalousDiffusionScene scene = new AnomalousDiffusionScene(size,2);
		
		CentralRandomNumberGenerator r = CentralRandomNumberGenerator.getInstance();
		r.setSeed(10);
		double excludedVolumeTreshold  = 0.8;
		double diameterMean = 1;
		double diameterSD = 0.5/3;
		double obstacleCounter = 0;
		while(scene.estimateExcludedVolumeFraction()<excludedVolumeTreshold){
			double radius = (diameterMean + r.nextGaussian()*diameterSD)/2;
			double[] pos = {size[0]/2.0 + r.randomSign()*(size[0]/2.0-radius)*r.nextDouble(),size[1]/2.0 + r.randomSign()*(size[1]/2.0-radius)*r.nextDouble(),0};
			ImmobileSphereObstacle obstacle = new ImmobileSphereObstacle(pos, radius,2);
			scene.addObstacle(obstacle);
			obstacleCounter++;
			if(obstacleCounter%10 == 0){
			System.out.println("Excluded area fraction: " + scene.estimateExcludedVolumeFraction());
			}
		}
		
		double diffusioncoefficient = 9.02*Math.pow(10,-2); //µm^2 /s
		double timelag = 1.0/30;
		int dimension = 2;
		int numberOfSteps = 1000;
		AnomalousDiffusionSimulator sim = new AnomalousDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps, scene,0.25,Math.PI/4);
		Trajectory t = sim.generateTrajectory();
		double minDistance = Double.MAX_VALUE;
		ArrayList<AbstractSphereObstacle> obst = scene.getObstacles();
		for(int j = 0; j < obst.size(); j++){
			minDistance = Double.MAX_VALUE;
			double obstacleRadius = obst.get(j).getRadius();
			double[] pos = obst.get(j).getPosition();
			for(int i = 0; i < t.size(); i++){
				double d = t.get(i).distance(new Point3d(pos));
				if(d<minDistance){
					minDistance = d;
				}
			}
			System.out.println("dmin " + minDistance + " d " + obstacleRadius );
			assertTrue("The distance should not be smaller than the sphere radius", minDistance>obstacleRadius);
		}

	}

}
