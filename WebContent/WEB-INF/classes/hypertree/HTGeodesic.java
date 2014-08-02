/*
 * HTGeodesic.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001 Christophe Bouthier
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.QuadCurve2D;


/**
 * The HTGeodesic class implements a geodesic 
 * linking to points in the Poincarre model.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTGeodesic {

    private static final double EPSILON = 1.0E-10; // epsilon

    private static final int    LINE    = 0;       // draw a line
    private static final int    ARC     = 1;       // draw an arc

    private int      type = LINE; // type of the geodesic

    private HTCoordE za   = null; // first point (Euclidian)
    private HTCoordE zb   = null; // second point (Euclidian)
    private HTCoordE zc   = null; // control point (Euclidian)
    private HTCoordE zo   = null; // center of the geodesic;

    //CURVE
    /*
    private double  r     = 0.0;  // ray of the geod
    private double  alpha = 0.0;  // alpha factor
    private double  beta  = 0.0;  // beta factor

    private int     n     = 10;   // numer of segment in the approximation
    private HTCoordE[] tz = new HTCoordE[(2 * n) + 1];
    private HTCoordS[] ts = new HTCoordS[(2 * n) + 1];
    */
    //CURVE

    private HTCoordS a    = null; // first point (on the screen)
    private HTCoordS b    = null; // second point (on the screen)
    private HTCoordS c    = null; // control point (on the screen)

    private int      d    = 0;

    private boolean  kleinMode = false;

    //QUAD
    private QuadCurve2D curve = new QuadCurve2D.Double(); 
    //QUAD


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param za       the first point
     * @param zb       the second point
     */
    HTGeodesic(HTCoordE za, HTCoordE zb) {
        this.za    = za;
        this.zb    = zb;
      
        zc = new HTCoordE();
        zo = new HTCoordE();

        //CURVE
        /*
        for (int i = 0; i < tz.length; ++i) {
            tz[i] = new HTCoordE();
            ts[i] = new HTCoordS();
        }
        */
        //CURVE

        a = new HTCoordS();
        b = new HTCoordS();
        c = new HTCoordS();

        rebuild();
    }


  /* --- Refresh --- */

    /**
     * Refresh the screen coordinates of this node.
     *
     * @param sOrigin   the origin of the screen plane
     * @param sMax      the (xMax, yMax) point in the screen plane
     */
    void refreshScreenCoordinates(HTCoordS sOrigin, HTCoordS sMax) {
        if (kleinMode) {
            a.projectionEtoS(za.pToK(), sOrigin, sMax);
            b.projectionEtoS(zb.pToK(), sOrigin, sMax);
        } else {
            a.projectionEtoS(za, sOrigin, sMax);
            b.projectionEtoS(zb, sOrigin, sMax);
            c.projectionEtoS(zc, sOrigin, sMax);

            d = ((a.x - b.x) * (a.x - b.x)) + ((a.y - b.y) * (a.y - b.y));
            if (d != 0) {
                //QUAD
                curve.setCurve(a.x, a.y, c.x, c.y, b.x, b.y);
                //QUAD
            }

            //CURVE
            /*
            for (int i = 0; i < tz.length; ++i) {
                ts[i].projectionEtoS(tz[i], sOrigin, sMax);
            }
            */
            //CURVE
        }
    }


  /* --- Rebuild --- */

    /**
     * Builds the geodesic.
     */
    void rebuild() {
      if (! kleinMode) {
        if ( // za == origin
             (Math.abs(za.d()) < EPSILON) || 
             
             // zb == origin
             (Math.abs(zb.d()) < EPSILON) || 
             
             // za = lambda.zb
             (Math.abs((za.x / zb.x) - (za.y / zb.y)) < EPSILON) )
        {    
            type = LINE;
        } else {
            type = ARC;

            double da = 1 + za.x * za.x + za.y * za.y;
            double db = 1 + zb.x * zb.x + zb.y * zb.y;
            double dd = 2 * (za.x * zb.y - zb.x * za.y);
 
            zo.x = (zb.y * da - za.y * db) / dd;
            zo.y = (za.x * db - zb.x * da) / dd;

            double det = (zb.x - zo.x) * (za.y - zo.y) - 
                         (za.x - zo.x) * (zb.y - zo.y);
            double fa  = za.y * (za.y - zo.y) - za.x * (zo.x - za.x);
            double fb  = zb.y * (zb.y - zo.y) - zb.x * (zo.x - zb.x);

            zc.x = ((za.y - zo.y) * fb - (zb.y - zo.y) * fa) / det;
            zc.y = ((zo.x - za.x) * fb - (zo.x - zb.x) * fa) / det; 

            //CURVE
            /*
            r = Math.sqrt(zo.d2() - 1);
            double p = ((za.x - zo.x) * (zb.x - zo.x)) + 
                       ((za.y - zo.y) * (zb.y - zo.y)); 
            alpha = Math.acos(p / (r*r));
            HTCoordE cPrim = new HTCoordE();
            cPrim.sub(zc, zo);
            beta = cPrim.arg();

            tz[0].x = zo.x + (r * Math.cos(beta));
            tz[0].y = zo.y + (r * Math.sin(beta));

            
            for (int j = 1; j <= n; ++j) {
                double dj = (double) j;
                double dn = (double) (2 * n);

                double pair = beta - (alpha * (dj / dn));
                double impair  = beta + (alpha * (dj / dn));
                int indPair = 2 * j;
                int indImpair = (2 * j) - 1;
                
                tz[indPair].x = zo.x + (r * Math.cos(pair));
                tz[indPair].y = zo.y + (r * Math.sin(pair));

                tz[indImpair].x = zo.x + (r * Math.cos(impair));
                tz[indImpair].y = zo.y + (r * Math.sin(impair));
            }
            */
            //CURVE
        }
      }
    }


  /* --- Draw --- */

    /**
     * Draws this geodesic.
     *
     * @param g    the graphic context
     */
    void draw(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON); 
            g2.setColor(Color.black);
          if (kleinMode) {
                g2.drawLine(a.x, a.y, b.x, b.y);
          } else {
            switch(type) {
            case LINE:
                g2.drawLine(a.x, a.y, b.x, b.y);
                break;
            case ARC:
                if (d != 0) {
                    //QUAD
                    g2.draw(curve);
                    //QUAD

                    //CURVE
                   // drawCurve(g);
                    //CURVE
                }
                break;
            default:
                break;
            }
          }
         } else {
             System.err.println("Error : Hypertree 2.0 requires Java 1.2 "
                                + "or superior.");
         }
    } 


