package de.biomedical_imaging.traJ.test;

import javax.vecmath.Point3d;

import org.apache.commons.math3.analysis.function.Sqrt;
import org.junit.Test;

import static org.junit.Assert.*;
import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectoryUtil;
import de.biomedical_imaging.traJ.drift.LinearDriftCorrector;
import de.biomedical_imaging.traJ.simulation.ActiveTransportTrackGenerator;
import de.biomedical_imaging.traJ.simulation.CentralRandomNumberGenerator;
import de.biomedical_imaging.traJ.simulation.RandomBrownianTrackGenerator;

public class LinearDriftCorrectorTest {
	
	@Test
	public void removeDriftTest_1d(){
		Trajectory t = new Trajectory(1);
		Trajectory tWithDrift = new Trajectory(1);
		int z = 0;
		int y = 0;
		double[] drift = {1,0,0};
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, y, z));
			tWithDrift.addPosition(new Point3d(i+ i*drift[0], y, z));
		}
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
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
			t.addPosition(new Point3d(i, i, z));
			tWithDrift.addPosition(new Point3d(i+ i*drift[0], i+i*drift[1], z));
		}
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	@Test
	public void removeDriftTest_2d_Brownian(){
		RandomBrownianTrackGenerator rg = new RandomBrownianTrackGenerator();
		ActiveTransportTrackGenerator atg = new ActiveTransportTrackGenerator();
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 2;
		int numberOfSteps = 1000;
		Trajectory t = rg.calculateBrownianTrack(diffusioncoefficient, timelag, dimension, numberOfSteps);
		double velocity = 5;
		double angularVelocity = 0;
		Trajectory pureDrift = atg.generateActiveTransportTrajectory(velocity*timelag, angularVelocity,0 ,timelag, dimension, numberOfSteps);
		Trajectory tWithDrift = TrajectoryUtil.combineTrajectory(t, pureDrift);

		double[] drift = {5,0,0};
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray()[0], tCorrected.getPositionsAsArray()[0],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[1], tCorrected.getPositionsAsArray()[1],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[2], tCorrected.getPositionsAsArray()[2],0.000001);
	}
	
	@Test
	public void removeDriftTest_2d_Brownian_CustomSettings(){
		RandomBrownianTrackGenerator rg = new RandomBrownianTrackGenerator();
		ActiveTransportTrackGenerator atg = new ActiveTransportTrackGenerator();
		double diffusioncoefficient = 10;
		double timelag = 1/30;
		int dimension = 2;
		int numberOfSteps = 1000;
		Trajectory t = rg.calculateBrownianTrack(diffusioncoefficient, timelag, dimension, numberOfSteps);
		double velocity = 90;
		double angularVelocity = 0;
		double direction = 0; //Measured in 3d. Zero means the direction points to x-axis
		Trajectory pureDrift = atg.generateActiveTransportTrajectory(velocity, angularVelocity,direction ,timelag, dimension, numberOfSteps);
		Trajectory tWithDrift = TrajectoryUtil.combineTrajectory(t, pureDrift);

		double[] drift = {velocity*timelag,0,0};
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray()[0], tCorrected.getPositionsAsArray()[0],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[1], tCorrected.getPositionsAsArray()[1],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[2], tCorrected.getPositionsAsArray()[2],0.000001);
	}
	
	@Test
	public void removeDriftTest_3d_Brownian(){
		CentralRandomNumberGenerator.getInstance().setSeed(3);
		RandomBrownianTrackGenerator rg = new RandomBrownianTrackGenerator();
		ActiveTransportTrackGenerator atg = new ActiveTransportTrackGenerator();
		double diffusioncoefficient = 1;
		double timelag = 1;
		int dimension = 3;
		int numberOfSteps = 1000;
		Trajectory t = rg.calculateBrownianTrack(diffusioncoefficient, timelag, dimension, numberOfSteps);
		double velocity = 5;
		double angularVelocity = 0;
		double direction = 0; //Mesaured in rad. Zero means the direction points to z-axis
		Trajectory pureDrift = atg.generateActiveTransportTrajectory(velocity*timelag, angularVelocity,direction ,timelag, dimension, numberOfSteps);
		Trajectory tWithDrift = TrajectoryUtil.combineTrajectory(t, pureDrift);

		double[] drift = {0,0,5};
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray()[0], tCorrected.getPositionsAsArray()[0],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[1], tCorrected.getPositionsAsArray()[1],0.000001);
		assertArrayEquals(t.getPositionsAsArray()[2], tCorrected.getPositionsAsArray()[2],0.000001);
	}
	
	@Test
	public void removeDriftTest_3d_Brownian_CustomSettings(){
		RandomBrownianTrackGenerator rg = new RandomBrownianTrackGenerator();
		ActiveTransportTrackGenerator atg = new ActiveTransportTrackGenerator();
		double diffusioncoefficient = 10;
		double timelag = 1.0/30;
		int dimension = 3;
		int numberOfSteps = 5;
		Trajectory t = rg.calculateBrownianTrack(diffusioncoefficient, timelag, dimension, numberOfSteps);
		double velocity = 90;
		double angularVelocity = 0;
		double direction = 0; //Measured in rad. Zero means the direction points to z-axis
		Trajectory pureDrift = atg.generateActiveTransportTrajectory(velocity, angularVelocity,direction ,timelag, dimension, numberOfSteps);
		Trajectory tWithDrift = TrajectoryUtil.combineTrajectory(t, pureDrift);

		double[] drift = {0,0,velocity*timelag};
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
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
				t.addPosition(null);
				tWithDrift.addPosition(null);
			}
			else
			{
				t.addPosition(new Point3d(i, i, z));
				tWithDrift.addPosition(new Point3d(i+ i*drift[0], i+i*drift[1], z));
			}
		}
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	@Test
	public void removeDriftTest_3d(){
		Trajectory t = new Trajectory(2);
		Trajectory tWithDrift = new Trajectory(2);
		double[] drift = {1,2,3};
		for(int i = 0; i < 100; i++){
			t.addPosition(new Point3d(i, i, i));
			tWithDrift.addPosition(new Point3d(i+ i*drift[0], i+i*drift[1], i+i*drift[2]));
		}
		
		LinearDriftCorrector dcorr = new LinearDriftCorrector(drift);
		
		Trajectory tCorrected = dcorr.removeDrift(tWithDrift);
		
		assertArrayEquals(t.getPositionsAsArray(), tCorrected.getPositionsAsArray());
	}
	
	

}
