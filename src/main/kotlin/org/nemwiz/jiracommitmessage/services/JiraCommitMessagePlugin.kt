package org.nemwiz.jiracommitmessage.services

import com.intellij.notification.BrowseNotificationAction
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import org.nemwiz.jiracommitmessage.configuration.InfixType
import org.nemwiz.jiracommitmessage.configuration.MessageWrapperType
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState
import org.nemwiz.jiracommitmessage.provider.PluginNotifier
import java.util.*
import java.util.regex.Pattern

private const val DEFAULT_REGEX_FOR_JIRA_PROJECT_ISSUES = "([A-Z]+[_-][0-9]+)"

class JiraCommitMessagePlugin(private val project: Project) : Disposable {

    fun getCommitMessageFromBranchName(branchName: String?): String {

        if (branchName == null) {
            return ""
        }

        val jiraProjectKeys = PluginSettingsState.instance.state.jiraProjectKeys
        val isAutoDetectProjectKey = PluginSettingsState.instance.state.isAutoDetectJiraProjectKey

        if (!isAutoDetectProjectKey && jiraProjectKeys.isEmpty()) {
            val notifier = PluginNotifier()
            notifier.showWarning(
                project,
                "Missing configuration",
                "Please configure your JIRA project key under Settings > Tools > JIRA Id Commit Message",
                BrowseNotificationAction(
                    "Visit documentation",
                    "https://github.com/nemwiz/jira-commit-message-intellij-plugin"
                )
            )

            return ""
        }

        val jiraIssue = extractJiraIssueFromBranch(isAutoDetectProjectKey, branchName, jiraProjectKeys)

        val selectedMessageWrapper = PluginSettingsState.instance.state.messageWrapperType
        val selectedInfixType = PluginSettingsState.instance.state.messageInfixType

        return if (jiraIssue != null)
            createCommitMessage(selectedMessageWrapper, selectedInfixType, jiraIssue)
        else ""
    }

    private fun extractJiraIssueFromBranch(
        isAutoDetectProjectKey: Boolean,
        branchName: String,
        jiraProjectKeys: List<String>
    ): String? {

        var jiraIssue: String? = null

        if (isAutoDetectProjectKey) {
            val pattern = Pattern.compile(DEFAULT_REGEX_FOR_JIRA_PROJECT_ISSUES).toRegex()
            val matchedJiraIssue = pattern.find(branchName)
            jiraIssue = matchedJiraIssue?.value
        } else {
            for (projectKey in jiraProjectKeys) {
                val pattern = Pattern.compile(String.format(Locale.US, "%s+[_-][0-9]+", projectKey)).toRegex()
                val matchedJiraIssue = pattern.find(branchName)

                if (matchedJiraIssue != null) {
                    jiraIssue = matchedJiraIssue.value
                    break
                }
            }
        }

        return jiraIssue
    }

    private fun createCommitMessage(wrapperType: String, infixType: String, jiraIssue: String): String {
        val messageWithWrapper = addWrapper(wrapperType, jiraIssue)
        return addInfix(infixType, messageWithWrapper)
    }

    private fun addWrapper(wrapperType: String, jiraIssue: String) =
        if (wrapperType == MessageWrapperType.NO_WRAPPER.type) {
            jiraIssue
        } else {
            String.format(
                Locale.US,
                "%s%s%s",
                wrapperType.substring(0, 1),
                jiraIssue,
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

    override fun dispose() {
    }
}