//CURVE
  /* --- Draw curve --- */

    /**
     * Draws the curve specified by the given points.
     *
     * @param g    the graphic context
     */
    /*
    private void drawCurve(Graphics g)
    {
         int d = ((ts[0].x - ts[2*n].x) * (ts[0].x - ts[2*n].x)) +
                 ((ts[0].y - ts[2*n].y) * (ts[0].y - ts[2*n].y));
         if (d == 0) {
             return;
         }

         if (HTDraw.isTranslating) {
             g.drawLine(a.x, a.y, b.x, b.y);
             return;
         }
         
         g.drawLine(ts[0].x, ts[0].y, ts[1].x, ts[1].y);
         g.drawLine(ts[0].x, ts[0].y, ts[2].x, ts[2].y);

         int p = 0;
         for (int j = 1; j < n; ++j) {
             p = 2 * j;
             g.drawLine(ts[p].x, ts[p].y, ts[p+2].x, ts[p+2].y);
             p = (2 * j) - 1;
             g.drawLine(ts[p].x, ts[p].y, ts[p+2].x, ts[p+2].y);
         }
    }
    */
//CURVE

  /* --- Klein --- */

    /**
     * Sets the klein mode.
     *
     * @param mode    setting on or off
     */
    void kleinMode(boolean mode) {
        if (mode != kleinMode) {
            kleinMode = mode;
        }
    }

    
  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = "Geodesic betweens : " +
                        "\n\t A: " + za + 
                        "\n\t B: " + zb +
                        "\n\t is ";
        switch(type) {
        case LINE:
            result += "a line.";
            break;
        case ARC:
            result += "an arc.";
            break;
        default:
            result += "nothing ?";
            break;
        }
        return result;
    }

}

