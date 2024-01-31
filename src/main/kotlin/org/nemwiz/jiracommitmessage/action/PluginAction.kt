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

        val commitPanel = getCommitPanel(actionEvent)

        if (PluginSettingsState.instance.state.isPrependJiraIssueOnActionClick) {
            val existingCommitMessage = actionEvent.dataContext.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT)?.text
            commitPanel?.setCommitMessage(String.format("%s%s", newCommitMessage, existingCommitMessage))
        } else {
            commitPanel?.setCommitMessage(newCommitMessage)
        }
    }

    private fun getCommitPanel(actionEvent: AnActionEvent): CommitMessageI? {
        val data = Refreshable.PANEL_KEY.getData(actionEvent.dataContext)

        if (data is CommitMessageI) {
            return data
        }

        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(actionEvent.dataContext)
    }
}
