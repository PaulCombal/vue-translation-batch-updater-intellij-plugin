package com.github.paulcombal.vuetranslationbatchupdaterintellijplugin.actions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessOutputType
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
// import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement

abstract class BaseTranslateAction : AnAction() {

    private val NOTIFICATION_GROUP = NotificationGroupManager.getInstance().getNotificationGroup("my.plugin.notification.group")

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiElement = getPsiElement(e) ?: return
        val keyElement = findKeyElement(psiElement) ?: return

        val fullKeyPath = getFullKeyPath(keyElement)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val virtualFile = psiFile.virtualFile ?: return
        val filePath = virtualFile.path

        // Messages.showMessageDialog(project, "Full Key: $fullKeyPath\nFile: $filePath", "Running Auto-Translate...", Messages.getInformationIcon())

        // Launch a background task to run the command
        object : Task.Backgroundable(project, "Running auto-translate command", true) {
            override fun run(indicator: com.intellij.openapi.progress.ProgressIndicator) {
                try {
                    val commandLine = GeneralCommandLine()
                        .withWorkDirectory(project.basePath)
                        .withExePath("yarn")
                        .withParameters("exec", "tt", "--default-language=json", "--auto-translate=$fullKeyPath", filePath)
                        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)

                    val processHandler = OSProcessHandler(commandLine.createProcess(), commandLine.commandLineString)

                    val errorOutput = StringBuilder()

                    processHandler.addProcessListener(object : ProcessListener {
                        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                            if (outputType == ProcessOutputType.STDERR) {
                                errorOutput.append(event.text)
                            } else {
                                println(event.text)
                            }
                        }

                        override fun processTerminated(event: ProcessEvent) {
                            if (event.exitCode != 0) {
                                NOTIFICATION_GROUP.createNotification(
                                    "Command failed with exit code ${event.exitCode}",
                                    errorOutput.toString(),
                                    NotificationType.ERROR
                                ).notify(project)
                            } else {
                                ApplicationManager.getApplication().invokeLater {
                                    refreshFile(virtualFile, project)
                                }
                            }
                        }
                    })

                    processHandler.startNotify()

                } catch (ex: Exception) {
                    NOTIFICATION_GROUP.createNotification(
                        "Error running command",
                        "An error occurred: ${ex.message}",
                        NotificationType.ERROR
                    ).notify(project)
                }
            }
        }.queue()
    }

    override fun update(e: AnActionEvent) {
        val psiElement = getPsiElement(e)
        e.presentation.isEnabledAndVisible = psiElement != null && isKeyElement(psiElement)
    }

    private fun getPsiElement(e: AnActionEvent): PsiElement? {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return null
        val file = e.getData(CommonDataKeys.PSI_FILE) ?: return null
        val offset = editor.caretModel.offset

        // Check for injected languages first (e.g., inside Vue files)
        val injectedLanguageManager = InjectedLanguageManager.getInstance(file.project)
        val injectedElement = injectedLanguageManager.findInjectedElementAt(file, offset)

        return injectedElement ?: file.findElementAt(offset)
    }

    private fun refreshFile(virtualFile: VirtualFile, project: Project) {
        ApplicationManager.getApplication().invokeLater {
            virtualFile.refresh(true, false) {
                val fileDocumentManager = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance()
                val document = fileDocumentManager.getDocument(virtualFile)
                if (document != null) {
                    fileDocumentManager.reloadFromDisk(document)
                }
            }
        }
    }

    // Abstract methods to be implemented by subclasses
    protected abstract fun findKeyElement(psiElement: PsiElement): PsiElement?
    protected abstract fun getFullKeyPath(keyElement: PsiElement): String
    protected abstract fun isKeyElement(psiElement: PsiElement): Boolean
}