[![DOI](https://zenodo.org/badge/18649/thorstenwagner/TraJ.svg)](https://zenodo.org/badge/latestdoi/18649/thorstenwagner/TraJ)
# TraJ
Java library for diffusion trajectory (2D) analysis

Implemented features so far:
- Covariance estimator for diffusion coefficient [1]
- Regression estimator for diffusion coefficient
- Stokes-Einstein converter to get hydrodynamic diameter
- Global linear drift calculator
- Static drift corrector
- Simulation: Brownian motion (free diffusion)
- Simulation: Active Transport
- Simulation: Confined diffusion
- Simulation: Anomalous diffusion with fixed obstacles (spheres)
- Simulation: Anomalous diffusion by weierstrass-mandelbrot approach [8]
- Trajectories are combineable
- Numerous unit tests to ensure correct functioning
- Trajectory characterization:
  - Center of gravity
  - Aspect ratio
  - Elongation
  - Fractal path dimension [2]
  - Mean squared displacment curve curvature [3]
  - Mean squared displacment
  - Short-time long-time diffusion coefficent ratio 
  - Exponent in power law fit to MSD curve [4]
  - Standard deviation in direction
  - Spline curve analysis features according to [5]
![Spline fit](https://dl.dropboxusercontent.com/u/560426/traj/splinefit.png "Spline fit")
  - Efficency [6]
  - Skeweness [6]
  - Kurtosis [6]
  - Asymmetry [7]
  - Trapped probability [7]
  - Maximum distance between two positions
  - Maximum distance for given timelag

  
References:

[1] C. L. Vestergaard, P. C. Blainey, and H. Flyvbjerg, “Optimal estimation of diffusion coefficients from single-particle trajectories,” Phys. Rev. E - Stat. Nonlinear, Soft Matter Phys., vol. 89, no. 2, p. 022726, Feb. 2014.

[2] M. J. Katz and E. B. George, “Fractals and the analysis of growth paths,” Bull. Math. Biol., vol. 47, no. 2, pp. 273–286, 1985.

[3] S. Huet, E. Karatekin, V. S. Tran, I. Fanget, S. Cribier, and J.-P. Henry, “Analysis of transient behavior in complex trajectories: application to secretory vesicle dynamics.,” Biophys. J., vol. 91, no. 9, pp. 3542–3559, 2006.

[4] D. Arcizet, B. Meier, E. Sackmann, J. O. Rädler, and D. Heinrich, “Temporal analysis of active and passive transport in living cells,” Phys. Rev. Lett., vol. 101, no. 24, p. 248103, Dec. 2008.

[5] Spatial structur analysis of diffusive dynamics according to: B. R. Long and T. Q. Vu, “Spatial structure and diffusive dynamics from single-particle trajectories using spline analysis,” Biophys. J., vol. 98, no. 8, pp. 1712–1721, 2010.

[6] Helmuth, J.A. et al., 2007. A novel supervised trajectory segmentation algorithm identifies distinct types of human adenovirus motion in host cells. Journal of structural biology, 159(3), pp.347–58.

[7] Saxton, M.J., 1993. Lateral diffusion in an archipelago. Single-particle diffusion. Biophysical Journal, 64(6), pp.1766–1780.

[8] Guigas, G. & Weiss, M., 2008. Sampling the Cell with Anomalous Diffusion—The Discovery of Slowness. Biophysical Journal, 94(1), pp.90–94.

To Do:
- Size distribution estimation for trajectory sets according to: J. G. Walker, “Improved nano-particle tracking analysis,” Meas. Sci. Technol., vol. 23, no. 6, p. 065605, Jun. 2012. (Already implemented in NanoTrackJ - I just have to port it)
- Simulation: Add anomalous diffusion with brownian motion obstacles and Ornstein-Uhlenbeck obstacles
