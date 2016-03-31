package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * Summe Länge Einzelschritte / Distanz start-end punk
 *
 */
public class StraightnessFeature extends AbstractTrajectoryFeature {
	
	private Trajectory t;
	@Override
	public double[] evaluate() {
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			sum += t.get(i).distance(t.get(i-1));
		}
		double straightness = sum/(t.get(0).distance(t.get(t.size()-1)));
		return new double[]{straightness};
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
