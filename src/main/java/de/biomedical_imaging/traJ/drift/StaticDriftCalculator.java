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

package de.biomedical_imaging.traJ.drift;

import java.util.ArrayList;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectoryValidIndexTimelagIterator;

public class StaticDriftCalculator<T extends Trajectory>  {
	
	/**
	 * Calculates the static drift. Static means, that the drift does not change direction or intensity over time.
	 * 
	 * @param tracks Tracks which seems to exhibit a local drift
	 * @return The static drift over all trajectories
	 */
	public double[] calculateDrift(ArrayList<T> tracks){
		double[] result = new double[3];
		
		double sumX =0;
		double sumY = 0;
		double sumZ = 0;
		int N=0;
		for(int i = 0; i < tracks.size(); i++){
			T t = tracks.get(i);
			TrajectoryValidIndexTimelagIterator it = new TrajectoryValidIndexTimelagIterator(t,1);
	
			//for(int j = 1; j < t.size(); j++){
			while(it.hasNext()) {
				int j = it.next();
				sumX += t.get(j+1).x - t.get(j).x;
				sumY += t.get(j+1).y - t.get(j).y;
				sumZ += t.get(j+1).z - t.get(j).z;
				N++;
			}
		}
		result[0] = sumX/N;
		result[1] = sumY/N;
		result[2] = sumZ/N;
		return result;
	}

}
