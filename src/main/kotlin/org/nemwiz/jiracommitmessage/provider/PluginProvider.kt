package org.nemwiz.jiracommitmessage.provider

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import git4idea.GitUtil
import org.nemwiz.jiracommitmessage.services.JiraCommitMessagePlugin

class PluginProvider : CommitMessageProvider {

    override fun getCommitMessage(forChangelist: LocalChangeList, project: Project): String? {
        val oldCommitMessage = forChangelist.comment
        val plugin = project.service<JiraCommitMessagePlugin>()

        val repositoryManager = GitUtil.getRepositoryManager(project)
        val branch = repositoryManager.repositories[0].currentBranch

        val newCommitMessage = plugin.getCommitMessageFromBranchName(branch?.name)

        return if (newCommitMessage == "") {
            oldCommitMessage
        } else {
            newCommitMessage
        }
    }
}
