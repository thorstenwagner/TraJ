/*
The MIT License (MIT)

Copyright (c) 2015-2016 Thorsten Wagner (wagner@biomedical-imaging.de)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package de.biomedical_imaging.traJ.simulation;

import javax.vecmath.Point3d;


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
