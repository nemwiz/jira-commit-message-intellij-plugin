package org.nemwiz.jiracommitmessage.provider

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import git4idea.GitUtil
import org.nemwiz.jiracommitmessage.services.JiraCommitMessagePlugin

private val LOG = logger<PluginProvider>()

class PluginProvider : CommitMessageProvider {

    override fun getCommitMessage(forChangelist: LocalChangeList, project: Project): String? {

        LOG.info("JIRA Commit message plugin provider - oldCommitMessage -> $forChangelist.comment")

        val oldCommitMessage = forChangelist.comment
        val plugin = project.service<JiraCommitMessagePlugin>()

        val repositoryManager = GitUtil.getRepositoryManager(project)
        var newCommitMessage = ""

        if (repositoryManager.repositories.getOrNull(0) != null) {
            val branch = repositoryManager.repositories[0].currentBranch
            LOG.info("JIRA Commit message plugin provider - branch -> $branch")
            newCommitMessage = plugin.getCommitMessageFromBranchName(branch?.name)
        } else {
            LOG.info("JIRA Commit message plugin provider - unable to find repository for this project")
        }

        return if (newCommitMessage == "") {
            oldCommitMessage
        } else {
            newCommitMessage
        }
    }
}
