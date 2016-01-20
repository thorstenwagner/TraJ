[![DOI](https://zenodo.org/badge/18649/thorstenwagner/TraJ.svg)](https://zenodo.org/badge/latestdoi/18649/thorstenwagner/TraJ)
# TraJ
Java library for diffusion trajectory analysis

Implemented features so far:
- Compatible with 1D, 2D and 3D trajectories.
- Covariance estimator for diffusion coefficient
- Regression estimator for diffusion coefficient
- Stokes-Einstein converter to get hydrodynamic diameter
- Global linear drift calculator
- Static drift corrector
- Simulation: Brownian motion (free diffusion)
- Simulation: Active Transport
- Simulation: Confined diffusion
- Trajectories are combineable
- Numerous unit tests to ensure correct functioning

Trajectory characterization:
- Aspect ratio
- Elongation
- Fractal dimension
- Mean squared displacment curve curvature
- Mean squared displacment
- Exponent in power law fit to MSD curve
- Standard deviation in direction

To Do:
- Size distribution estimation for trajectory sets according to: J. G. Walker, “Improved nano-particle tracking analysis,” Meas. Sci. Technol., vol. 23, no. 6, p. 065605, Jun. 2012.
- Spatial structur analysis of diffusive dynamics according to: B. R. Long and T. Q. Vu, “Spatial structure and diffusive dynamics from single-particle trajectories using spline analysis,” Biophys. J., vol. 98, no. 8, pp. 1712–1721, 2010.
- Add path dimension according to: M. J. Katz and E. B. George, “Fractals and the analysis of growth paths,” Bull. Math. Biol., vol. 47, no. 2, pp. 273–286, 1985.
