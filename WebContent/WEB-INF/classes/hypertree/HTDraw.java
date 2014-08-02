/*
 * HTDraw.java
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

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.SwingUtilities;

import java.util.Date;

/**
 * The HTDraw class implements the drawing model for the HTView.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTDraw {

    private final static int NBR_FRAMES = 5; // number of intermediates 
                                             // animation frames

    private HTModel    model    = null;  // the tree model
    private HTView     view     = null;  // the view using this drawing model
    private HTDrawNode drawRoot = null;  // the root of the drawing tree 

    private HTCoordS   sOrigin  = null;  // origin of the screen plane
    private HTCoordS   sMax     = null;  // max point in the screen plane 
      
    private double[]   ray      = null;
    
    private boolean    fastMode = false; // fast mode
    private boolean    longNameMode = false; // long name mode
    private boolean    kleinMode = false; // klein mode

    public static boolean isTranslating = false;


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param model    the tree model to draw 
     * @param view     the view using this drawing model
     */
    HTDraw(HTModel model, HTView view) {
        this.view = view;
        this.model = model;
        HTModelNode root = model.getRoot();
        sOrigin = new HTCoordS();
        sMax = new HTCoordS();

        ray = new double[4];
        ray[0] = model.getLength();

        for (int i = 1; i < ray.length; i++) {
            ray[i] = (ray[0] + ray[i - 1]) / (1 + (ray[0] * ray[i - 1]));
        }

        if (root.isLeaf()) {
            drawRoot = new HTDrawNode(null, root, this);
        } else {
            drawRoot = new HTDrawNodeComposite(null, 
                                            (HTModelNodeComposite) root, this);
        }
    }


  /* --- Screen coordinates --- */

    /**
     * Refresh the screen coordinates of the drawing tree.
     */
    void refreshScreenCoordinates() {
        Insets insets = view.getInsets();
        sMax.x = (view.getWidth() - insets.left - insets.right) / 2;
        sMax.y = (view.getHeight() - insets.top - insets.bottom) / 2;
        sOrigin.x = sMax.x + insets.left;
        sOrigin.y = sMax.y + insets.top;
        drawRoot.refreshScreenCoordinates(sOrigin, sMax);
    }

    /**
     * Returns the origin of the screen plane.
     * WARNING : this is not a copy but the original object.
     *
     * @return    the origin of the screen plane
     */
    HTCoordS getSOrigin() {
        return sOrigin;
    }

    /**
     * Return the point representing the up right corner
     * of the screen plane, thus giving x and y maxima.
     * WARNING : this is not a copy but the original object.
     *
     * @return    the max point
     */
    HTCoordS getSMax() {
        return sMax;
    }


  /* --- Drawing --- */

    /**
     * Draws the branches of the hyperbolic tree.
     *
     * @param g    the graphic context
     */
    void drawBranches(Graphics g) {
        drawRoot.drawBranches(g);
    }

    /**
     * Draws the nodes of the hyperbolic tree.
     *
     * @param g    the graphic context
     */
    void drawNodes(Graphics g) {
        drawRoot.drawNodes(g);
    }


  /* --- Translation --- */

    /**
     * Translates the hyperbolic tree by the given vector.
     *
     * @param t    the translation vector
     */
    void translate(HTCoordE zs, HTCoordE ze) {
        isTranslating = true;
        
        HTCoordE zo = new HTCoordE(drawRoot.getOldCoordinates());
        zo.x = - zo.x;
        zo.y = - zo.y;
        HTCoordE zs2 = new HTCoordE(zs);
        
        zs2.translate(zo);

        HTCoordE t = new HTCoordE();
        double de = ze.d2();
        double ds = zs2.d2();
        double dd = 1.0 - de * ds;
        t.x = (ze.x * ( 1.0 - ds) - zs2.x * (1.0 - de)) / dd;
        t.y = (ze.y * ( 1.0 - ds) - zs2.y * (1.0 - de)) / dd;
        
        if (t.isValid()) {

            // alpha = 1 + conj(zo)*t
            HTCoordE alpha = new HTCoordE();
            alpha.x = 1 + (zo.x * t.x) + (zo.y * t.y);
            alpha.y = (zo.x * t.y) - (zo.y * t.x);
            // beta = zo + t
            HTCoordE beta = new HTCoordE();
            beta.x = zo.x + t.x;
            beta.y = zo.y + t.y;
           
            drawRoot.specialTrans(alpha, beta);

            view.repaint();
        }
    }

    /**
     * Signal that the translation ended.
     */
    void endTranslation() {
        isTranslating = false;
        drawRoot.endTranslation();
        view.repaint();
    }

    /**
     * Translate the hyperbolic tree so that the given node 
     * is put at the origin of the hyperbolic tree.
     *
     * @param node    the given HTDrawNode
     */
    void translateToOrigin(HTDrawNode node) {
        view.stopMouseListening();
        isTranslating = true;
        AnimThread t = new AnimThread(node);
        t.start();
    }

    /**
     * Restores the hyperbolic tree to its origin.
     */
    void restore() {
        drawRoot.restore();
        view.repaint();
    }

    /**
     * Sets the fast mode, where nodes are no more drawed.
     *
     * @param mode    setting on or off.
     */
    void fastMode(boolean mode) {
        if (mode != fastMode) {
            fastMode = mode;
            drawRoot.fastMode(mode);
            if (mode == false) {
                view.repaint();
            }
        }
    }
    
    /**
     * Sets the long name mode, where full names are drawn.
     *
     * @param mode    setting on or off.
     */
    void longNameMode(boolean mode) {
        if (mode != longNameMode) {
            longNameMode = mode;
            drawRoot.longNameMode(mode);
            view.repaint();
        }
    }
    
    /**
     * Sets the klein mode.
     *
     * @param mode    setting on or off.
     */
    void kleinMode(boolean mode) {
        if (mode != kleinMode) {
            HTCoordE zo = new HTCoordE(drawRoot.getCoordinates());
            
            kleinMode = mode;
            if (kleinMode) {
                model.setLengthKlein();
            } else {
                model.setLengthPoincare();
            }
            model.layoutHyperbolicTree();
            drawRoot.kleinMode(mode);
            
            restore();

            if (! kleinMode) {
                translate(new HTCoordE(), zo.pToK());
            } else {
                translate(new HTCoordE(), zo.kToP());
            }
            endTranslation();
        }
    }
    

  /* --- Node searching --- */

    /**
     * Returns the node (if any) whose screen coordinates' zone
     * contains thoses given in parameters.
     *
     * @param zs    the given screen coordinate
     * @return      the searched HTDrawNode if found;
     *              <CODE>null</CODE> otherwise
     */
    HTDrawNode findNode(HTCoordS zs) {
        return drawRoot.findNode(zs);
    }


  /* --- Inner animation thread --- */

    /**
     * The AnimThread class implements the thread that do the animation
     * when clicking on a node.
     */  
    class AnimThread
        extends Thread {

        private HTDrawNode node  = null; // node to put at the origin
        private Runnable   tTask = null; // translation task

        /**
         * Constructor.
         *
         * @param node    the node to put at the origin
         */
        AnimThread(HTDrawNode node) {
            this.node = node;
        }

        /**
         * Do the animation.
         */
        public void run() {
            HTCoordE zn = node.getOldCoordinates();
            HTCoordE zf = new HTCoordE();

            int frames = NBR_FRAMES;
            int nodes = model.getNumberOfNodes();
            
            double d = zn.d();
            for (int i = 0; i < ray.length; i++) {
            	if (d > ray[i]) {
                	frames += NBR_FRAMES / 2;
            	}
            }
                        
            double factorX = zn.x / frames;
            double factorY = zn.y / frames;
            
            for (int i = 1; i < frames; i++) {
                zf.x = zn.x - (i * factorX);
                zf.y = zn.y - (i * factorY);
                tTask = new TranslateThread(zn, zf);
                try {
                    SwingUtilities.invokeAndWait(tTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
       
            zf.x = 0.0;
            zf.y = 0.0;
            tTask = new LastTranslateThread(zn, zf);
            try {
                SwingUtilities.invokeAndWait(tTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isTranslating = false;
        }
        
      /* --- Inner's inner --- */
      
        class TranslateThread
            implements Runnable {
            
            HTCoordE zStart = null; 
            HTCoordE zEnd   = null;
            
            TranslateThread(HTCoordE z1, HTCoordE z2) {
                zStart = z1;
                zEnd = z2;
            }
            
            public void run() {
                translate(zStart, zEnd);
			    view.repaint();
            }        
        }
        
        class LastTranslateThread
            implements Runnable {
            
            HTCoordE zStart = null; 
            HTCoordE zEnd   = null;
            
            LastTranslateThread(HTCoordE z1, HTCoordE z2) {
                zStart = z1;
                zEnd = z2;
            }
            
            public void run() {
                translate(zStart, zEnd);
                endTranslation();
			    view.repaint();
			    view.startMouseListening();
            }        
        }

              
    }

}

