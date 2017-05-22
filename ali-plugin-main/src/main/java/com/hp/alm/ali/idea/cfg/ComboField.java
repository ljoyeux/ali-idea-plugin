package com.hp.alm.ali.idea.cfg;

import javax.swing.*;
import javax.swing.text.Document;

/**
 * Created by joyeuxl on 22.05.2017.
 */
public class ComboField extends JComboBox<String> implements ConfigurationField {

    @Override
    public String getValue() {
        return getModel().getSelectedItem().toString();
    }

    @Override
    public void setValue(String value) {
        getModel().setSelectedItem(value);
    }

    @Override
    public Document getDocument() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getText() { return getModel().getSelectedItem().toString(); }
}
