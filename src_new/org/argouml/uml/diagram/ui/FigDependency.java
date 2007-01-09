// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;

import org.argouml.kernel.ProjectManager;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.Fig;

/**
 * This class represents a Fig for a Dependency.
 * It has a dashed line and a V-shaped arrow-head.
 * 
 * @author ics 125b course, spring 1998
 */
public class FigDependency extends FigEdgeModelElement {

    /**
     * Serial version generated by Eclipse for rev. 1.22
     */
    private static final long serialVersionUID = -1779182458484724448L;

    private ArrowHeadGreater endArrow;
    
    /*
     * Text group to contain name & stereotype
     */
    private FigTextGroup middleGroup = new FigTextGroup(); 

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor
     */
    public FigDependency() {
        middleGroup.addFig(getNameFig());
        middleGroup.addFig(getStereotypeFig());
        addPathItem(middleGroup,
                new PathConvPercent2(this, middleGroup, 50, 25));
        endArrow = new ArrowHeadGreater();
        endArrow.setFillColor(Color.red);
        setDestArrowHead(endArrow);
        setBetweenNearestPoints(true);
        setLayer(ProjectManager.getManager()
		 .getCurrentProject().getActiveDiagram().getLayer());
        getFig().setDashed(true);
    }

    /**
     * Constructor that sets the UML element
     * @param edge the UML element
     */
    public FigDependency(Object edge) {
        this();
        setOwner(edge);
    }

    /**
     * @param edge theUML element
     * @param lay the layer
     */
    public FigDependency(Object edge, Layer lay) {
        this();
        setOwner(edge);
        setLayer(lay);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /*
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(true);
        // computeRoute();
        // this recomputes the route if you reload the diagram.
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(org.tigris.gef.presentation.Fig)
     */
    protected boolean canEdit(Fig f) {
        return false;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /*
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }

    protected void updateNameText() {
        super.updateNameText();
        middleGroup.calcBounds();
    }

    protected void updateStereotypeText() {
        super.updateStereotypeText();
        middleGroup.calcBounds();
    }

} /* end class FigRealization */

