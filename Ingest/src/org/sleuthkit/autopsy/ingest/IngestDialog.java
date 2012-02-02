/*
 * Autopsy Forensic Browser
 * 
 * Copyright 2011 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.ingest;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.sleuthkit.autopsy.casemodule.AddImageAction;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.datamodel.Image;

/**
 * IngestDialog shown on Case.CASE_ADD_IMAGE property change
 */
public class IngestDialog extends JDialog implements PropertyChangeListener{
    
    private static IngestDialog instance;
    private static final String TITLE = "Welcome";
    private static Dimension DIMENSIONS = new Dimension(300, 300);
    private Image image;

    private IngestDialog(JFrame frame, String title) {
        super(frame, title, true);
    }

    /**
     * Get the startup window
     * @return the startup window singleton
     */
    public static synchronized IngestDialog getInstance() {
        if (IngestDialog.instance == null) {
            JFrame frame = new JFrame(TITLE);
            IngestDialog.instance = new IngestDialog(frame, TITLE);
        }
        return instance;
    }

    /**
     * Shows the startup window.
     */
    public void display() {

        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

        // set the popUp window / JFrame
        setSize(DIMENSIONS);
        int w = this.getSize().width;
        int h = this.getSize().height;

        // set the location of the popUp Window on the center of the screen
        setLocation((screenDimension.width - w) / 2, (screenDimension.height - h) / 2);

        IngestDialogPanel panel = new IngestDialogPanel(image);

        // add the command to close the window to the button on the Volume Detail Panel
        panel.setCloseButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        panel.setStartButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        add(panel);
        pack();
        setResizable(false);
        setVisible(true);

    }

    /**
     * Closes the startup window.
     */
    public void close() {
        this.dispose();
    }
    
    IngestDialog(){
        Case.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changed = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();
        
        if(changed.equals(AddImageAction.WIZARD_COMPLETE)){
            this.image = (Image) newValue;
            display();
        }
    }
}
