/*
 * Copyright 2013 Hewlett-Packard Development Company, L.P
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

package com.hp.alm.ali.idea.ui.editor.field;

import com.hp.alm.ali.idea.action.UndoAction;
import com.hp.alm.ali.idea.cfg.AliConfiguration;
import com.hp.alm.ali.idea.cfg.AliProjectConfiguration;
import com.hp.alm.ali.idea.services.ProjectUserService;
import com.hp.alm.ali.idea.util.DateUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.components.JBScrollPane;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentField extends BaseField {

    public static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String HTML_END_RX = "^(.*)(</body>\\s*</html>\\s*)$";

    private JPanel commentPanel;
    private JTextPane addedComment;

    private String userName;
    private String fullName;
    private String fontName;

    public CommentField(Project project, String label, String value) {
        super(label, false, value);

        this.userName = project.getComponent(AliProjectConfiguration.class).getUsername();
        this.fullName = project.getComponent(ProjectUserService.class).getUserFullName(userName);
        this.fontName = project.getComponent(AliProjectConfiguration.class).getFont();

        addedComment = new JTextPane();
        addedComment.getDocument().addDocumentListener(new MyDocumentListener(this));
        UndoAction.installUndoRedoSupport(addedComment);
        HTMLAreaField.installNavigationShortCuts(addedComment);

        commentPanel = new JPanel(new BorderLayout());
        if(!value.isEmpty()) {
            Splitter splitter = new Splitter(true, 0.75f);
            splitter.setHonorComponentsMinimumSize(true);
            splitter.setShowDividerControls(true);
            splitter.setFirstComponent(createPane(HTMLAreaField.createTextPane(value)));
            splitter.setSecondComponent(createPane(addedComment));
            commentPanel.add(splitter, BorderLayout.CENTER);
        } else {
            commentPanel.add(createPane(addedComment), BorderLayout.CENTER);
        }
    }

    public Component getComponent() {
        return commentPanel;
    }

    public String getValue() {
        return mergeComment(getOriginalValue(), addedComment.getText(), userName, fullName, fontName);
    }

    public boolean isDisableDefaultAction() {
        return true;
    }

    @Override
    public void setValue(String value) {
        addedComment.setText(value);
    }

    private JBScrollPane createPane(Component component) {
        JBScrollPane pane = new JBScrollPane(component);
        pane.setPreferredSize(new Dimension(600, 30));
        pane.setMinimumSize(new Dimension(300, 30));
        return pane;
    }

    public boolean hasChanged() {
        return !addedComment.getText().isEmpty();
    }

    public static String mergeComment(String existingComment, String newComment, String userName, String fullName, String fontName) {
        if(newComment.isEmpty()) {
            return existingComment;
        }

        if(existingComment == null) {
            existingComment = "";
        }

        String html_end = "";
        Matcher matcher = Pattern.compile(HTML_END_RX, Pattern.MULTILINE | Pattern.DOTALL).matcher(existingComment);
        if(matcher.matches()) {
            html_end = matcher.group(2);
            existingComment = matcher.replaceAll(Matcher.quoteReplacement(matcher.group(1)));
        }

        StringBuilder font = new StringBuilder("<font");
        if(fontName!=null && !fontName.trim().isEmpty()) {
            font.append(" face=\"").append(fontName).append("\"");
        }
        font.append(" color=\"#000080\">");

        StringBuffer sb = new StringBuffer();
        // neither latest ALM nor AGM store the outer-most html tags, simply add to what we have found
        sb.append(existingComment);
        sb.append("<br>").append(font).append("<b>________________________________________</b></font><br>");
        sb.append(font).append("<b>");

        if (fullName != null) {
            sb.append(fullName);
            sb.append(" &lt;");
            sb.append(userName);
            sb.append("&gt;");
        } else {
            sb.append(userName);
        }
        sb.append(", ");
        sb.append(DateUtils.SHORT_LOCALE_DATE_FORMAT.format(new Date()));
        sb.append(":</b></font><br>");

        newComment = StringEscapeUtils.escapeHtml3(newComment);
        newComment= newComment.replaceAll("\n", "<br>").replaceAll("\r", "").replaceAll("\t", "        ");
        sb.append(newComment);

        sb.append(html_end);
        return sb.toString();
    }
}
