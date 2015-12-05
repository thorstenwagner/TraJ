package de.biomedical_imaging.traJ;

import java.util.Iterator;

/**
 * This iterator iterates over a trajectory. It returns the index i where i and i+timelag is valid for that track.
 * This iterator is especally helpfull, when the trajectory contain gaps (null positions).
 * @author Thorsten Wagner
 *
 */
public class TrajectoryValidIndexTimelagIterator implements Iterator<Integer>{

	private Trajectory t;
	private int timelag;
	boolean overlap = true;
	int currentIndex;
	
	/**
	 * @param t The trajectory
	 * @param timelag The timelag
	 */
	public TrajectoryValidIndexTimelagIterator(Trajectory t, int timelag) {
		this.t = t;
		this.timelag = timelag;
		this.overlap = true;
		currentIndex = 0;
	}
	
	/**
	 * @param t The trajectory
	 * @param timelag The timelag
	 * @param overlap True when valid positions are allowed to overlap
	 */
	public TrajectoryValidIndexTimelagIterator(Trajectory t, int timelag, boolean overlap) {
		this.t = t;
		this.timelag = timelag;
		this.overlap = overlap;
		currentIndex = 0;
		
	}
	public boolean hasNext() {
		for(int i = currentIndex; i < t.getPositions().size(); i++){
			if(i+timelag>=t.getPositions().size()){
				return false;
			}
			if((t.getPositions().get(i) != null) && (t.getPositions().get(i+timelag) != null)){
				return true;
			}
		}
		return false;
	}

	public Integer next() {
		for(int i = currentIndex; i < t.getPositions().size(); i++){
			if(i+timelag>=t.getPositions().size()){
				return null;
			}
			if((t.getPositions().get(i) != null) && (t.getPositions().get(i+timelag) != null)){
				if(overlap){
					currentIndex = i+1;
				}
				else{
					currentIndex = i+timelag;
				}
				return i;
			}
		}
		return null;
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
