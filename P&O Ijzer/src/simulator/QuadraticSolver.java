package simulator;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that handles solving quadratic equations of the form ax² + bx + c.
 * @author Thomas Vochten
 *
 */

public class QuadraticSolver {

	/**
	 * Returns a solver initialised with the given values for the equation parameters.
	 * 
	 * @param a
	 *        The coefficient corresponding to the quadratic term.
	 * @param b
	 *        The coefficient corresponding to the linear term.
	 * @param c
	 *        The coefficient corresponding to the constant term.
	 */
	public QuadraticSolver(double a, double b, double c)
	{
		this.a = a;
		this.b = b;
		this.c = c;

		this.discriminant = Math.pow(this.b, 2) - 4 * this.a * this.c;
		if (this.discriminant >= 0)
		{
			// Credit to the authors of the book "Numerical Recipes in C" for this numerically stable method.
			double q = 0;
			if (b == 0) {
				q = -(1.0/2.0) * (Math.sqrt(this.discriminant));
			}
			else {
				q = -(1.0/2.0) * (this.b + Math.signum(this.b) * Math.sqrt(this.discriminant));
			}
			this.x1 = q/this.a;
			this.x2 = this.c/q;
		}
		else { this.x1 = 0; this.x2 = 0; };
	}

	/**
	 * The equation variables.
	 */
	private final double a, b, c;

	/**
	 * Returns the coefficient corresponding to the quadratic term.
	 */
	public double getA()
	{
		return this.a;
	}

	/**
	 * Returns the coefficient corresponding to the linear term.
	 */
	public double getB()
	{
		return this.b;
	}

	/**
	 * Returns the coefficient corresponding to the constant term.
	 */
	public double getC()
	{
		return this.c;
	}

	/**
	 * The discriminant.
	 */
	private final double discriminant;

	/**
	 * Returns the discriminant.
	 */
	public double getDiscriminant()
	{
		return this.discriminant;
	}

	/**
	 * The solutions in x.
	 */
	private final double x1, x2;

	/**
	 * Returns the solutions of this quadratic equation in the form of a list of doubles.
	 * 
	 * @return An empty list if the discriminant is less than 0.
	 * @return A list containing one number if the discriminant is equal to 0.
	 * @return A list of two numbers that are not equal to each other if the discriminant is equal to 0.
	 */
	public List<Double> getSolutions()
	{
		ArrayList<Double> toReturn = new ArrayList<Double>(2);
		if (this.getDiscriminant() < 0)
		{
			return toReturn;
		}
		else if (this.getDiscriminant() == 0)
		{
			toReturn.add(this.x1);
			return toReturn;
		}
		else
		{
			toReturn.add(this.x1); toReturn.add(this.x2);
			return toReturn;
		}
	}


}
