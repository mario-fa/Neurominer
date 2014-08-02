/*
 * HTDrawNodeComposite.java
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

import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * The HTDrawNodeComposite class implements the Composite design pattern
 * for HTDrawNode.
 * It represents a HTDrawNode which is not a leaf.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTDrawNodeComposite 
    extends HTDrawNode {

    private HTModelNodeComposite node      = null; // encapsulated HTModelNode
    private Vector               children  = null; // children of this node
    private Hashtable            geodesics = null; // geodesics linking the 
                                                   // children


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param father    the father of this node
     * @param node      the encapsulated HTModelNode
     * @param model     the drawing model
     */
    HTDrawNodeComposite(HTDrawNodeComposite father, 
                        HTModelNodeComposite node, HTDraw model) {
        super(father, node, model);
        this.node = node;
        this.children = new Vector();
        this.geodesics = new Hashtable();

        HTModelNode childNode = null;
        HTDrawNode child = null;
        HTDrawNode brother = null;
        boolean first = true;
        boolean second = false;
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            childNode = (HTModelNode) e.nextElement();
            if (childNode.isLeaf()) {
                child = new HTDrawNode(this, childNode, model);
            } else {
                child = new HTDrawNodeComposite(this, 
                                      (HTModelNodeComposite) childNode, model);
            }
            addChild(child);
            if (first) {
                brother = child;
                first = false;
                second = true;
            } else if (second) {
                child.setBrother(brother);
                brother.setBrother(child);
                brother = child;
                second = false;
            } else {
                child.setBrother(brother);
                brother = child;
            }  
        }
    }


  /* --- Children --- */

    /**
     * Returns the children of this node, 
     * in an Enumeration.
     *
     * @return    the children of this node
     */
    Enumeration children() {
        return children.elements();
    }

    /** 
     * Adds the HTDrawNode as a children.
     *
     * @param child    the child
     */
    void addChild(HTDrawNode child) {
        children.addElement(child);
        geodesics.put(child, new HTGeodesic(getCoordinates(), 
                                            child.getCoordinates()));
    }


  /* --- Screen Coordinates --- */

    /**
     * Refresh the screen coordinates of this node
     * and recurse on children.
     *
     * @param sOrigin   the origin of the screen plane
     * @param sMax      the (xMax, yMax) point in the screen plane
     */ 
    void refreshScreenCoordinates(HTCoordS sOrigin, HTCoordS sMax) {
        super.refreshScreenCoordinates(sOrigin, sMax);
        HTDrawNode child = null;

        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.refreshScreenCoordinates(sOrigin, sMax);
            HTGeodesic geod = (HTGeodesic) geodesics.get(child);
            if (geod != null) {
                geod.refreshScreenCoordinates(sOrigin, sMax);
            }
            
        }
    }


  /* --- Drawing --- */

    /**
     * Draws the branches from this node to 
     * its children.
     *
     * @param g    the graphic context
     */
    void drawBranches(Graphics g) {
        HTDrawNode child = null;

        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            HTGeodesic geod = (HTGeodesic) geodesics.get(child);
            if (geod != null) {
                geod.draw(g);
            }
            child.drawBranches(g); 
        }
    }

    /**
     * Draws this node.
     *
     * @param g    the graphic context
     */
    void drawNodes(Graphics g) {
        if (fastMode == false) {
            super.drawNodes(g);
        
            HTDrawNode child = null;
            for (Enumeration e = children(); e.hasMoreElements(); ) {
                child = (HTDrawNode) e.nextElement();
                child.drawNodes(g);
            }
        }
    }

    /**
     * Returns the minimal distance between this node
     * and his father, his brother, and his children.
     *
     * @return    the minimal distance
     */
    int getSpace() {
        int space = super.getSpace();
        
        if (! children.isEmpty()) {
            HTDrawNode child = (HTDrawNode) children.firstElement();
            HTCoordS zC = child.getScreenCoordinates();      
            int dC = zs.getDistance(zC);
            
            if (space == -1) {
                return dC;
            } else {
                return Math.min(space, dC);
            }
        } else {
            return space;
        }
    }
    

  /* --- Translation --- */

    /**
     * Special transformation, optimized.
     *
     * @param alpha    first member
     * @param beta     second member
     */
    void specialTrans(HTCoordE alpha, HTCoordE beta) {
        super.specialTrans(alpha, beta);

        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.specialTrans(alpha, beta);
            HTGeodesic geod = (HTGeodesic) geodesics.get(child);
            if (geod != null) {
                geod.rebuild();
            }
        }
    }

    /**
     * Ends the translation.
     */
    void endTranslation() {
        super.endTranslation();

        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.endTranslation();
        }
    }

    /**
     * Restores the hyperbolic tree to its origin.
     */
    void restore() {
        super.restore();

        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.restore();
            HTGeodesic geod = (HTGeodesic) geodesics.get(child);
            if (geod != null) {
                geod.rebuild();
            }
        }

    }

    /**
     * Sets the fast mode, where nodes are no more drawed.
     *
     * @param mode    setting on or off.
     */
    void fastMode(boolean mode) {
        super.fastMode(mode);
        
        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.fastMode(mode);
        }
    }

    /**
     * Sets the long name mode, where full name are drawn.
     *
     * @param mode    setting on or off.
     */
    void longNameMode(boolean mode) {
        super.longNameMode(mode);
        
        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.longNameMode(mode);
        }
    }

    /**
     * Sets the klein mode.
     *
     * @param mode    setting on or off.
     */
    void kleinMode(boolean mode) {
        super.kleinMode(mode);
        
        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.kleinMode(mode);
            HTGeodesic geod = (HTGeodesic) geodesics.get(child);
            if (geod != null) {
                geod.kleinMode(mode);
                geod.rebuild();
            }
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
        HTDrawNode result = super.findNode(zs);
        if (result != null) {
            return result;
        } else {
            HTDrawNode child = null;
            for (Enumeration e = children(); e.hasMoreElements(); ) {
                 child = (HTDrawNode) e.nextElement();
                 result = child.findNode(zs);
                 if (result != null) {
                     return result;
                 }
            }
            return null;
        }
    }


  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = super.toString();
        HTDrawNode child = null;
        result += "\n\tChildren :";
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            result += "\n\t-> " + child.getName();
        }
        return result;
    }

}

