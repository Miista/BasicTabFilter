package org.intellij.ideaplugins.basictabsfilter

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ArrayUtil

class SelectTabAction(val name: String, val file: VirtualFile, project: Project)
        : com.intellij.openapi.actionSystem.AnAction(name) {

    private val editorManager: FileEditorManagerImpl by lazyOf( FileEditorManager.getInstance(project) as FileEditorManagerImpl )

    override fun actionPerformed(event: AnActionEvent?) {
        val tab = findTab() ?: return

        editorManager.addSelectionRecord(file, tab);
        editorManager.openFileImpl2(tab, file, true);
    }

    private fun findTab(): EditorWindow? {
        var tab = editorManager.selectionHistory.find { p -> p.first.equals(this.file) }?.second
        return findAppropriateEditorWindow(tab)
    }

    private fun findAppropriateEditorWindow(info: EditorWindow?): EditorWindow? {
        val windows = info?.owner?.windows ?: return null;
        return when {
            ArrayUtil.contains(info, windows) -> info
            windows.size > 0 -> windows[0]
            else -> null
        }
    }
}