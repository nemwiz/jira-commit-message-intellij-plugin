package org.nemwiz.jiracommitmessage.listener

import com.intellij.openapi.vcs.BranchChangeListener
import com.intellij.openapi.vcs.CheckinProjectPanel
import org.nemwiz.jiracommitmessage.services.JiraCommitMessagePlugin

class BranchCheckoutListener(
    private val panel: CheckinProjectPanel,
    private val plugin: JiraCommitMessagePlugin
) : BranchChangeListener {

    override fun branchWillChange(branchName: String) {
    }

    override fun branchHasChanged(branchName: String) {
        val commitMessage = plugin.getCommitMessageFromBranchName(branchName)
        panel.commitMessage = commitMessage
    }


}