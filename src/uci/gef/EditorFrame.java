// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: EditorFrame.java
// Class: EditorFrame
// original author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import uci.graph.*;

/** Needs-More-Work: This is dead code and will be removed in the next
 *  release. */

public class EditorFrame extends ForwardingFrame {

  public EditorFrame(GraphModel gm) {
    super(new Dimension(400, 300));
    setEventHandler(new Editor(gm, this));
    ((Editor)getEventHandler()).frame(this);
  }

  public void addMenu(Menu m) { ((Editor)getEventHandler()).addMenu(m); }

} /* end class EditorFrame */
