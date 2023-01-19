package org.nemwiz.jiracommitmessage.provider

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import git4idea.GitUtil
import org.nemwiz.jiracommitmessage.services.MyProjectService

class PluginProvider : CommitMessageProvider {

    override fun getCommitMessage(forChangelist: LocalChangeList, project: Project): String? {
        val oldCommitMessage = forChangelist.comment
        val projectService = project.service<MyProjectService>()

        val repositoryManager = GitUtil.getRepositoryManager(project)
        val branch = repositoryManager.repositories[0].currentBranch

        val newCommitMessage = projectService.getCommitMessageFromBranchName(branch?.name)

        return if (newCommitMessage == "") {
            oldCommitMessage
        } else {
            newCommitMessage
        }
    }
}
