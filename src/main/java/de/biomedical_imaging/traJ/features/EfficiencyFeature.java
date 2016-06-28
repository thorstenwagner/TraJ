/*
 * The MIT License (MIT)
 * Copyright (c) 2015-2016 Thorsten Wagner (wagner@biomedical-imaging.de)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * 
 * Relates the squared net dispalcement to the sum of squared step lengths
 * 
 * Based on:
 * Helmuth, J.A. et al., 2007. 
 * A novel supervised trajectory segmentation algorithm identifies distinct types of human adenovirus motion in host cells. 
 * Journal of structural biology, 159(3), pp.347–58.
 */
public class EfficiencyFeature extends AbstractTrajectoryFeature {
	
	private Trajectory t;
	
	public EfficiencyFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		
		return new double[]{getEfficiency()};
	}
	
	public double getEfficiency(){
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			double d = t.get(i).distance(t.get(i-1));
			sum += d*d;
		}
		if(sum<Math.pow(10, -10)){
			return 0;
		}
		double d = t.get(0).distance(t.get(t.size()-1));
		double eff = (d*d)/(t.size()*sum);
		return eff;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Efficiency";
	}

	@Override
	public String getShortName() {
		
		return "EFFICENCY";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
