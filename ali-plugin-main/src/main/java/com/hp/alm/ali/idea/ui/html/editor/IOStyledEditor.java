package com.hp.alm.ali.idea.ui.html.editor;
// IOStyledEditor.java
// An extension of StyledEditor that adds document serialization.
//
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.io.*;

public class IOStyledEditor extends StyledEditor {

    public static void main(String[] args) {
        IOStyledEditor te = new IOStyledEditor();
        te.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        te.setVisible(true);
    }

    // Provide a new open action.
    protected Action getOpenAction() {
        if (inAction == null)
            inAction = new InAction();
        return inAction;
    }

    // Provide a new save action.
    protected Action getSaveAction() {
        if (outAction == null)
            outAction = new OutAction();
        return outAction;
    }

    private Action inAction;
    private Action outAction;

    // An action that saves the document as a serialized object
    class OutAction extends AbstractAction {
        public OutAction() {
            super("Serialize Out", new ImageIcon("icons/save.gif"));
        }

        public void actionPerformed(ActionEvent ev) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(IOStyledEditor.this) !=
                    JFileChooser.APPROVE_OPTION)
                return;
            File file = chooser.getSelectedFile();
            if (file == null)
                return;

            FileOutputStream writer = null;
            try {
                Document doc = getTextComponent().getDocument();
                writer = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(writer);
                oos.writeObject(doc);  // Write the document.
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(IOStyledEditor.this,
                        "File Not Saved", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException x) {}
                }
            }
        }
    }

    // An action that reads the document as a serialized object
    class InAction extends AbstractAction {
        public InAction() {
            super("Serialize In", new ImageIcon("icons/open.gif"));
        }
        public void actionPerformed(ActionEvent ev) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(IOStyledEditor.this) !=
                    JFileChooser.APPROVE_OPTION)
                return;
            File file = chooser.getSelectedFile();
            if (file == null)
                return;
            FileInputStream reader = null;
            try {
                reader = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(reader);
                Object o = ois.readObject();  // Read the document.
                getTextComponent().setDocument((Document)o);
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(IOStyledEditor.this,
                        "File Input Error", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(IOStyledEditor.this,
                        "Class Not Found", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException x) {}
                }
            }
        }
    }
}