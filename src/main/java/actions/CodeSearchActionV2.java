package actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class CodeSearchActionV2 extends AnAction {

    private static final String SEARCH_URL_TEMPLATE = "https://www.google.com/search?q=";

    public CodeSearchActionV2() {
        super("Google Search");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        showSidebar(project);
    }
    private void showSidebar(Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Google Search");
        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow("Google Search", true, ToolWindowAnchor.RIGHT);
        }
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);
        contentManager.addContent(ContentFactory.SERVICE.getInstance().createContent(createInputPanel(toolWindow), null, false));
        toolWindow.show(null);
    }

    private JComponent createInputPanel(ToolWindow toolWindow) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 25));
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String query = textField.getText();
            if (!query.isEmpty()) {
                searchInGoogle(query);
                toolWindow.hide(null);
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Enter a query:"), BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.SOUTH);
        return panel;
    }

    private void searchInGoogle(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            URI uri = new URI("https://www.google.com/search?q=" + encodedQuery);
            java.awt.Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
