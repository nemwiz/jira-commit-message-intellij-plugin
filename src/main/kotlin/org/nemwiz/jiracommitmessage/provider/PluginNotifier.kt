package org.nemwiz.jiracommitmessage.provider

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project

class PluginNotifier {

    fun showWarning(project: Project?, title: String, subtitle: String, message: String, actions: List<AnAction>) {
        val group = NotificationGroup("JIRA Id Commit Message Notification Group", NotificationDisplayType.BALLOON)
        val notification = group.createNotification(title, subtitle, message, NotificationType.WARNING)

        for (action in actions) {
            notification.addAction(action)
        }

        notification.notify(project)
    }
}
