/*
 * HTDrawNode.java
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
import java.awt.FontMetrics;


/**
 * The HTDrawNode class contains the drawing coordinates of a HTModelNode 
 * for the HTView. 
 * It implements the Composite design pattern.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTDrawNode {

    private   HTDraw              model    = null;  // drawing model
    private   HTModelNode         node     = null;  // encapsulated HTModelNode
 
    private   HTCoordE            ze       = null;  // current euclidian coords
    private   HTCoordE            oldZe    = null;  // old euclidian coords
    protected HTCoordS            zs       = null;  // current screen coords

    private   HTDrawNodeComposite father   = null;  // father of this node
    private   HTDrawNode          brother  = null;  // brother of this node

    private   HTNodeLabel         label    = null;  // label of the node

    protected boolean             fastMode = false; // fast mode
    protected boolean             longNameMode = false; // long name displayed
    protected boolean             kleinMode = false; // klein mode


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param father    the father of this node
     * @param node      the encapsulated HTModelNode
     * @param model     the drawing model
     */
    HTDrawNode(HTDrawNodeComposite father, HTModelNode node, HTDraw model) {
        this.father = father;
        this.node = node;
        this.model = model;

        label = new HTNodeLabel(this);

        ze = new HTCoordE(node.getCoordinates());
        oldZe = new HTCoordE(ze);
        zs = new HTCoordS();
    }


  /* --- Brother --- */
  
    /**
     * Sets the brother of this node.
     *
     * @param brother    the borther of this node
     */
    void setBrother(HTDrawNode brother) {
        this.brother = brother;
    }


  /* --- Encapsulated HYModelNode --- */

    /**
     * Returns the encapsulated HTModelNode.
     *
     * @return    the encapsulated HTModelNode
     */
    HTModelNode getHTModelNode() {
        return node;
    }


  /* --- Color --- */

    /**
     * Returns the color of the node.
     *
     * @return    the color of the node
     */
    Color getColor() {
        return node.getNode().getColor();
    }


  /* --- Name --- */

    /**
     * Returns the name of this node.
     *
     * @return    the name of this node
     */
    String getName() {
        return node.getName();
    }
    
    String getDistance() {
        return node.getDistance();
    }

    int getTypeData () {
    	return node.getTypeData();
    }
    
    public String getPathFile()
    {
    	return node.getPathFile();
    }    
  /* --- Coordinates --- */

    /**
     * Returns the current coordinates of this node.
     * WARNING : this is NOT a copy but the true object
     * (for performance).
     *
     * @return     the current coordinates
     */
    HTCoordE getCoordinates() {
        return ze;
    } 

    HTCoordE getOldCoordinates() {
        return oldZe;
    }

    HTCoordS getScreenCoordinates() {
        return zs;
    }

    /**
     * Refresh the screen coordinates of this node.
     *
     * @param sOrigin   the origin of the screen plane
     * @param sMax      the (xMax, yMax) point in the screen plane
     */
    void refreshScreenCoordinates(HTCoordS sOrigin, HTCoordS sMax) {
        if (kleinMode) {
            zs.projectionEtoS(ze.pToK(), sOrigin, sMax);
        } else {
            zs.projectionEtoS(ze, sOrigin, sMax);
        }
    } 


  /* --- Drawing --- */

    /**
     * Draws the branches from this node to 
     * its children.
     * Overidden by HTDrawNodeComposite
     *
     * @param g    the graphic context
     */
    void drawBranches(Graphics g) {}

    /**
     * Draws this node.
     *
     * @param g    the graphic context
     */
    void drawNodes(Graphics g) {
        if (fastMode == false) {
            label.draw(g);
        }
    }

    /**
     * Returns the minimal distance between this node
     * and his father and his brother.
     *
     * @return    the minimal distance
     */
    int getSpace() {
        int dF = -1;
        int dB = -1;
        
        if (father != null) {
            HTCoordS zF = father.getScreenCoordinates();
            dF = zs.getDistance(zF);
        }
        if (brother != null) {
          	HTCoordS zB = brother.getScreenCoordinates();
        	dB = zs.getDistance(zB);
        }
         
        if ((dF == -1) && (dB == -1)) {
            return -1;
        } else if (dF == -1) {
            return dB;
        } else if (dB == -1) {
            return dF;
        } else { 
            return Math.min(dF, dB);
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
        ze.copy(oldZe);
        ze.specialTrans(alpha, beta);
    }

    /**
     * Ends the translation.
     */
    void endTranslation() {
        oldZe.copy(ze);
    }

    /**
     * Restores the hyperbolic tree to its origin.
     */
    void restore() {
        HTCoordE orig = node.getCoordinates();
        ze.x = orig.x;
        ze.y = orig.y;
        oldZe.copy(ze);
    }

    /**
     * Sets the fast mode, where nodes are no more drawed.
     *
     * @param mode    setting on or off.
     */
    void fastMode(boolean mode) {
        if (mode != fastMode) {
            fastMode = mode;
        }
    }


    /**
     * Sets the long name mode, where full names are drawn.
     *
     * @param mode     setting on or off.
     */
    void longNameMode(boolean mode) {
        if (mode != longNameMode) {
            longNameMode = mode;
        }
    }

    /**
     * Sets the klein mode.
     *
     * @param mode     setting on or off.
     */
    void kleinMode(boolean mode) {
        if (mode != kleinMode) {
            kleinMode = mode;
        }
    }

    /**
     * Returns the long name mode.
     *
     * @return    is the long name mode on or off ?
     */
    boolean getLongNameMode() {
        return longNameMode;
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
        if (label.contains(zs)) {
            return this;
        } else {
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
        String result = getName() + 
                        "\n\t" + ze + 
                        "\n\t" + zs; 
        return result;
    }

}

