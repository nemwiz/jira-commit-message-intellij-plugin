package org.nemwiz.jiracommitmessage.services

import com.intellij.notification.BrowseNotificationAction
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import org.nemwiz.jiracommitmessage.configuration.InfixType
import org.nemwiz.jiracommitmessage.configuration.MessageWrapperType
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState
import org.nemwiz.jiracommitmessage.provider.PluginNotifier
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MyProjectService(private val project: Project) : Disposable {

    fun getTaskIdFromBranchName(branchName: String?): String? {
        val jiraProjectPrefixes = PluginSettingsState.instance.state.jiraProjectPrefixes

        if (jiraProjectPrefixes.isEmpty()) {
            val notifier = PluginNotifier()
            notifier.showWarning(
                project,
                "Missing configuration",
                "Please configure your JIRA project prefix under Settings > Tools > JIRA Id Commit Message",
                BrowseNotificationAction(
                    "Visit documentation",
                    "https://github.com/nemwiz/jira-commit-message-intellij-plugin"
                )
            )

            return ""
        }

        val selectedMessageWrapper = PluginSettingsState.instance.state.messageWrapperType
        val selectedInfixType = PluginSettingsState.instance.state.messageInfixType

        jiraProjectPrefixes.forEach { prefix ->
            run {
                val jiraPrefixRegex = branchName?.let { matchBranchNameThroughRegex(prefix, it) }

                return jiraPrefixRegex?.let {
                    if (jiraPrefixRegex.find()) {
                        return createCommitMessage(selectedMessageWrapper, selectedInfixType, jiraPrefixRegex)
                    } else {
                        return@forEach
                    }
                }
            }
        }

        return ""
    }

    private fun createCommitMessage(wrapperType: String, infixType: String, jiraPrefixRegex: Matcher): String {
        val messageWithWrapper = addWrapper(wrapperType, jiraPrefixRegex)
        return addInfix(infixType, messageWithWrapper)
    }

    private fun addWrapper(wrapperType: String, jiraPrefixRegex: Matcher) =
        if (wrapperType == MessageWrapperType.NO_WRAPPER.type) {
            jiraPrefixRegex.group(0)
        } else {
            String.format(
                Locale.US,
                "%s%s%s",
                wrapperType.substring(0, 1),
                jiraPrefixRegex.group(0),
                wrapperType.substring(1, 2)
            )
        }

    private fun addInfix(infixType: String, commitMessage: String) =
        if (infixType == InfixType.NO_INFIX.type) {
            commitMessage
        } else {
            String.format(
                Locale.US,
                "%s%s",
                commitMessage,
                infixType
            )
        }

    private fun matchBranchNameThroughRegex(valueToMatch: String?, branchName: String): Matcher? {
        val pattern = Pattern.compile(String.format(Locale.US, "%s+-[0-9]+", valueToMatch))
        return pattern.matcher(branchName)
    }

    override fun dispose() {
    }
}
