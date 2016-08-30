/*
The MIT License (MIT)

Copyright (c) 2015-2016 Thorsten Wagner (wagner@biomedical-imaging.de)

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

package de.biomedical_imaging.traJ.features;


import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.scijava.vecmath.Vector3d;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traJ.TrajectoryUtil;

/**
 * Implements the standard deviation of the trajectory direction according to 
 * [1] D. Arcizet, B. Meier, E. Sackmann, J. O. Rädler, and D. Heinrich, 
 * “Temporal analysis of active and passive transport in living cells,” 
 * Phys. Rev. Lett., vol. 101, no. 24, p. 248103, Dec. 2008.
 * @author Thorsten Wagner
 *
 */
public class StandardDeviationDirectionFeature extends AbstractTrajectoryFeature {
	private Trajectory t;
	private int timelag;

	public StandardDeviationDirectionFeature(Trajectory t, int timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	
	@Override
	public double[] evaluate() {
		StandardDeviation sd = new StandardDeviation();
		double[] values = new double[t.size()-timelag-1];
		double subx = 0;
		double suby = 0;
		double subz = 0;

		for(int i = timelag+1; i < t.size(); i++){
			
				subx = t.get(i-timelag-1).x;
				suby = t.get(i-timelag-1).y;
				subz = t.get(i-timelag-1).z;
			
			Vector3d v1 = new Vector3d(t.get(i-timelag).x-subx,t.get(i-timelag).y-suby,t.get(i-timelag).z-subz);
			
			subx = t.get(i-1).x;
			suby = t.get(i-1).y;
			subz = t.get(i-1).z;
			Vector3d v2 = new Vector3d(t.get(i).x-subx,t.get(i).y-suby,t.get(i).z-subz);
		
			double v = v1.angle(v2);
			
			boolean v1IsZero = TrajectoryUtil.isZero(v1.x) && TrajectoryUtil.isZero(v1.y) && TrajectoryUtil.isZero(v1.z);  
			boolean v2IsZero = TrajectoryUtil.isZero(v2.x) && TrajectoryUtil.isZero(v2.y) && TrajectoryUtil.isZero(v2.z);  
			if(v1IsZero || v2IsZero){
				v = 0;
			}
			values[i-timelag-1] = v;
			//System.out.println("da " + v1.angle(v2));
		}
	
		sd.setData(values);
		result = new double[]{sd.evaluate()};

		return result;
	}

	@Override
	public String getName() {
		return "Standard deviation direction-dt-"+timelag;
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "DIR-SD";
	}

}
