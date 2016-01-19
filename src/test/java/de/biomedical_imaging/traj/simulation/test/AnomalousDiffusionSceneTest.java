package de.biomedical_imaging.traj.simulation.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.simulation.AnomalousDiffusionScene;

public class AnomalousDiffusionSceneTest {

	@Test
	public void checkCollisionTest1() {
		double[] size = {512,512,512};
		AnomalousDiffusionScene s = new AnomalousDiffusionScene(size,3);
		
		double[] input = {-1,256,512};
		double[] result = s.convertToScenePosition(input);
		double[] expected = {511,256,0};
		
		assertArrayEquals(expected, result, 0.00001);
	}
	
	@Test
	public void checkCollisionTest2() {
		double[] size = {512,512,512};
		AnomalousDiffusionScene s = new AnomalousDiffusionScene(size,3);
		
		double[] input = {-2,127,513};
		double[] result = s.convertToScenePosition(input);
		double[] expected = {510,127,1};
		
		assertArrayEquals(expected, result, 0.00001);
	}
	
	@Test
	public void checkCollisionTest3() {
		double[] size = {512,512,512};
		AnomalousDiffusionScene s = new AnomalousDiffusionScene(size,3);
		
		double[] input = {511+3*512,127,513};
		double[] result = s.convertToScenePosition(input);
		double[] expected = {511,127,1};
		
		assertArrayEquals(expected, result, 0.00001);
	}
	

}
