package com.hp.alm.ali.idea.cfg;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joyeuxl on 22.05.2017.
 */
public class FontField extends ComboField {
    public FontField(String defaultValue) {
        String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        List<String> fonts = new ArrayList<String >();
        fonts.add("");
        fonts.addAll(Arrays.asList(availableFonts));
        setModel(new DefaultComboBoxModel<String>(fonts.toArray(new String[0])));
        getModel().setSelectedItem(defaultValue!=null ? defaultValue : fonts.get(0));
    }
}
