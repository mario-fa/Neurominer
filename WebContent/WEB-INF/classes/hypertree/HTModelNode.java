/*
 * HTModelNode.java
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
 * The HTModelNode class implements encapsulation of a HTNode
 * for the model. 
 * It keeps the original euclidian coordinates of the node.
 * It implements the Composite design pattern.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTModelNode {

    private   HTNode               node   = null; // encapsulated HTNode

    protected HTModel              model  = null; // tree model
    protected HTModelNodeComposite parent = null; // parent node

    protected HTCoordE             z      = null; // Euclidian coordinates
    protected double               weight = 1.0;  // part of space taken 
                                                  // by this node


  /* --- Constructor --- */

    /**
     * Constructor for root node.
     *
     * @param node     the encapsulated HTNode
     * @param model    the tree model using this HTModelNode
     */
    HTModelNode(HTNode node, HTModel model) {
        this(node, null, model);
    }

    /**
     * Constructor.
     *
     * @param node      the encapsulated HTNode
     * @param parent    the parent node
     * @param model     the tree model using this HTModelNode
     */
    HTModelNode(HTNode node, HTModelNodeComposite parent, HTModel model) {
        this.node = node;
        this.parent = parent;
        this.model = model;
        model.incrementNumberOfNodes();
         
        z = new HTCoordE();
    }


  /* --- Encapsulated node --- */

    /**
     * Returns the encapsulated node.
     *
     * @return    the encapsulated node
     */
    HTNode getNode() {
        return node;
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
    	return String.valueOf(node.getDistance());
    }
    
    int getTypeData() {
    	return node.getTypeData();
    }
    
    public String getPathFile()
    {
    	return node.getPathFile();
    }

  /* --- Weight Managment --- */

    /**
     * Returns the weight of this node.
     *
     * @return    the weight of this node
     */
    double getWeight() {
        return weight;
    }


  /* --- Tree management --- */

    /**
     * Returns the parent of this node.
     *
     * @return    the parent of this node
     */
    HTModelNodeComposite getParent() {
        return parent;
    }

    /**
     * Returns <CODE>true</CODE> if this node
     * is not an instance of HTModelNodeComposite.
     *
     * @return    <CODE>true</CODE>
     */
    boolean isLeaf() {
        return true;
    }


  /* --- Coordinates --- */

    /**
     * Returns the coordinates of this node.
     * Thoses are the original hyperbolic coordinates, 
     * without any translations.
     * WARNING : this is NOT a copy but the true object
     * (for performance).
     *
     * @return    the original hyperbolic coordinates
     */
    HTCoordE getCoordinates() {
        return z;
    }


  /* --- Hyperbolic layout --- */

    /**
     * Layouts the nodes in the hyperbolic space.
     */
    void layoutHyperbolicTree() {
        HTSector sector = new HTSector();
        double eps = 0.01;
        double d = Math.sqrt(1 - (eps * eps));
        sector.A = new HTCoordE(d, eps);
        sector.B = new HTCoordE(d, -eps);
        this.layout(sector, model.getLength());
    }

    /**
     * Layout this node in the hyperbolic space.
     * First set the point at the right distance,
     * then translate by father's coordinates.
     * Then, compute the right angle and the right width.
     *
     * @param sector    the sector
     * @param length    the parent-child length
     */
    void layout(HTSector sector, double length) {
        // Nothing to do for the root node
        if (parent == null) {
            return;
        }
        
        HTCoordE zp = parent.getCoordinates();

        double angle = sector.getBisectAngle();

        // We first start as if the parent was the origin.
        // We still are in the hyperbolic space.
        z.x = length * Math.cos(angle);
        z.y = length * Math.sin(angle);

        // Then translate by parent's coordinates
        z.translate(zp);
    } 


  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = getName() +
                        "\n\t" + z +
                        "\n\tWeight = " + weight; 
        return result;
    }

}

