package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

public abstract class AbstractTrajectoryFeature {
	
	public abstract double[] evaluate();
	public abstract String getName();
	public abstract void setTrajectory(Trajectory t);

}
