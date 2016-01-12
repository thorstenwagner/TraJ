package de.biomedical_imaging.traJ.simulation;

import java.util.ArrayList;

public class AnomalousDiffusionScene {
	
	private ArrayList<AbstractSphereObstacle> obstacles;
	private double[] size;
	private int dimension; 
	private int nRandPoints;
	private double[] randomNumbers = null;
	
	public AnomalousDiffusionScene(double[] size, int dimension) {
		obstacles = new ArrayList<AbstractSphereObstacle>();
		this.size = size;
		this.dimension = dimension;
		nRandPoints = (int)Math.pow(10,5);
	}
	
	public void addObstacle(AbstractSphereObstacle o){
		if(o.insideSzeneBoundaries(this)){
			obstacles.add(o);
		}
		else{
			throw new IllegalStateException("The position of the obstacle is not inside the scene boundaries.");
		}
	}
	
	public void updateObstaclePositions(){
		for (AbstractSphereObstacle o : obstacles) {
			o.updatePosition(this);
		}
	}
	
	 
	/**
	 * Estimate excluded volume fraction by monte carlo method
	 * @return
	 */
	public double estimateExcludedVolumeFraction(){
		//Calculate volume/area of the of the scene without obstacles
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
		
		
		return countCollision*1.0/nRandPoints;
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
