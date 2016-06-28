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
 * The features characterizes  the asymetry of the trajectory. 
 * It is equal to 0 for circularly symmetric trajectories and 
 * 1 for linear trajectories.
 * 
 * It was implemented according to
 * 
 * Saxton, M.J., 1993. Lateral diffusion in an archipelago. 
 * Single-particle diffusion. Biophysical Journal, 64(6), pp.1766–1780.
 * @author Thorsten Wagner
 *
 */
public class AsymmetryFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	
	public AsymmetryFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		Array2DRowRealMatrix gyr = RadiusGyrationTensor2D.getRadiusOfGyrationTensor(t);
		EigenDecomposition eigdec = new EigenDecomposition(gyr);
		double e1 = eigdec.getRealEigenvalue(0);
		double e2 = eigdec.getRealEigenvalue(1);
		
		double asym = Math.pow(e1*e1-e2*e2, 2)/(Math.pow(e1*e1+e2*e2, 2)); //-1*Math.log(1-Math.pow(e1-e2, 2)/(2*Math.pow(e1+e2, 2)));
		return new double[]{asym};
	}

	@Override
	public String getName() {
		return "Assymetry";
	}

	@Override
	public String getShortName() {
		return "ASYM";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
