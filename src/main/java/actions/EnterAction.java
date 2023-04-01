package actions;

import com.intellij.codeInsight.template.LiveTemplateBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.ui.Messages;
import org.codehaus.groovy.control.messages.Message;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EnterAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
//        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
//        String random_text = "szdxcfvgbhnjmk,o";
//        editor.getDocument().insertString(editor.getDocument().getTextLength(), random_text);

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        // Generate a random string using UUID
        String randomString = UUID.randomUUID().toString();
        // Insert the generated string at the current cursor position
        EditorModificationUtil.insertStringAtCaret(editor, randomString);
    }
}
