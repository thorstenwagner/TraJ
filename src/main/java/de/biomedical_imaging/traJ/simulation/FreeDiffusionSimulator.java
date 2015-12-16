package de.biomedical_imaging.traJ.simulation;

import java.util.Random;

import javax.vecmath.Point3d;

import de.biomedical_imaging.traJ.Trajectory;

public class FreeDiffusionSimulator extends AbstractSimulator {
	private Random r;
	double diffusioncoefficient;
	double timelag;
	int dimension;
	int numberOfSteps;
	
	public FreeDiffusionSimulator(double diffusioncoefficient, double timelag, int dimension,int numberOfSteps) {
		r = CentralRandomNumberGenerator.getInstance();
		this.diffusioncoefficient = diffusioncoefficient;
		this.timelag = timelag;
		this.dimension = dimension;
		this.numberOfSteps = numberOfSteps;
	}
	
	@Override
	public Trajectory generateTrajectory() {
		Trajectory t = new Trajectory(dimension);
		t.add(new Point3d(0, 0, 0));
	
		for(int i = 1; i <= numberOfSteps; i++) {
			double steplength = Math.sqrt(-2*dimension*diffusioncoefficient*timelag*Math.log(1-r.nextDouble()));
			Point3d pos = SimulationUtil.randomPosition(dimension,steplength);
			pos.setX(t.get(i-1).x + pos.x);
			pos.setY(t.get(i-1).y + pos.y);
			pos.setZ(t.get(i-1).z + pos.z);
			t.add(pos);
		}
		
		return t;
	}
}
