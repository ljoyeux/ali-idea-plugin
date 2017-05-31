package com.hp.alm.ali.idea.ui.html.editor;

import javax.swing.*;

/**
 * Created by joyeuxl on 31.05.2017.
 */
public class HTMLUtils {

    public static ImageIcon load(String name) {
        return new ImageIcon(HTMLUtils.class.getResource("/" + name));
    }
}
