package de.biomedical_imaging.traJ;

import de.biomedical_imaging.traJ.features.AbstractTrajectoryFeature;

public class FeatureWorker extends Thread {
	public enum EVALTYPE{
		FIRST,RATIO_01,RATIO_10,RATIO_12;
	}
	double[] result;
	AbstractTrajectoryFeature c;
	EVALTYPE ev;
	int resIndex;
	
	public FeatureWorker(double[] result, int resIndex, AbstractTrajectoryFeature c, EVALTYPE ev) {
		this.result = result;
		this.c = c;
		this.ev =ev;
		this.resIndex = resIndex;
	}
	
	@Override
	public void run() {
		double[] res ;
			switch (ev) {
			case FIRST:
				result[resIndex] = c.evaluate()[0];
				break;
			case RATIO_01:
				res = c.evaluate();
				result[resIndex] = res[0]/res[1];
				break;
			case RATIO_10:
				res = c.evaluate();
				result[resIndex] = res[1]/res[0];
				break;
			case RATIO_12:
				res = c.evaluate();
				result[resIndex] = res[1]/res[2];
				break;

			default:
				break;
			}
			
		}

}
