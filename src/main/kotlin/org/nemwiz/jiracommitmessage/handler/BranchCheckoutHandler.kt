package org.nemwiz.jiracommitmessage.handler

import com.intellij.openapi.components.service
import com.intellij.openapi.vcs.BranchChangeListener
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory
import org.nemwiz.jiracommitmessage.listener.BranchCheckoutListener
import org.nemwiz.jiracommitmessage.services.JiraCommitMessagePlugin

class BranchCheckoutHandler : CheckinHandlerFactory() {
    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler {
        val plugin = panel.project.service<JiraCommitMessagePlugin>()
        val messageBus = panel.project.messageBus.connect(plugin)
        messageBus.subscribe(BranchChangeListener.VCS_BRANCH_CHANGED, BranchCheckoutListener(panel, plugin))
        return object : CheckinHandler() {}
    }
}

