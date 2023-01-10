package org.nemwiz.jiracommitmessage.provider

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project

class PluginNotifier {

    fun showWarning(project: Project?, title: String, message: String, action: AnAction) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("JIRA Id Commit Message Notification Group")
            .createNotification(message, NotificationType.WARNING)
            .setTitle(title)
            .addAction(action)
            .notify(project)
    }
}
