package org.nemwiz.jiracommitmessage.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable
import git4idea.GitUtil
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState
import org.nemwiz.jiracommitmessage.services.JiraCommitMessagePlugin

private val LOG = logger<PluginAction>()

class PluginAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val currentProject = actionEvent.project

        LOG.info("JIRA Commit message plugin action - project -> $currentProject")

        if (currentProject != null) {
            val plugin = currentProject.service<JiraCommitMessagePlugin>()

            val repositoryManager = GitUtil.getRepositoryManager(currentProject)
            val branch = repositoryManager.repositories[0].currentBranch

            LOG.info("JIRA Commit message plugin action - branch -> $branch")

            val newCommitMessage = plugin.getCommitMessageFromBranchName(branch?.name)
            setCommitMessage(actionEvent, newCommitMessage)
        }
    }

    fun setCommitMessage(actionEvent: AnActionEvent, newCommitMessage: String) {

        val commitPanel = actionEvent.dataContext.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT)

        if (PluginSettingsState.instance.state.isPrependJiraIssueOnActionClick) {
            val existingCommitMessage = commitPanel?.text
            commitPanel?.setText(String.format("%s%s", newCommitMessage, existingCommitMessage))
        } else {
            commitPanel?.setText(newCommitMessage)
        }
    }
}
