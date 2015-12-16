package de.biomedical_imaging.traJ.simulation;

import java.util.Random;

import javax.vecmath.Point3d;

import org.apache.commons.math3.analysis.function.Sqrt;

import de.biomedical_imaging.traJ.Trajectory;

public class ConfinedDiffusionSimulator extends AbstractSimulator {
	private CentralRandomNumberGenerator r;
	double diffusioncoefficient;
	double timelag;
	double radius; //Radius of confinement
	int dimension;
	int numberOfSteps;
	
	
	public ConfinedDiffusionSimulator(double diffusioncoefficient, double timelag, double radius, int dimension,int numberOfSteps) {
		this.diffusioncoefficient = diffusioncoefficient;
		this.timelag = timelag;
		this.radius = radius;
		this.dimension = dimension;
		this.numberOfSteps = numberOfSteps;
		r = CentralRandomNumberGenerator.getInstance();
	}
	
	@Override
	public Trajectory generateTrajectory() {
		Trajectory t = new Trajectory(dimension);
		t.add(new Point3d(0, 0, 0));
		Point3d center = new Point3d(0, 0, 0);

		for(int i = 1; i <= numberOfSteps; i++) {
			Point3d pos = nextConfinedPosition(100,t.get(t.size()-1));
			pos.setX(pos.x);
			pos.setY(pos.y);
			pos.setZ(pos.z);
			t.add(pos);
		}
		
		return t;
	}
	
	/**
	 * Simulates a single step (for dt) of a confined diffusion inside of a circle. 
     * Therefore each step is split up in N substeps. A substep which collidates 
     *  with an object is set to the previous position.
	 * @param Nsub number of substeps 
	 * @return
	 */
	private Point3d nextConfinedPosition(int Nsub, Point3d lastPosition){
		
		double timelagSub = timelag / Nsub;
		Point3d center = new Point3d(0, 0, 0);
		Point3d lastValidPosition = lastPosition;
		int validSteps = 0;
		while(validSteps<Nsub){
			double u = r.nextDouble();
			double steplength = Math.sqrt(-2*dimension*diffusioncoefficient*timelagSub*Math.log(1-u));
			
			Point3d candiate = SimulationUtil.randomPosition(dimension, steplength);
			candiate.add(lastValidPosition);
			if(center.distance(candiate)<radius){
				lastValidPosition = candiate;
				validSteps++;
				
			}
		}
		
		return lastValidPosition;
	}

}
