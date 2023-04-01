package actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class CodeSearchActionV4 extends AnAction {

    private static final String SEARCH_URL_TEMPLATE = "https://www.stackoverflow.com/search?q=%s";

    public CodeSearchActionV4() {
        super("Stack Overflow Search ✅");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        String searchTerm = Messages.showInputDialog(project, "Enter a search term:", "Stack over Flow Search", Messages.getQuestionIcon());
        if (searchTerm == null || searchTerm.isEmpty()) {
            return;
        }
        String searchUrl = String.format(SEARCH_URL_TEMPLATE, searchTerm);
//        BrowserUtil.browse(searchUrl);
        showSidebar(project, searchUrl);
    }
    private void showSidebar(Project project, String searchUrl) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Stack over flow Search Results");
        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow("Stack over flow Search Results", true, ToolWindowAnchor.RIGHT);
        }
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);
        contentManager.addContent(ContentFactory.SERVICE.getInstance().createContent(createResultsPanel(searchUrl), null, false));
        toolWindow.show(null);
    }

    private JComponent createResultsPanel(String searchUrl) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    BrowserUtil.browse(e.getURL().toString());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(editorPane);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    editorPane.setPage(searchUrl);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return scrollPane;
    }
}
