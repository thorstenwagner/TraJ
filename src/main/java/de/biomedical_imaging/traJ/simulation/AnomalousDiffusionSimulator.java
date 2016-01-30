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

public class AnomalousDiffusionSimulator extends AbstractSimulator {
	private AnomalousDiffusionScene scene;
	private double timelag;
	private int dimension;
	private int numberOfSteps;
	private double diffusioncoefficient;
	private CentralRandomNumberGenerator r;
	private final double numberOfSubsteps = 100;
	private Trajectory drift;
	private Point3d start;

	public AnomalousDiffusionSimulator(double diffusioncoefficient, double timelag, int dimension,
			int numberOfSteps, AnomalousDiffusionScene scene, double driftVelocity, double driftAngleVelocity) {
		this.timelag = timelag;
		this.dimension = dimension;
		this.numberOfSteps = numberOfSteps;
		this.diffusioncoefficient = diffusioncoefficient;
		this.start = new Point3d(0, 0, 0);
		r = CentralRandomNumberGenerator.getInstance();
		this.scene = scene;
		ActiveTransportSimulator actSim = new ActiveTransportSimulator(driftVelocity, driftAngleVelocity, timelag, dimension, numberOfSteps);
		drift = actSim.generateTrajectory();
	}
	
	public void setStartPoint(double x,double y, double z){
		start = new Point3d(x, y, z);
	}
	
	public void setStartPoint(Point3d start){
		this.start = start;
	}
	
	public AnomalousDiffusionSimulator(double diffusioncoefficient, double timelag, int dimension,
			int numberOfSteps, AnomalousDiffusionScene scene) {
		this(diffusioncoefficient, timelag, dimension,
				numberOfSteps, scene,0,0);
	}
	
	public AnomalousDiffusionSimulator(AnomalousDiffusionScene scene) {
		this.scene = scene;
	}
	
	@Override
	public Trajectory generateTrajectory() {

		Trajectory t = new Trajectory(dimension);
		t.add(start);
		
		if(dimension==2 && scene.checkCollision(new double[]{start.x,start.y})) {
			
			throw new IllegalStateException("Start position has to be outside of an obstacle");
			
		}
		else if(dimension==3 && scene.checkCollision(new double[]{start.x,start.y,start.z})) {
			
			throw new IllegalStateException("Start position has to be outside of an obstacle");
			
		}

		for(int i = 1; i <= numberOfSteps; i++) {
			
			scene.updateObstaclePositions();
			double driftdx = drift.get(i).x-drift.get(i-1).x;
			double driftdy = drift.get(i).y-drift.get(i-1).y;
			double driftdz = drift.get(i).z-drift.get(i-1).z;
			double[] drift = {driftdx,driftdy,driftdz};
			Point3d pos = nextValidStep(t.get(t.size()-1),drift);
		//	pos.setX(pos.x);
		//	pos.setY(pos.y);
		//	pos.setZ(pos.z);
			t.add(pos);
			
		}
		
		return t;
	}
	
	private Point3d nextValidStep(Point3d lastPosition, double[] drift){
		double subTimelag = timelag/numberOfSubsteps;
	
		int takenSubsteps = 0;
		double[] nextPos = {lastPosition.x,lastPosition.y,lastPosition.z};
	
		while(takenSubsteps < numberOfSubsteps){
			double steplength = Math.sqrt(-2*dimension*diffusioncoefficient*subTimelag*Math.log(1-r.nextDouble()));
			Point3d pos = SimulationUtil.randomPosition(dimension,steplength);
		
			double[] candPos = {nextPos[0]+pos.x+drift[0],nextPos[1]+pos.y+drift[1],nextPos[2]+pos.z+drift[2]};
			if(scene.checkCollision(candPos)==false){
				for(int i = 0; i < candPos.length; i++){
					nextPos[i] = candPos[i];
				}
				
			}
			takenSubsteps++;
		}
		
		return new Point3d(nextPos);
		
	}

}
