package de.biomedical_imaging.traJ.simulation;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectoryUtil;

public class CombinedSimulator extends AbstractSimulator {

	private AbstractSimulator sim1;
	private AbstractSimulator sim2;
	
	public CombinedSimulator(AbstractSimulator sim1, AbstractSimulator sim2) {
		this.sim1 =sim1;
		this.sim2 = sim2;
	}
	
	@Override
	public Trajectory generateTrajectory() {
		Trajectory t = TrajectoryUtil.combineTrajectory(sim1.generateTrajectory(), sim2.generateTrajectory());
		return t;
	}

}
