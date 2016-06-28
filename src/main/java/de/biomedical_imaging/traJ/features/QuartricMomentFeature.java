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
