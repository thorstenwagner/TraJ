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

import de.biomedical_imaging.traJ.Trajectory;

/**
 * 
 * Abstract trajectory feature. Should be the base class for all new features.
 * @author Thorsten Wagner
 * 
 */
public abstract class AbstractTrajectoryFeature {

	protected double[] result = null;
	
	/**
	 * @return return the result;
	 */
	public abstract double[] evaluate();
	
	/**
	 * @return Returns the result, but does not recalculate when it was calculated earlier
	 */
	public double[] getValue(){
		if(result==null){
			result = evaluate();
		}
		return result;
	}
	/**
	 * 
	 * @return The name of the feature
	 */
	public abstract String getName();
	/**
	 * Short name of the feature. Should not contain any spaces.
	 * @return A shortened name of the feature
	 */
	public abstract String getShortName();
	public abstract void setTrajectory(Trajectory t);
	
	

}
