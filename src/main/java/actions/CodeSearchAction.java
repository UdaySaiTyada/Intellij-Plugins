package actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class CodeSearchAction extends AnAction {

    private static final String SEARCH_URL_TEMPLATE = "https://www.github.com/search?q=%s";

    public CodeSearchAction() {
        super("Google Search");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        String input = Messages.showInputDialog(project, "Enter a string:", "String Input", Messages.getQuestionIcon());
//        Messages.showInfoMessage(input, "Results");
        if (input != null && !input.isEmpty()) {
            String searchUrl = String.format(SEARCH_URL_TEMPLATE, input);
            BrowserUtil.browse(searchUrl);
            showSidebar(project, searchUrl);
        }

    }
    private void showSidebar(Project project, String searchUrl) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Google Search Results");
        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow("Google Search Results", true, ToolWindowAnchor.RIGHT);
        }
        ContentManager contentManager = toolWindow.getContentManager();
//        contentManager.removeAllContents(true);
        contentManager.addContent(ContentFactory.SERVICE.getInstance().createContent(createResultsPanel(searchUrl), null, false));
        toolWindow.show(null);
    }

    private JComponent createResultsPanel(String searchUrl) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new HyperlinkListener()
        {

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
