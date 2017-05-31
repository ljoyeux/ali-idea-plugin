package com.hp.alm.ali.idea.ui.html.editor;

// HTMLExample.java
// A very simple browser using the basic HTML support built into JEditorPane.
//
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

public class HTMLExample {
    public static void main(String[] args) {
        JEditorPane pane = null;
        try {
            pane = new JEditorPane(args[0]);
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
        pane.setEditable(false);

        // Add a hyperlink listener.
        final JEditorPane finalPane = pane;
        pane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent ev) {
                try {
                    if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                        finalPane.setPage(ev.getURL());
                } catch (IOException ex) { ex.printStackTrace(System.err); }
            }
        });

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new JScrollPane(pane));
        frame.setSize(350,400);
        frame.setVisible(true);
    }
}