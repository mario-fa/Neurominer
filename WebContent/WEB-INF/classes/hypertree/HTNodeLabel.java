/*
 * HTNodeLabel.java
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


/**
 * The HTNodeLabel class implements the drawed label 
 * representing a node.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTNodeLabel {

    private HTDrawNode node   = null;  // represented node
    private int        x      = 0;     // x up-left corner
    private int        y      = 0;     // y up-left corner
    private int        width  = 0;     // width of the label
    private int        height = 0;     // height of the label
    private boolean    active = false; // should be drawed ?


  /* ---  Constructor --- */
  
    /**
     * Constructor.
     * 
     * @param node    the represented node
     */
    HTNodeLabel(HTDrawNode node) {
        this.node = node;
    }
    
    
  /* --- Draw --- */
  
    /**
     * Draw this label, if there is enought space.
     *
     * @param g    the graphic context
     */
    void draw(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int fh = fm.getHeight();
        int space = node.getSpace();
        if (space >= fh) {

            active = true;
            HTCoordS zs = node.getScreenCoordinates();          
            String name = node.getName();
            Color color = node.getColor();
            char[] nameC = name.toCharArray();            
            int nameLength = nameC.length;
            int nameWidth = fm.charsWidth(nameC, 0, nameLength);

/*            if (node.getLongNameMode() == false) {
                while((nameWidth >= space) && (nameLength > 0)) {
                    nameLength--;               
                    nameWidth = fm.charsWidth(nameC, 0, nameLength);
                }
            }
*/           
            height = fh;
            width = nameWidth + 10;
            x = zs.x - (width / 2);
            y = zs.y - (fh / 2);  
                      
            g.setColor(color);
            g.fillRect(x, y, width, height);
            g.setColor(Color.black);
            g.drawRect(x, y, width, height);

            int sx = zs.x - (nameWidth / 2);            
            int sy = y + fm.getAscent() + (fm.getLeading() / 2);       
            
            g.drawString(new String(nameC, 0, nameLength), sx, sy);           
        } else {
            active = false;
        }
    }


  /* --- Zone containing --- */

    /**
     * Is the given HTCoordS within this label ?
     *
     * @return    <CODE>true</CODE> if it is,
     *            <CODE>false</CODE> otherwise
     */
    boolean contains(HTCoordS zs) {
        if (active) {
            if ((zs.x >= x) && (zs.x <= (x + width)) &&
                (zs.y >= y) && (zs.y <= (y + height)) ) {
                return true;
            } else {
                return false;
            }
        } else {
            return node.getScreenCoordinates().contains(zs);
        }
    }
    
        
  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = "label of " + node.getName() + 
                        "\n\tx = " + x + " : y = " + y +
                        "\n\tw = " + width + " : h = " + height; 
        return result;
    }

}
