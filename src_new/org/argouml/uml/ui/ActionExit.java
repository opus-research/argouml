// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.api.Configuration;
import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;

/**
 * Action to exit ArgoUML.
 */
public class ActionExit extends UMLAction 
    implements CommandLineInterface {
  
    ////////////////////////////////////////////////////////////////
    // static variables
  
    /**
     * The singleton.
     */
    public static final ActionExit SINGLETON = new ActionExit();
 
    /** remember if this form is already active, so that it does
        not popup twice.
    */
    private boolean active = false;
  
    ////////////////////////////////////////////////////////////////
    // constructors
  
    /**
     * The constructor.
     */
    public ActionExit() {
	super ("action.exit", NO_ICON);
	active = false;
    }
  
    ////////////////////////////////////////////////////////////////
    // main methods
  
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed (ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	Project p = ProjectManager.getManager().getCurrentProject();
    
	if (p != null && p.needsSave() && !active) {
	    active = true;
	    String t = 
		MessageFormat.format(Translator.localize(
			"optionpane.exit-save-changes-to"),
			new Object[] {p.getName()} );
	    int response = 
		JOptionPane.showConfirmDialog(pb, t, t, 
					      JOptionPane.YES_NO_CANCEL_OPTION);
      
	    if (response == JOptionPane.CANCEL_OPTION 
            	|| response == JOptionPane.CLOSED_OPTION) {
		active = false;
		return;
	    }
	    if (response == JOptionPane.YES_OPTION) {
		boolean safe = false;
        
		if (ActionSaveProject.getInstance().isEnabled()) {
		    safe = ActionSaveProject.getInstance().trySave (true);
		}
		if (!safe) {
		    safe = ActionSaveProjectAs.SINGLETON.trySave (false);
		}
		if (!safe) {
		    active = false;
		    return;
		}
	    }
	    active = false;
	}
	if (!active) {
	    Configuration.save();
	    doCommand(null);
	}
    }


    /**
     * Execute this action from the command line.
     *
     * @param argument is not used.
     * @return true if it is OK.
     */
    public boolean doCommand(String argument) {
        ArgoSecurityManager.getInstance().setAllowExit (true);
        System.exit (0);
	return true;
    }
} /* end class ActionExit */
