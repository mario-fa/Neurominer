/*
 * HTModel.java
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


/**
 * The HTModel class implements the model for the HyperTree.
 * It's a tree of HTModelNode and HTModelNodeComposite, each keeping the
 * initial layout of the tree in the Poincarre's Model.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTModel {

    private HTModelNode root   = null; // the root of the tree's model 
    private static final double lp = 0.3;
    private static final double lk = 0.08;

    private double      length = lp;  // distance between node and children
    private int         nodes  = 0;    // number of nodes


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param root    the root of the real tree 
     */
    HTModel(HTNode root) {
        if (root.isLeaf()) {
            this.root = new HTModelNode(root, this);
        } else {
            this.root = new HTModelNodeComposite(root, this);
        }
        this.root.layoutHyperbolicTree();
    }


  /* --- Accessor --- */

    /**
     * Returns the root of the tree model.
     *
     * @return    the root of the tree model
     */
    HTModelNode getRoot() {
        return root;
    }


  /* --- Length --- */

    /**
     * Returns the distance between a node and its children
     * in the hyperbolic space.
     *
     * @return    the distance
     */
    double getLength() {
        return length;
    }

    void setLengthPoincare() {
        length = lp;
    }

    void setLengthKlein() {
        length = lk;
    }

    void layoutHyperbolicTree() {
        root.layoutHyperbolicTree();
    }


  /* --- Number of nodes --- */
  
    /**
     * Increments the number of nodes.
     */
    void incrementNumberOfNodes() {
        nodes++;
    }
    
    /**
     * Returns the number of nodes.
     *
     * @return    the number of nodes
     */
    int getNumberOfNodes() {
        return nodes;
    }

}

