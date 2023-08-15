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

        val changes = forChangelist.changes

        val branchName = plugin.extractBranchNameFromChanges(changes, repositoryManager)

        LOG.info("JIRA Commit message plugin provider - branch -> $branchName")

        val newCommitMessage = plugin.getCommitMessageFromBranchName(branchName)

        return if (newCommitMessage == "") {
            oldCommitMessage
        } else {
            newCommitMessage
        }
    }
}
