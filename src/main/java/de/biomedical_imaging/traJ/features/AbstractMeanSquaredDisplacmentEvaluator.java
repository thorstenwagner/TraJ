package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

public interface AbstractMeanSquaredDisplacmentEvaluator {
	
	public void setTimelag(int timelag);
	
	public void setTrajectory(Trajectory t);
	
	public double[] evaluate();
	
}
