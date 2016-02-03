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

import java.util.ArrayList;

public class AnomalousDiffusionScene {
	
	private ArrayList<AbstractSphereObstacle> obstacles;
	private double[] size;
	private int dimension; 
	private int nRandPoints;
	private double[] randomNumbers = null;
	boolean recalculateVolumeFraction = true;
	boolean recalculateProbNonInteraction = true;
	private double fraction;
	private double probNonInteraction;
	public AnomalousDiffusionScene(double[] size, int dimension) {
		obstacles = new ArrayList<AbstractSphereObstacle>();
		this.size = size;
		this.dimension = dimension;
		nRandPoints = (int)Math.pow(10,5);
	}
	
	public void addObstacle(AbstractSphereObstacle o){
		if(o.insideSzeneBoundaries(this)){
			obstacles.add(o);
			recalculateVolumeFraction = true;
			recalculateProbNonInteraction = true;
		}
		else{
			throw new IllegalStateException("The position of the obstacle is not inside the scene boundaries.");
		}
	}
	
	public void updateObstaclePositions(){
		for (AbstractSphereObstacle o : obstacles) {
			o.updatePosition(this);
			recalculateVolumeFraction = true;
			recalculateProbNonInteraction = true;
		}
	}
	
	 
	/**
	 * Estimate excluded volume fraction by monte carlo method
	 * @return
	 */
	public double estimateExcludedVolumeFraction(){
		//Calculate volume/area of the of the scene without obstacles
		if(recalculateVolumeFraction){
			CentralRandomNumberGenerator r = CentralRandomNumberGenerator.getInstance();
			boolean firstRandomDraw = false;
			if(randomNumbers==null){
				randomNumbers = new double[nRandPoints*dimension];
				firstRandomDraw = true;
			}
			int countCollision = 0;
			for(int i = 0; i< nRandPoints; i++){
				double[] pos = new double[dimension];
				for(int j = 0; j < dimension; j++){
					if(firstRandomDraw){
						randomNumbers[i*dimension + j] = r.nextDouble();
					}
					pos[j] = randomNumbers[i*dimension + j]*size[j];
				}
				if(checkCollision(pos)){
					countCollision++;
				}
			}
			fraction = countCollision*1.0/nRandPoints;
			recalculateVolumeFraction = false;
		}
		return fraction;
	}
	
	public double estimateProbNonInteraction(double radius){
		
		if(recalculateProbNonInteraction){
			CentralRandomNumberGenerator r = CentralRandomNumberGenerator.getInstance();
			boolean firstRandomDraw = false;
			if(randomNumbers==null){
				randomNumbers = new double[nRandPoints*dimension];
				firstRandomDraw = true;
			}
			int countCollision = 0;
			for(int i = 0; i< nRandPoints; i++){
				double[] rpos = new double[dimension];
				for(int j = 0; j < dimension; j++){
					if(firstRandomDraw){
						randomNumbers[i*dimension + j] = r.nextDouble();
					}
					rpos[j] = randomNumbers[i*dimension + j]*size[j];
				}
				for( int k = 0; k < obstacles.size(); k++){
					double maxrad = Math.max(radius, obstacles.get(k).getRadius());
					double[] pos = obstacles.get(k).getPosition();
					if( (Math.pow(rpos[0]-pos[0],2)+Math.pow(rpos[1]-pos[1],2))<Math.pow(maxrad, 2)){
						countCollision++;
						k = obstacles.size();
					}
				}
			}
			probNonInteraction = 1- countCollision*1.0/nRandPoints;
			recalculateProbNonInteraction = false;
		}
		
		return probNonInteraction;
	}
	
	public boolean checkCollision(double[] pos){
		double[] inScene = convertToScenePosition(pos);
		for (AbstractSphereObstacle o : obstacles) {
			
			if(o.isInside(inScene)){
				return true;
			}
		}
		return false;
	}
	
	public double[] convertToScenePosition(double[] pos){
		double[] pPos = new double[pos.length];

		for(int i = 0; i < dimension; i++){
			pPos[i] = pos[i];
			while(pPos[i] < 0){
				pPos[i]+=size[i];
			}
			while(pPos[i] >= size[i]){
				pPos[i]-=size[i];
			}
		}
		return pPos;
		
	}
	
	public ArrayList<AbstractSphereObstacle> getObstacles(){
		return obstacles;
	}
			
	public double[] getSize(){
		return size;
	}
	
	

}
