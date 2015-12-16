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
		switch (dimension) {
		case 1:
			t.addPosition(new Point3d(100, 0, 0));
			break;
		case 2:
			t.addPosition(new Point3d(100, 100, 0));	
			break;
		case 3:
			t.addPosition(new Point3d(100, 100, 100));
			break;

		default:
			break;
		}
	
		for(int i = 1; i <= numberOfSteps; i++) {
			double steplength = Math.sqrt(-2*dimension*diffusioncoefficient*timelag*Math.log(1-r.nextDouble()));
			Point3d pos = randomPosition(dimension,steplength);
			pos.setX(t.getPositions().get(i-1).x + pos.x);
			pos.setY(t.getPositions().get(i-1).y + pos.y);
			pos.setZ(t.getPositions().get(i-1).z + pos.z);
			t.addPosition(pos);
		}
		
		return t;
	}
	
	private Point3d randomPosition(int dimension, double length){
		
		
		
		Point3d p = null;
		
		switch (dimension) {
			case 1:
				int sign = 1;
				if(r.nextDouble()>0.5){
					sign = -1;
				}
				p = new Point3d(sign*length, 0, 0);
				break;
			case 2:
				double alpha = 2*Math.PI*r.nextDouble();
				p = new Point3d(Math.cos(alpha)*length, Math.sin(alpha)*length, 0);
				break;
			case 3:
				double u = 2*r.nextDouble()-1;
		
				double theta = 2*Math.PI*r.nextDouble();

				double x = Math.sqrt(1-u*u)*Math.cos(theta)*length;

				double y = Math.sqrt(1-u*u)*Math.sin(theta)*length;
				double z = u*length;
				/*
				 * Pick random position on unit sphere:
				 * http://mathworld.wolfram.com/SpherePointPicking.html
				 */
				p = new Point3d(x, y, z);
				break;
			default:
				break;
		}
		
		return p;
		
	}

}
