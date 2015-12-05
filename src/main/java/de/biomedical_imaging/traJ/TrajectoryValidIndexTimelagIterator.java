/*
The MIT License (MIT)

Copyright (c) 2015 Thorsten Wagner (wagner@biomedical-imaging.de)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
