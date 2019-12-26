package com.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.plugin.ui.MainDialog;

/**
 * @author YongQc
 * .
 * @date 2019/12/25 17:25
 *
 * ProtobufToolAction :
 */
public class ProtobufToolAction extends AnAction {

    private MainDialog dialog;

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getProject(e);
        if (project == null)
            return;
//        if (dialog == null){
            dialog = new MainDialog(project);
//        }

        dialog.setVisible(true);
    }

    public static Project getProject(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null)
            project = ProjectManager.getInstance().getOpenProjects()[0];
        if (project == null)
            Messages.showMessageDialog(project, "Project still loading please re-run with an active project", "Information", Messages.getInformationIcon());
        return project;
    }
}
