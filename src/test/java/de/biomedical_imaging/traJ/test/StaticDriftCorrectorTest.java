package de.biomedical_imaging.traJ.test;

import org.scijava.vecmath.Point3d;

import org.apache.commons.math3.analysis.function.Sqrt;
import org.junit.Test;

import static org.junit.Assert.*;
import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectoryUtil;
import de.biomedical_imaging.traJ.drift.StaticDriftCorrector;
import de.biomedical_imaging.traJ.simulation.ActiveTransportSimulator;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;
import de.biomedical_imaging.traJ.simulation.FreeDiffusionSimulator;

public class StaticDriftCorrectorTest {
	
	@Test
	public void removeDriftTest_1d(){
		Trajectory t = new Trajectory(1);
		Trajectory tWithDrift = new Trajectory(1);
		int z = 0;
		int y = 0;
		double[] drift = {1,0,0};
		for(int i = 0; i < 100; i++){
			t.add(new Point3d(i, y, z));
			tWithDrift.add(new Point3d(i+ i*drift[0], y, z));
		}
		
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	@Test
	public void removeDriftTest_2d(){
		Trajectory t = new Trajectory(2);
		Trajectory tWithDrift = new Trajectory(2);
		int z = 0;
		double[] drift = {1,2,0};
		for(int i = 0; i < 100; i++){
			t.add(new Point3d(i, i, z));
			tWithDrift.add(new Point3d(i+ i*drift[0], i+i*drift[1], z));
		}
		
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	@Test
	public void removeDriftTest_2d_Brownian(){
		
		
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 1000;
		FreeDiffusionSimulator rg = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = rg.generateTrajectory();
		double velocity = 5;
		double angularVelocity = 0;
		ActiveTransportSimulator atg = new ActiveTransportSimulator(velocity*timelag, angularVelocity,0 ,timelag, dimension, numberOfSteps);
		Trajectory pureDrift = atg.generateTrajectory();
		Trajectory tWithDrift = TrajectoryUtil.combineTrajectory(t, pureDrift);

		double[] drift = {5,0,0};
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray()[0], tCorrected.getPositionsAsArray()[0],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[1], tCorrected.getPositionsAsArray()[1],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[2], tCorrected.getPositionsAsArray()[2],0.000001);
	}
	
	@Test
	public void removeDriftTest_2d_Brownian_CustomSettings(){
		
		
		double diffusioncoefficient = 10;
		double timelag = 1/30;
		int dimension = 2;
		int numberOfSteps = 1000;
		FreeDiffusionSimulator rg = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = rg.generateTrajectory();
		double velocity = 90;
		double angularVelocity = 0;
		double direction = 0; //Measured in 3d. Zero means the direction points to x-axis
		ActiveTransportSimulator atg = new ActiveTransportSimulator(velocity, angularVelocity,direction ,timelag, dimension, numberOfSteps);
		Trajectory pureDrift = atg.generateTrajectory();
		Trajectory tWithDrift = TrajectoryUtil.combineTrajectory(t, pureDrift);

		double[] drift = {velocity*timelag,0,0};
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray()[0], tCorrected.getPositionsAsArray()[0],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[1], tCorrected.getPositionsAsArray()[1],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[2], tCorrected.getPositionsAsArray()[2],0.000001);
	}
	
	@Test
	public void removeDriftTest_3d_Brownian(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		
		
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 3;
		int numberOfSteps = 1000;
		FreeDiffusionSimulator rg = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = rg.generateTrajectory();
		double velocity = 5;
		double angularVelocity = 0;
		double direction = 0; //Mesaured in rad. Zero means the direction points to z-axis
		ActiveTransportSimulator atg = new ActiveTransportSimulator(velocity*timelag, angularVelocity,direction ,timelag, dimension, numberOfSteps);
		Trajectory pureDrift = atg.generateTrajectory();
		Trajectory tWithDrift = TrajectoryUtil.combineTrajectory(t, pureDrift);

		double[] drift = {0,0,5};
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray()[0], tCorrected.getPositionsAsArray()[0],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[1], tCorrected.getPositionsAsArray()[1],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[2], tCorrected.getPositionsAsArray()[2],0.000001);
	}
	
	@Test
	public void removeDriftTest_3d_Brownian_CustomSettings(){
		
		
		double diffusioncoefficient = 10;
		double timelag = 1.0/30;
		int dimension = 3;
		int numberOfSteps = 5;
		FreeDiffusionSimulator rg = new FreeDiffusionSimulator(diffusioncoefficient, timelag, dimension, numberOfSteps);
		Trajectory t = rg.generateTrajectory();
		double velocity = 90;
		double angularVelocity = 0;
		double direction = 0; //Measured in rad. Zero means the direction points to z-axis
		ActiveTransportSimulator atg = new ActiveTransportSimulator(velocity, angularVelocity,direction ,timelag, dimension, numberOfSteps);
		Trajectory pureDrift = atg.generateTrajectory();
		Trajectory tWithDrift = TrajectoryUtil.combineTrajectory(t, pureDrift);

		double[] drift = {0,0,velocity*timelag};
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray()[0], tCorrected.getPositionsAsArray()[0],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[1], tCorrected.getPositionsAsArray()[1],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[2], tCorrected.getPositionsAsArray()[2],0.000001);
	}
	
	@Test
	public void removeDriftTest_2d_WithGaps(){
		Trajectory t = new Trajectory(2);
		Trajectory tWithDrift = new Trajectory(2);
		int z = 0;
		double[] drift = {1,2,0};
		for(int i = 0; i < 100; i++){
			if((i+1)%10==0){
				t.add(null);
				tWithDrift.add(null);
			}
			else
			{
				t.add(new Point3d(i, i, z));
				tWithDrift.add(new Point3d(i+ i*drift[0], i+i*drift[1], z));
			}
		}
		
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	@Test
	public void removeDriftTest_3d(){
		Trajectory t = new Trajectory(2);
		Trajectory tWithDrift = new Trajectory(2);
		double[] drift = {1,2,3};
		for(int i = 0; i < 100; i++){
			t.add(new Point3d(i, i, i));
			tWithDrift.add(new Point3d(i+ i*drift[0], i+i*drift[1], i+i*drift[2]));
		}
		
		StaticDriftCorrector dcorr = new StaticDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	

}
