package org.nemwiz.jiracommitmessage.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable
import git4idea.GitUtil
import org.nemwiz.jiracommitmessage.services.MyProjectService

class PluginAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val currentProject = actionEvent.project

        if (currentProject != null) {
            val service = currentProject.service<MyProjectService>()

            val repositoryManager = GitUtil.getRepositoryManager(currentProject)
            val branch = repositoryManager.repositories[0].currentBranch

            val newCommitMessage = service.getTaskIdFromBranchName(branch?.name)
            getCommitPanel(actionEvent)?.setCommitMessage(newCommitMessage)
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
