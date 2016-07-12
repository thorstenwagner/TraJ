package de.biomedical_imaging.traj.simulation.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectoryUtil;
import de.biomedical_imaging.traJ.features.MeanSquaredDisplacmentFeature;
import de.biomedical_imaging.traJ.features.PowerLawFeature;
import de.biomedical_imaging.traJ.simulation.AnomalousDiffusionWMSimulation;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;

public class AnomalousDiffusionWMSimulationTest {

	@Test
	public void MSD_CORRECT_nstep_1000() {
		CentralRandomNumberGenerator.getInstance().setSeed(1);
		double diffusioncoefficient = 1;
		double timelag = 1.0/30;
		int numberOfSteps = 1000;
		double alpha = 1;
		AnomalousDiffusionWMSimulation ads = new AnomalousDiffusionWMSimulation(diffusioncoefficient, timelag, 2, numberOfSteps, alpha);
		MeanSquaredDisplacmentFeature msdf = new MeanSquaredDisplacmentFeature(ads.generateTrajectory(), 1);
		
		double res = msdf.evaluate()[0];
		assertEquals(4*diffusioncoefficient*timelag, res,0.01);
	}
	
	@Test
	public void MSD_CORRECT_nstep_10000() {
		CentralRandomNumberGenerator.getInstance().setSeed(1);
		double diffusioncoefficient = 1;
		double timelag = 1.0/30;
		int numberOfSteps = 10000;
		double alpha = 1;
		AnomalousDiffusionWMSimulation ads = new AnomalousDiffusionWMSimulation(diffusioncoefficient, timelag, 2, numberOfSteps, alpha);
		MeanSquaredDisplacmentFeature msdf = new MeanSquaredDisplacmentFeature(ads.generateTrajectory(), 1);
		
		double res = msdf.evaluate()[0];
		assertEquals(4*diffusioncoefficient*timelag, res,0.01);
	}
	
	@Test
	public void MSD_CORRECT_nstep_10000_cut1000() {
		CentralRandomNumberGenerator.getInstance().setSeed(1);
		double diffusioncoefficient = 1;
		double timelag = 1.0/30;
		int numberOfSteps = 10000;
		double alpha = 1;
		AnomalousDiffusionWMSimulation ads = new AnomalousDiffusionWMSimulation(diffusioncoefficient, timelag, 2, numberOfSteps, alpha);
		Trajectory t = ads.generateTrajectory();
		t = t.subList(0, 1000);
		MeanSquaredDisplacmentFeature msdf = new MeanSquaredDisplacmentFeature(t, 1);
		
		double res = msdf.evaluate()[0];
		assertEquals(4*diffusioncoefficient*timelag, res,0.01);
	}
	
	@Test
	public void check_alpha1() {
		CentralRandomNumberGenerator.getInstance().setSeed(1);
		double diffusioncoefficient = 1;
		double timelag = 1.0/30;
		int numberOfSteps = 10000;
		double alpha = 1;
		AnomalousDiffusionWMSimulation ads = new AnomalousDiffusionWMSimulation(diffusioncoefficient, timelag, 2, numberOfSteps, alpha);
		Trajectory t = ads.generateTrajectory();
		PowerLawFeature pwf = new PowerLawFeature(t, 1, t.size()/3);
		double res = pwf.evaluate()[0];
		assertEquals(alpha, res,0.01);
	}
	
	@Test
	public void check_alpha05() {
		CentralRandomNumberGenerator.getInstance().setSeed(1);
		double diffusioncoefficient = 1;
		double timelag = 1.0/30;
		int numberOfSteps = 10000;
		double alpha = 0.5;
		AnomalousDiffusionWMSimulation ads = new AnomalousDiffusionWMSimulation(diffusioncoefficient, timelag, 2, numberOfSteps, alpha);
		Trajectory t = ads.generateTrajectory();
		PowerLawFeature pwf = new PowerLawFeature(t, 1, t.size()/3);
		double res = pwf.evaluate()[0];
		assertEquals(alpha, res,0.04);
	}


}
