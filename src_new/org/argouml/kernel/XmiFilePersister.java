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
package org.argouml.kernel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.cognitive.ProjectMemberTodoList;
import org.argouml.model.uml.UmlHelper;
import org.argouml.xml.xmi.XMIParser;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;

/**
 * To persist to and from XMI file storage.
 * 
 * @author Bob Tarling
 */
public class XmiFilePersister extends AbstractFilePersister {
    
    private static final Logger LOG = 
        Logger.getLogger(XmiFilePersister.class);
    
    /**
     * The constructor.
     * Sets the extensionname and the description. 
     */
    public XmiFilePersister() {
        desc = "XML Metadata Interchange";
    }

    /**
     * @see org.argouml.kernel.AbstractFilePersister#getExtension()
     */
    public String getExtension() {
        return "xmi";
    }
    
    /**
     * Save a project to a file in XMI format.
     * 
     * @param project the project to save.
     * @param file The file to write.
     * @throws SaveException if anything goes wrong.
     */
    public void save(Project project, File file)
        throws SaveException {
        
        project.setFile(file);
        project.setVersion(ArgoVersion.getVersion());

        if (expander == null) {
            Hashtable templates = TemplateReader.readFile(ARGO_TEE);
            expander = new OCLExpander(templates);
        }

        // frank: first backup the existing file to name+"#"
        File tempFile = new File( file.getAbsolutePath() + "#");
        File backupFile = new File( file.getAbsolutePath() + "~");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        
        BufferedWriter writer = null;
        try {
            if (file.exists()) {
                copyFile(tempFile, file);
            }
            // frank end
    
            writer =
                new BufferedWriter(new FileWriter(file));
    
            String path = file.getParent();
            if (LOG.isInfoEnabled()) {
                LOG.info("Dir ==" + path);
            }
            int size = project.getMembers().size();

            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = 
                    (ProjectMember) project.getMembers().elementAt(i);
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: "
                              + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getType());
                    }
                    projectMember.save(writer, null);
                }
            }
            
            // if save did not raise an exception 
            // and name+"#" exists move name+"#" to name+"~"
            // this is the correct backup file
            if (backupFile.exists()) {
                backupFile.delete();
            }
            if (tempFile.exists() && !backupFile.exists()) {
                tempFile.renameTo(backupFile);
            }
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (Exception e) {
            LOG.error("Exception occured during save attempt", e);
            try {
                writer.close();
            } catch (IOException ex) { }
            
            // frank: in case of exception 
            // delete name and mv name+"#" back to name if name+"#" exists
            // this is the "rollback" to old file
            file.delete();
            tempFile.renameTo( file);
            // we have to give a message to user and set the system to unsaved!
            throw new SaveException(e);
        }

        try {
            writer.close();
        } catch (IOException ex) {
            LOG.error("Failed to close save output writer", ex);
        }
    }
    
    
    /**   
     * This method creates a project from the specified URL
     *
     * Unlike the constructor which forces an .argo extension This
     * method will attempt to load a raw XMI file
     * 
     * This method can fail in several different ways. Either by
     * throwing an exception or by having the
     * ArgoParser.SINGLETON.getLastLoadStatus() set to not true.
     * 
     * @param url The URL to load the project from.
     * @return The newly loaded project.
     * @throws OpenException if the file can not be opened
     *
     * @see org.argouml.kernel.ProjectFilePersister#loadProject(java.net.URL)
     */
    public Project loadProject(URL url) throws OpenException {
        try {
            Project p = new Project();
            XMIParser.getSingleton().readModels(p, url);
            Object model = XMIParser.getSingleton().getCurModel();
            UmlHelper.getHelper().addListenersToModel(model);
            p.setUUIDRefs(XMIParser.getSingleton().getUUIDRefs());
            p.addMember(new ProjectMemberTodoList("", p));
            p.addMember(model);
            p.setNeedsSave(false);
            return p;
        } catch (IOException e) {
            throw new OpenException(e);
        }
    }
}
