package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectoryValidIndexTimelagIterator;

public class QuartricMomentFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private int timelag;
	private String shortname = "QMOMENT";
	
	public QuartricMomentFeature(Trajectory t, int timelag){
		this.t = t;
		this.timelag = timelag;
	}
	@Override
	public double[] evaluate() {
		double sum =0;
		TrajectoryValidIndexTimelagIterator it = new TrajectoryValidIndexTimelagIterator(t, timelag);
		int N = 0;
		while(it.hasNext()){
			int i = it.next();
			sum = sum + 
					Math.pow(t.get(i).getX()-t.get(i+timelag).getX(),4) + 
					Math.pow(t.get(i).getY()-t.get(i+timelag).getY(),4) +
					Math.pow(t.get(i).getZ()-t.get(i+timelag).getZ(),4);
			N++;
		}
		
		return new double[] {sum/N};
	}
	
	public void setTimelag(int timelag){
		this.timelag = timelag;
	}

	@Override
	public String getName() {
		return "Quartric Moment";
	}

	@Override
	public String getShortName() {
		return shortname;
	}
	
	public void setShortName(String name){
		shortname = name;
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
