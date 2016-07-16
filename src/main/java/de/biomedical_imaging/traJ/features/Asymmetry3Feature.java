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

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traj.math.RadiusGyrationTensor2D;

/**
 * The features characterizes the asymetry of the trajectory. 
 * 
 * It was implemented according to
 * 
 * Helmuth, J.A. et al., 2007. 
 * A novel supervised trajectory segmentation algorithm identifies distinct types of human adenovirus motion in host cells. 
 * Journal of structural biology, 159(3), pp.347–58.
 * 
 * @author Thorsten Wagner
 *
 */
public class Asymmetry3Feature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	/**
	 * 
	 * @param t Trajectory for which the asymmetry is to be calculated.
	 */
	public Asymmetry3Feature(Trajectory t) {
		this.t = t;
	}
	
	/**
	 * @return Returns an double array with the following elements [0]=Asymmetry
	 */
	@Override
	public double[] evaluate() {
		Array2DRowRealMatrix gyr = RadiusGyrationTensor2D.getRadiusOfGyrationTensor(t);
		EigenDecomposition eigdec = new EigenDecomposition(gyr);
		double e1 = eigdec.getRealEigenvalue(0);
		double e2 = eigdec.getRealEigenvalue(1);
		double asym = -1*Math.log(1-Math.pow(e1-e2,2)/(2*Math.pow(e1+e2, 2)));
		result = new double[]{asym};

		return result;
	}

	@Override
	public String getName() {
		return "Assymetry3";
	}

	@Override
	public String getShortName() {
		return "ASYM3";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

}
