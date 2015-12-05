package de.biomedical_imaging.traJ;

import java.util.ArrayList;

public class GlobalDriftCalculator {
	
	public double[] calculateDrift(ArrayList<Trajectory> tracks){
		double[] result = new double[3];
		
		double sumX =0;
		double sumY = 0;
		double sumZ = 0;
		int N=0;
		for(int i = 0; i < tracks.size(); i++){
			Trajectory t = tracks.get(i);
			TrajectoryValidIndexTimelagIterator it = new TrajectoryValidIndexTimelagIterator(t,1);
	
			//for(int j = 1; j < t.getPositions().size(); j++){
			while(it.hasNext()) {
				int j = it.next();
				sumX += t.getPositions().get(j+1).x - t.getPositions().get(j).x;
				sumY += t.getPositions().get(j+1).y - t.getPositions().get(j).y;
				sumZ += t.getPositions().get(j+1).z - t.getPositions().get(j).z;
				N++;
			}
		}
		result[0] = sumX/N;
		result[1] = sumY/N;
		result[2] = sumZ/N;
		return result;
	}

}
