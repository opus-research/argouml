// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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


package org.argouml.uml.diagram.activity.ui;

import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.state.ui.StateDiagramRenderer;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigNode;


/**
 * 
 * @author mkl
 *
 */
public class ActivityDiagramRenderer extends StateDiagramRenderer {

    /** Return a Fig that can be used to represent the given node
     *  
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer, java.lang.Object)
     */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
        if (ModelFacade.isAPartition(node)) {
            return new FigPartition(gm, node);
        }
        if (ModelFacade.isACallState(node)) {
            return new FigCallState(gm, node);
        }
        if (ModelFacade.isAObjectFlowState(node)) {
            return new FigObjectFlowState(gm, node);
        }
        if (ModelFacade.isASubactivityState(node)) {
            return new FigSubactivityState(gm, node);
        }
        return super.getFigNodeFor(gm, lay, node);
    }
}
