package actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.wm.*;
import com.intellij.ui.components.*;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import java.awt.*;

public class CodeDisplayAction extends AnAction {

    public CodeDisplayAction() {
        super("Custom API Search \uD83E\uDD16");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        showSidebar(project);
    }

    private void showSidebar(Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Code Display");
        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow("Code Display", true, ToolWindowAnchor.RIGHT);
        }
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);
        contentManager.addContent(ContentFactory.SERVICE.getInstance().createContent(createInputPanel(toolWindow, contentManager), null, false));
        toolWindow.show(null);
    }

    private JComponent createInputPanel(ToolWindow toolWindow, ContentManager contentManager) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 25));
        JButton searchButton = new JButton("Display");
        searchButton.addActionListener(e -> {
            String query = textField.getText();
            if (!query.isEmpty()) {
                String code = getCode(query);
                contentManager.addContent(ContentFactory.SERVICE.getInstance().createContent(createCodePanel(code), null, false));
                toolWindow.show(null);
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Enter a query:"), BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.SOUTH);
        return panel;
    }

    private JComponent createCodePanel(String code) {
        JBScrollPane scrollPane = new JBScrollPane();
        JBTextArea textArea = new JBTextArea();
        textArea.setEditable(false);
        textArea.setText(code);
        scrollPane.setViewportView(textArea);
        return scrollPane;
    }

    private String getCode(String query) {
        // TODO: Implement code retrieval logic based on user's query
        return "public class Hello {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}";
    }
}
