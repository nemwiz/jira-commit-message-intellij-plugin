package org.nemwiz.jiracommitmessage.provider

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import org.nemwiz.jiracommitmessage.services.MyProjectService

class PluginProvider : CommitMessageProvider {

    override fun getCommitMessage(forChangelist: LocalChangeList?, project: Project?): String? {
        val oldCommitMessage = forChangelist?.comment
        val projectService = project?.getService(MyProjectService::class.java)
        val newCommitMessage = projectService?.getTaskIdFromBranchName()

        return if (newCommitMessage.equals("")) {
            oldCommitMessage
        } else {
            newCommitMessage
        }
    }

}
