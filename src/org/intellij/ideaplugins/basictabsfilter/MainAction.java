/*
 * Copyright (c) 2008-2009 by Fuhrer Engineering AG, CH-2504 Biel/Bienne, Switzerland & Bas Leijdekkers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intellij.ideaplugins.basictabsfilter;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.stream.Stream;

public class MainAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project eventProject = getEventProject(event);
        if (eventProject == null) {
            return;
        }

        final FileEditorManagerImpl instance = (FileEditorManagerImpl) FileEditorManager.getInstance(eventProject);
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        final VirtualFile[] allOpenFiles = instance.getOpenFiles();
        for (VirtualFile file : allOpenFiles) {
            final String name = EditorUtil.INSTANCE.getText(eventProject, file);
            actionGroup.add(new SelectTabAction(name, file, eventProject));
        }

        ListPopup actionGroupPopup = JBPopupFactory.getInstance()
                                                   .createActionGroupPopup(
                                                           "Go To Tab",
                                                           actionGroup,
                                                           event.getDataContext(),
                                                           JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                                                           true);

        actionGroupPopup.showCenteredInCurrentWindow(eventProject);
    }

    @Override
    public void update(AnActionEvent event) {
        final DataContext dataContext = event.getDataContext();
        final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        event.getPresentation().setEnabled(project != null);
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}
