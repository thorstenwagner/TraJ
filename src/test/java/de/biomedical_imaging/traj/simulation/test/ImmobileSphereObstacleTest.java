package de.biomedical_imaging.traj.simulation.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.simulation.AnomalousDiffusionScene;
import de.biomedical_imaging.traJ.simulation.ImmobileSphereObstacle;

public class ImmobileSphereObstacleTest {

	@Test
	public void insideSzeneBoundariesTest_OutOfSceneBounds() {
		double[] size = {512,512,512};
		AnomalousDiffusionScene s = new AnomalousDiffusionScene(size,3);
		double[] obstaclePosition = {127,127,513};
		ImmobileSphereObstacle obs = new ImmobileSphereObstacle(obstaclePosition,11.0,3);
		boolean result = obs.insideSzeneBoundaries(s);
		assertFalse(result);
	}
	
	@Test
	public void insideSzeneBoundariesTest_OutOfSceneBounds2() {
		double[] size = {512,512,512};
		AnomalousDiffusionScene s = new AnomalousDiffusionScene(size,3);
		double[] obstaclePosition = {127,127,502+2*512};
		ImmobileSphereObstacle obs = new ImmobileSphereObstacle(obstaclePosition,11.0,3);
		boolean result = obs.insideSzeneBoundaries(s);
		assertFalse(result);
	}
	
	@Test
	public void insideSzeneBoundariesTest_InsideOfSceneBounds() {
		double[] size = {512,512,512};
		AnomalousDiffusionScene s = new AnomalousDiffusionScene(size,3);
		double[] obstaclePosition = {127,127,500};
		ImmobileSphereObstacle obs = new ImmobileSphereObstacle(obstaclePosition,11.0,3);
		boolean result = obs.insideSzeneBoundaries(s);
		assertTrue(result);
	}
	
	@Test
	public void intersectionVolumeTest_NoOverlap2d(){
		double[] obstaclePosition = {127,127};
		int radius = 10;
		ImmobileSphereObstacle obs = new ImmobileSphereObstacle(obstaclePosition,radius,2);
		double[] obstaclePosition2 = {127+21,127+21};
		ImmobileSphereObstacle obs2 = new ImmobileSphereObstacle(obstaclePosition2,radius,2);
		double intersectionArea = obs.intersectionVolume(obs2);
		assertEquals(0, intersectionArea,0.000001);
	}
	
	@Test
	public void intersectionVolumeTest_HalfOverlap2d(){
		double[] obstaclePosition = {127,127};
		int radius = 1;
		ImmobileSphereObstacle obs = new ImmobileSphereObstacle(obstaclePosition,radius,2);
		double[] obstaclePosition2 = {127,127+0.8079455}; //see (17) http://mathworld.wolfram.com/Circle-CircleIntersection.html
		ImmobileSphereObstacle obs2 = new ImmobileSphereObstacle(obstaclePosition2,radius,2);
		double intersectionArea = obs.intersectionVolume(obs2);
		double area = obs.getVolume();
		assertEquals(area/2, intersectionArea,0.000001);
	}
	
	public void intersectionVolumeTest_CompleteOverlap2d(){
		double[] obstaclePosition = {127,127};
		ImmobileSphereObstacle obs = new ImmobileSphereObstacle(obstaclePosition,11.0,2);
		ImmobileSphereObstacle obs2 = new ImmobileSphereObstacle(obstaclePosition,11.0,2);
		double intersectionArea = obs.intersectionVolume(obs2);
		double area = obs.getVolume();
		assertEquals(area, intersectionArea,0.001);
	}
	
	@Test
	public void intersectionVolumeTest_CompleteOverlap3d(){
		double[] obstaclePosition = {127,127,127};
		ImmobileSphereObstacle obs = new ImmobileSphereObstacle(obstaclePosition,11.0,3);
		ImmobileSphereObstacle obs2 = new ImmobileSphereObstacle(obstaclePosition,11.0,3);
		double intersectionArea = obs.intersectionVolume(obs2);
		double area = obs.getVolume();
		assertEquals(area, intersectionArea,0.001);
	}
	
	

}
