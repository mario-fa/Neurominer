/*
 * HTCoordE.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001-2003 Christophe Bouthier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package hypertree;


/**
 * The HTCoordE class implements the coordinates of a point
 * in the Euclidian space.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTCoordE {

    private static final double EPSILON = 1.0E-10; // epsilon

    double x = 0.0; // x coord
    double y = 0.0; // y coord


  /* --- Constructor --- */

    /**
     * Constructor.
     * x = 0 and y = 0.
     */
    HTCoordE() {}

    /**
     * Constructor copying the given euclidian point.
     *
     * @param z    the euclidian point to copy 
     */
    HTCoordE(HTCoordE z) {
        this.copy(z);
    }

    /**
     * Constructor fixing x and y.
     *
     * @param x    the x coord
     * @param y    the y coord
     */
    HTCoordE(double x, double y) {
        this.x = x;
        this.y = y;
    }


  /* --- Copy --- */

    /**
     * Copy the given HTCoordE into this HTCoordE.
     *
     * @param z    the HTCoordE to copy
     */
    void copy(HTCoordE z) {
        this.x = z.x;
        this.y = z.y;
    }


  /* --- Projections --- */

    /**
     * Progects from Screen to Euclidian.
     * 
     * @param x        the x screen coordinate
     * @param y        the y screen coordinate
     * @param sOrigin   the origin of the screen plane
     * @param sMax      the (xMax, yMax) point in the screen plane
     */
    void projectionStoE(int x, int y, HTCoordS sOrigin, HTCoordS sMax) {
        this.x = (double) (x - sOrigin.x) / (double) sMax.x;
        this.y = -((double) (y - sOrigin.y) / (double) sMax.y);
    } 


  /* --- Validation --- */

    /**
     * Is this coordinate in the hyperbolic disc ?
     *
     * @return    <CODE>true</CODE> if this point is in;
     *            <CODE>false</CODE> otherwise
     */
    boolean isValid() {
        return (this.d2() < 1.0);
    }
 

  /* --- Transformation --- */

    /*
     * Some complex computing formula :
     *
     * arg(z)  = atan(y / x) if x > 0
     *         = atan(y / x) + Pi if x < 0
     *
     * d(z)    = Math.sqrt((z.x * z.x) + (z.y * z.y)) 
     *
     * conj(z) = | z.x
     *           | - z.y
     *
     * a * b   = | (a.x * b.x) - (a.y * b.y)
     *           | (a.x * b.y) + (a.y * b.x)
     *
     * a / b   = | ((a.x * b.x) + (a.y * b.y)) / d(b)
     *           | ((a.y * b.x) - (a.x * b.y)) / d(b)
     */
     
    /**
     * Conjugate the complex.
     */
    void conjugate() {
        y = -y;
    }

    /**
     * Multiply this coordinate by the given coordinate.
     *
     * @param z    the coord to multiply with
     */
    void multiply(HTCoordE z) {
        double tx = x;
        double ty = y;
        x = (tx * z.x) - (ty * z.y);
        y = (tx * z.y) + (ty * z.x);
    }
    
    /**
     * Divide this coordinate by the given coordinate.
     *
     * @param z    the coord to divide with
     */
    void divide(HTCoordE z) {
        double d = z.d2();
        double tx = x;
        double ty = y;
        x = ((tx * z.x) + (ty * z.y)) / d;
        y = ((ty * z.x) - (tx * z.y)) / d;
    }

    /**
     * Substracts the second coord to the first one
     * and put the result in this HTCoorE
     * (this = a - b).
     *
     * @param a    the first coord
     * @param b    the second coord
     */
    void sub(HTCoordE a, HTCoordE b) {
        x = a.x - b.x;
        y = a.y - b.y;
    }

    /**
     * Returns the angle between the x axis and the line
     * passing throught the origin O and this point.
     * The angle is given in radians.
     *
     * @return    the angle, in radians
     */
    double arg() {
        double a = Math.atan(y / x);
        if (x < 0) {
            a += Math.PI;
        } else if (y < 0) {
            a += 2 * Math.PI;
        }
        return a;
    }

    /**
     * Returns the square of the distance from the origin 
     * to this point.
     *
     * @return    the square of the distance
     */
    double d2() {
        double d2 = (x * x) + (y * y);
        if (d2 > 1.0) {
            d2 = 1.0;
        }
        return d2;
    }

    /**
     * Returns the distance from the origin 
     * to this point.
     *
     * @return    the distance
     */
    double d() {
        return Math.sqrt(d2());
    }

    /**
     * Returns the distance from this point
     * to the point given in parameter.
     *
     * @param p    the other point
     * @return     the distance between the 2 points
     */
    double d(HTCoordE p) {
        return Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y));
    }

    /**
     * Translate this Euclidian point 
     * by the coordinates of the given Euclidian point.
     * 
     * @param t    the translation coordinates
     */
    void translate(HTCoordE t) {
        // z = (z + t) / (1 + z * conj(t))
        
        // first the denominator
        double denX = (x * t.x) + (y * t.y) + 1.0;
        double denY = (y * t.x) - (x * t.y) ;    

        // and the numerator
        double numX = x + t.x;
        double numY = y + t.y;

        // then the division (bell)
        double dd = (denX * denX) + (denY * denY);
        x = ((numX * denX) + (numY * denY)) / dd;
        y = ((numY * denX) - (numX * denY)) / dd;
    }

    HTCoordE pToK() {
        HTCoordE k = new HTCoordE();
        double d = 2.0 / (1.0 + d2());
        k.x = (d * x);
        k.y = (d * y);
        return k;
    }

    HTCoordE kToP() {
        HTCoordE k = new HTCoordE();
        double d = 1.0 / (1.0 + Math.sqrt(1 - d2()));
        k.x = (d * x);
        k.y = (d * y);
        return k;
    }

    /**
     * Special transformation, optimized.
     * 
     * @param alpha    first member
     * @param beta     second member
     */
    void specialTrans(HTCoordE alpha, HTCoordE beta) {
        // z = (alpha * z + beta) / (conj(alpha) + conj(beta)*z)
 
        double dx = (this.x * beta.x) + (this.y * beta.y) + alpha.x;
        double dy = (this.y * beta.x) - (this.x * beta.y) - alpha.y;
        double d = (dx * dx) + (dy * dy);
        
        double tx = (this.x * alpha.x) - (this.y * alpha.y) + beta.x;
        double ty = (this.x * alpha.y) + (this.y * alpha.x) + beta.y;

        x = ((tx * dx) + (ty * dy)) / d;
        y = ((ty * dx) - (tx * dy)) / d;
    }


  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        return "(" + x + " : " + y + ")E";
    }

}

