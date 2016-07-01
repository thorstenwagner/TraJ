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

package de.biomedical_imaging.traJ;


/**
 * Converts an diffusion coefficient into the hydrodynamic diameter
 * @author Thorsten Wagner (wagner at biomedical minus imaging dot de)
 *
 */
public class StokesEinsteinConverter {
	private final static double kB = 1.3806488* Math.pow(10, -23);  // Bolzmann Constant in [J K^-1] bzw. [kg m^2 s^-2 K^-1]
	
	public static enum SOLVENT {WATER,BENZENE,ACETONE,ETHANOL};
	/**
	 * Converters the diffusion coefficient to hydrodynamic diameter and vice versa
	 * @param value Diffusion coefficient in [m^2 s^-1] or hydrodynamic diameter in [m]
	 * @param temperatur Temperatur in [Kelvin]
	 * @param viscosity Viscosity in [kg m^-1 s^-1]
	 * @return Hydrodynmaic diameter [m] / diffusion coefficient [m^2 s^-1]
	 */
	public double convert(double value, double temperatur, double viscosity){
		return temperatur*kB / (Math.PI*viscosity*value);
	}
	
	/**
	 * Calculates the viscosity for different solvent in dependence from the temperatur [k]. Please see
	 * https://de.wikipedia.org/wiki/Andrade-Gleichung
	 * @param temperatur Temperatur in Kelvin [K]
	 * @return Viscosity in [kg m^-1 s^-1]
	 */
	public static double ViscosityByAndradeEquation(double temperatur, SOLVENT s){
		double res = 0;
		double a;
		double b;
		switch (s) {
		case WATER:
			if(temperatur<274 || temperatur>373){
				throw new IllegalArgumentException("The andrade equation can not be applied for this solvent/temperatur combination.");
			}
			a = -6.944;
			b = 2036.8;
			
			res = Math.exp(a+b/temperatur)*Math.pow(10, -3);
			break;
		case BENZENE:
			if(temperatur<273 || temperatur>483){
				throw new IllegalArgumentException("The andrade equation can not be applied for this solvent/temperatur combination.");
			}
			a = -4.825;
			b = 1289.2;
			
			res = Math.exp(a+b/temperatur)*Math.pow(10, -3);
			break;
		case ACETONE:
			if(temperatur<183 || temperatur>329){
				throw new IllegalArgumentException("The andrade equation can not be applied for this solvent/temperatur combination.");
			}
			a = -4.003;
			b = 842.5;
			
			res = Math.exp(a+b/temperatur)*Math.pow(10, -3);
			break;
		case ETHANOL:
			if(temperatur<163 || temperatur>516){
				throw new IllegalArgumentException("The andrade equation can not be applied for this solvent/temperatur combination.");
			}
			a = -5.878;
			b = 1755.8;
			
			res = Math.exp(a+b/temperatur)*Math.pow(10, -3);
			break;
		default:
			break;
		}
		return res;
	}
	
	/**
	 * Converts celsius in kelvin
	 * @param tempInCelsius Temperture in celsius
	 * @return Temperatur in kelvin
	 */
	public static double convertCelsiusToKelvin(double tempInCelsius){
		return tempInCelsius + 273;
	}

	/**
	 * Converts kelvin to celsius
	 * @param tempInCelsius Temperture in kelvin
	 * @return Temperatur in celsius
	 */
	public static double convertKelvinToCelsius(double tempInKelvin){
		return tempInKelvin - 273;
	}
	
	
}
