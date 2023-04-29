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
private const val CONVENTIONAL_COMMITS_REGEX = "(feat|fix|build|ci|chore|docs|perf|refactor|style|test)"

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

        val isConventionalCommit = PluginSettingsState.instance.state.isConventionalCommit

        val jiraIssue = extractJiraIssueFromBranch(isAutoDetectProjectKey, branchName, jiraProjectKeys)
        val conventionalCommitType = extractConventionalCommitType(isConventionalCommit, branchName)

        val selectedMessageWrapper = PluginSettingsState.instance.state.messageWrapperType
        val selectedInfixType = PluginSettingsState.instance.state.messageInfixType

        return if (jiraIssue != null)
            createCommitMessage(selectedMessageWrapper, selectedInfixType, jiraIssue, conventionalCommitType)
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

    private fun extractConventionalCommitType(isConventionalCommit: Boolean, branchName: String): String? {
        if (isConventionalCommit) {
            val pattern = Pattern.compile(CONVENTIONAL_COMMITS_REGEX).toRegex()
            val matchedConventionalType = pattern.find(branchName)
            return matchedConventionalType?.value
        }
        return null
    }

    private fun createCommitMessage(
        wrapperType: String,
        infixType: String,
        jiraIssue: String,
        conventionalCommitType: String?
    ): String {
        val messageWithWrapper = addWrapper(wrapperType, jiraIssue)
        val messageWithInfix = addInfix(infixType, messageWithWrapper)
        return addConventionalCommitType(conventionalCommitType, messageWithInfix)
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

    private fun addConventionalCommitType(conventionalCommitType: String?, commitMessage: String): String =
        if (conventionalCommitType != null) {
            String.format(Locale.US, "%s%s", conventionalCommitType, commitMessage)
        } else commitMessage


    override fun dispose() {
    }
}
