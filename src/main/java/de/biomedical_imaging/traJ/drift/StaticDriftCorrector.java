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

import javax.vecmath.Point3d;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * 
 * @author Thorsten Wagner
 *
 */
public class StaticDriftCorrector extends AbstractDriftCorrector {

	private double[] drift;

	/**
	 * 
	 * @param drift An array with the elements [0] = Drift in x direction, [1] = Drift in y direction, [2] = Drift in z direction
	 */
	public StaticDriftCorrector(double[] drift) {
		this.drift = drift;
	}

	@Override
	public Trajectory removeDrift(Trajectory t) {
		Trajectory tNew = new Trajectory(t.getDimension());
		tNew.add(new Point3d(t.get(0).x, t
				.get(0).y, t.get(0).z));

		for (int i = 1; i < t.size(); i++) {
			if (t.get(i) != null) {
				tNew.add(new Point3d(t.get(i).x - i
						* drift[0], t.get(i).y - i * drift[1], t
						.get(i).z - i * drift[2]));
			} else {
				tNew.add(null);
			}

		}
		return tNew;
	}

}
