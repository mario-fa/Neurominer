/*
 * HTModelNodeComposite.java
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

import java.util.Enumeration;
import java.util.Vector;


/**
 * The HTModelNodeComposite class implements the Composite design pattern
 * for HTModelNode.
 * It represents a HTModelNode which is not a leaf.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTModelNodeComposite 
    extends HTModelNode {

    private Vector children     = null; // children of this node

    private double globalWeight = 0.0;  // sum of children weight


  /* --- Constructor --- */

    /**
     * Constructor for root node.
     *
     * @param node     the encapsulated HTNode
     * @param model    the tree model using this HTModelNode
     */
    HTModelNodeComposite(HTNode node, HTModel model) {
        this(node, null, model);
    }

    /**
     * Constructor.
     *
     * @param node      the encapsulated HTNode
     * @param parent    the parent node
     * @param model     the tree model using this HTModelNode
     */
    HTModelNodeComposite(HTNode node, HTModelNodeComposite parent, 
                         HTModel model) {
        super(node, parent, model);
        this.children = new Vector();

        HTNode childNode = null;
        HTModelNode child = null;
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            childNode = (HTNode) e.nextElement();
            if (childNode.isLeaf()) {
                child = new HTModelNode(childNode, this, model);
            } else {
                child = new HTModelNodeComposite(childNode, this, model);
            }
            addChild(child);
        }
        
        // here the down of the tree is built, so we can compute the weight
        computeWeight();
    }


  /* --- Weight Managment --- */

    /**
     * Compute the Weight of this node.
     * As the weight is computed with the log
     * of the sum of child's weight, we must have all children 
     * built before starting the computing.
     */
    private void computeWeight() {
        HTModelNode child = null;
         
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTModelNode) e.nextElement();
            globalWeight += child.getWeight();
        } 
        if (globalWeight != 0.0) {
            weight += Math.log(globalWeight);
        }
    }


  /* --- Tree management --- */

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
     * Adds the HTModelNode as a children.
     *
     * @param child    the child
     */
    void addChild(HTModelNode child) {
        children.addElement(child);
    }

    /**
     * Returns <CODE>false</CODE> as this node
     * is an instance of HTModelNodeComposite.
     *
     * @return    <CODE>false</CODE>
     */
    boolean isLeaf() {
        return false;
    }


  /* --- Hyperbolic layout --- */

    /**
     * Layout this node and its children in the hyperbolic space.
     * Mainly, divide the width angle between children and
     * put the children at the right angle.
     * Compute also an optimized length to the children.
     *
     * @param sector    the sector
     * @param length    the parent-child length
     */
   void layout(HTSector sector, double length) {
        super.layout(sector, length);   

        if (parent != null) {
            sector.translate(parent.getCoordinates());
            HTCoordE h = new HTCoordE(getCoordinates());
            h.x = -h.x;
            h.y = -h.y;
            sector.translate(h);
        }

        int nbrChild = children.size();
        double l1 = (0.95 - model.getLength());
        double l2 = Math.cos((20.0 * Math.PI) / (2.0 * nbrChild + 38.0)); 
        length = model.getLength() + (l1 * l2);

        double   alpha = sector.getAngle();
        double   omega = sector.A.arg();
        HTCoordE K     = new HTCoordE(sector.A);

        // It may be interesting to sort children by weight instead
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            HTModelNode child = (HTModelNode) e.nextElement();
            HTSector childSector = new HTSector();
            childSector.A = new HTCoordE(K); 
            omega += (alpha * (child.getWeight() / globalWeight));
            K.x = Math.cos(omega);
            K.y = Math.sin(omega); 
            childSector.B = new HTCoordE(K);
            child.layout(childSector, length);
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
        HTModelNode child = null;
        result += "\n\tChildren :";
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTModelNode) e.nextElement();
            result += "\n\t-> " + child.getName();
        }
        return result;
    }

}

