package dev.protsenko.securityLinter.utils

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.executeCommand
import com.intellij.openapi.project.Project

inline fun modifyPsi(project: Project, crossinline command: () -> Unit){
    ApplicationManager.getApplication().invokeLater {
        WriteAction.run<Throwable> {
            executeCommand(project) {
                command()
            }
        }
    }
}