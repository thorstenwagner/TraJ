package de.biomedical_imaging.traJ.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.simulation.ActiveTransportTrackSimulator;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;

public class ActiveTransportTrackGeneratorTest {
	
	@Test
	public void generateActiveTransportTrajectory_NoAngularChange_1D_Straight() {
		/*
		 * When the angular velocity is 0, then the trajectory should be straight line.
		 * The sum of distances between each position should be the same as the distance between
		 * start and endpoint
		 */
		
		double velocity = 1;
		double angularVelocity = 0;
		double timelag = 1;
		int dimension = 1;
		int numberOfSteps = 10;
		
		ActiveTransportTrackSimulator active = new ActiveTransportTrackSimulator(velocity, angularVelocity, timelag, dimension, numberOfSteps);
		Trajectory t = active.generateTrajectory();
		
		double distance = t.getPositions().get(0).distance(t.getPositions().get(numberOfSteps));
		
		assertEquals(numberOfSteps*velocity*timelag, distance,0.001);
	}

	@Test
	public void generateActiveTransportTrajectory_NoAngularChange_2D_Straight() {
		/*
		 * When the angular velocity is 0, then the trajectory should be straight line.
		 * The sum of distances between each position should be the same as the distance between
		 * start and endpoint
		 */
		
		double velocity = 1;
		double angularVelocity = 0;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 10;
		
		ActiveTransportTrackSimulator active = new ActiveTransportTrackSimulator(velocity, angularVelocity, timelag, dimension, numberOfSteps);
		Trajectory t = active.generateTrajectory();
		
		double distance = t.getPositions().get(0).distance(t.getPositions().get(numberOfSteps));
		
		assertEquals(numberOfSteps*velocity*timelag, distance,0.001);
	}
	
	@Test
	public void generateActiveTransportTrajectory_NoAngularChange_3D_Straight() {
		/*
		 * When the angular velocity is 0, then the trajectory should be straight line.
		 * The distance between start and endpoint should be numberOfSteps*velocity*timelag
		 */
		
		double velocity = 1;
		double angularVelocity = 0;
		double timelag = 1;
		int dimension = 3;
		int numberOfSteps = 1000;
		ActiveTransportTrackSimulator active = new ActiveTransportTrackSimulator(velocity, angularVelocity, timelag, dimension, numberOfSteps);
		Trajectory t = active.generateTrajectory();
		
		double distance = t.getPositions().get(0).distance(t.getPositions().get(numberOfSteps));
		
		assertEquals(numberOfSteps*velocity*timelag, distance,0.001);
	}
	
	@Test
	public void generateActiveTransportTrajectory_2D_AngularChangeShouldBeConstant() {
		/*
		 * The difference of angles should be constant
		 */
		
		double velocity = 1;
		double angularVelocity = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 100;
		ActiveTransportTrackSimulator active = new ActiveTransportTrackSimulator(velocity, angularVelocity, timelag, dimension, numberOfSteps);
		Trajectory t = active.generateTrajectory();
		
		//double distance = t.getPositions().get(0).distance(t.getPositions().get(numberOfSteps));
		ArrayList<Point3d> pos = t.getPositions();
		for(int i = 2; i < t.getPositions().size(); i++){

			Point3d h1 = new Point3d(pos.get(i-1).x-pos.get(i-2).x, pos.get(i-1).y-pos.get(i-2).y, pos.get(i-1).z-pos.get(i-2).z);
			Point3d h2 = new Point3d(pos.get(i).x-pos.get(i-1).x, pos.get(i).y-pos.get(i-1).y, pos.get(i).z-pos.get(i-1).z);
			double angle = (new Vector3d(h1)).angle(new Vector3d(h2));
			assertEquals(angularVelocity*timelag, angle,0.001);
			
		}
		
		
	}
	
	@Test
	public void generateActiveTransportTrajectory_3D_AngularChangeShouldBeConstant_Theta() {
		/*
		 * The difference of angles should be constant
		 */
		
		double velocity = 1;
		double angularVelocity = 0.8;
		double timelag = 1;
		int dimension = 3;
		int numberOfSteps = 100;
		
		ActiveTransportTrackSimulator active = new ActiveTransportTrackSimulator(velocity, angularVelocity, timelag, dimension, numberOfSteps);
		Trajectory t = active.generateTrajectory();
		
		//double distance = t.getPositions().get(0).distance(t.getPositions().get(numberOfSteps));
		ArrayList<Point3d> pos = t.getPositions();
		for(int i = 2; i < t.getPositions().size(); i++){

		    Point3d h1 = new Point3d(pos.get(i-1).x-pos.get(i-2).x, pos.get(i-1).y-pos.get(i-2).y, pos.get(i-1).z-pos.get(i-2).z);
			
			Point3d h2 = new Point3d(pos.get(i).x-pos.get(i-1).x, pos.get(i).y-pos.get(i-1).y , pos.get(i).z-pos.get(i-1).z);
	
			double thetaH2 = Math.atan(h2.y/h2.x);
			double thetaH1 = Math.atan(h1.y/h1.x);

			assertEquals(angularVelocity*timelag, Math.min(Math.abs(thetaH2-thetaH1), Math.PI-Math.abs(thetaH2-thetaH1)),0.001);

			
		}
	}
	
	
	

}
