package org.intellij.ideaplugins.basictabsfilter

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object EditorUtil {
    public fun getText(eventProject: Project, file: VirtualFile): String {
        val manager = FileEditorManager.getInstance(eventProject) as FileEditorManagerImpl

        val selectionHistory = manager.selectionHistory
        if (selectionHistory.size == 0) {
            return file.name
        }

        val editorWindow = selectionHistory.first().second
        return editorWindow.tabbedPane?.tabs?.let {
            val index = editorWindow.findFileIndex(file)
            it.getTabAt(index).text
        } ?: file.name
    }
}