package org.nemwiz.jiracommitmessage.listener

import com.intellij.openapi.vcs.BranchChangeListener
import com.intellij.openapi.vcs.CheckinProjectPanel
import org.nemwiz.jiracommitmessage.services.MyProjectService

class BranchCheckoutListener(
    private val panel: CheckinProjectPanel,
    private val service: MyProjectService
) : BranchChangeListener {

    override fun branchWillChange(branchName: String) {
    }

    override fun branchHasChanged(branchName: String) {
        val commitMessage = service.getTaskIdFromBranchName(branchName)

        if (commitMessage !== null) {
            panel.commitMessage = commitMessage
        }
    }


}