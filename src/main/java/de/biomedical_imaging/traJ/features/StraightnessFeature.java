package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * Distanz start-end punk / Summe Länge Einzelschritte 
 *
 */
public class StraightnessFeature extends AbstractTrajectoryFeature {
	
	private Trajectory t;
	
	public StraightnessFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		
		return new double[]{getStraightness()};
	}
	
	public double getStraightness(){
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			sum += t.get(i).distance(t.get(i-1));
		}
		if(sum<Math.pow(10, -10)){
			return 0;
		}
		double straightness = (t.get(0).distance(t.get(t.size()-1)))/sum;
		return straightness;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Straightness";
	}

	@Override
	public String getShortName() {
		
		return "STRAIGHTNESS";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
