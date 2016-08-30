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
 * Implements the fractal path dimension according to
 * M. J. Katz and E. B. George, “Fractals and the analysis of growth paths,” Bull. Math. Biol., vol. 47, no. 2, pp. 273–286, 1985.
 * 
 * It takes between around 1 for directed trajectories, values around 2 and values around 3 for confined or subdiffusion.
 * @author Thorsten Wagner
 *
 */
public class FractalDimensionFeature extends AbstractTrajectoryFeature{

	Trajectory t;
	/**
	 * 
	 * @param t Trajectory for which the fractal dimension is to be calculated.
	 */
	public FractalDimensionFeature(Trajectory t) {
		this.t = t;
		if(t.getDimension() != 2){
			throw new IllegalArgumentException("The fractal dimension feature only supoorts planer (2D) trajetorys"); 
		}
	}
	
	/**
	 * @return Returns an double array with the elements [0] = fractal path dimension
	 */
	@Override
	public double[] evaluate() {
		double largestDistance = Double.MIN_VALUE;
		double totalLength = 0;
		for(int i = 0; i < t.size(); i++){
			for(int j = i+1; j < t.size(); j++){
				double d = t.get(i).distance(t.get(j));
				if(d>largestDistance){
					largestDistance = d;
				}
			}
			if(i>0){
				totalLength += t.get(i).distance(t.get(i-1));
			}
		}
	
		double n = t.size()-1;
		double fractalDImension = Math.log(n)/(Math.log(n)+Math.log(largestDistance/totalLength));
		result = new double[] {fractalDImension};

	
		return result;
	}

	@Override
	public String getName() {
		return "Fractal Dimension";
	}
	
	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		if(t.getDimension() != 2){
			throw new IllegalArgumentException("The fractal dimension feature only supoorts planer (2D) trajetorys"); 
		}
		
	}

	@Override
	public String getShortName() {
		return "FD";
	}

}
