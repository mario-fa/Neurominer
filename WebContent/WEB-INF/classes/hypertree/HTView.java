/*
 * HTView.java
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

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;


/**
 * The HTView class implements a view of the HyperTree.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
public class HTView
    extends JPanel {

    private HTModel    model  = null; // the tree model represented
    private HTDraw     draw   = null; // the drawing model
    private HTAction   action = null; // action manager


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param model    the tree model to view
     */
    public HTView(HTModel model) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(250, 250));
        setBackground(Color.white);

        this.model = model; 
        draw = new HTDraw(model, this);
        action = new HTAction(draw);
        startMouseListening();
        ToolTipManager.sharedInstance().registerComponent(this);
    }


  /* --- Node finding --- */

    /**
     * Returns the node containing the mouse event.
     * <P>
     * This will be a HTNode.
     *
     * @param event    the mouse event on a node
     * @return         the node containing this event;
     *                 could be <CODE>null</CODE> if no node was found
     */
    public Object getNodeUnderTheMouse(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        
        HTDrawNode node = draw.findNode(new HTCoordS(x, y));
        if (node != null) {
            return node.getHTModelNode().getNode();
        } else {
            return null;
        }
    }
    

  /* --- Tooltip --- */

    /**
     * Returns the tooltip to be displayed.
     *
     * @param event    the event triggering the tooltip
     * @return         the String to be displayed
     */
    public String getToolTipText(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        
        HTDrawNode node = draw.findNode(new HTCoordS(x, y));
        if (node != null) {
            return "Distância Relativa: " + node.getDistance() + " %";
        } else {
            return null;
        }
    }

  /* --- Paint --- */

    /**
     * Paint the component.
     *
     * @param g    the graphic context
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw.refreshScreenCoordinates();
        draw.drawBranches(g);
        draw.drawNodes(g);
    }


  /* --- Thread-safe locking --- */
  
    /**
     * Stops the listening of mouse events.
     */
    void stopMouseListening() {
        this.removeMouseListener(action);
        this.removeMouseMotionListener(action);
    }
    
    /**
     * Starts the listening of mouse events.
     */
    void startMouseListening() {
        this.addMouseListener(action);
        this.addMouseMotionListener(action);
    }

}

