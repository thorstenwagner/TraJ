package de.biomedical_imaging.traj.features.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.features.StandardDeviationDirectionFeature;

public class StandardDeviationDirectionTest {

	@Test
	public void ZeroStandardDeviation() {
		Trajectory t = new Trajectory();
		
		t.add(0, 0, 0);
		int i = 1;
		t.add(i, i, 0);i++;
		t.add(i, i, 0);i++;
		t.add(i, i, 0);i++;
		t.add(i, i, 0);i++;
		t.add(i, i, 0);i++;
		t.add(i, i, 0);i++;
		t.add(i, i, 0);i++;
		t.add(i, i, 0);i++;
		t.add(i, i, 0);i++;
		
		StandardDeviationDirectionFeature sdf = new StandardDeviationDirectionFeature(t, 1);
		
		double res = sdf.evaluate()[0];
		
		assertEquals(0,res,0.00001);
		
		
	}
	
	@Test
	public void ZeroStandardDeviation2() {
		Trajectory t = new Trajectory();
		
		t.add(0, 0, 0);
		int i = 1;
		t.add(1, i, 0);i++;
		t.add(0, i, 0);i++;
		t.add(1, i, 0);i++;
		t.add(0, i, 0);i++;
		t.add(1, i, 0);i++;
		t.add(0, i, 0);i++;
		t.add(1, i, 0);i++;
		t.add(0, i, 0);i++;
		t.add(1, i, 0);i++;
		
		StandardDeviationDirectionFeature sdf = new StandardDeviationDirectionFeature(t, 1);
		
		double res = sdf.evaluate()[0];
		
		assertEquals(0,res,0.00001);
		
		
	}

}
