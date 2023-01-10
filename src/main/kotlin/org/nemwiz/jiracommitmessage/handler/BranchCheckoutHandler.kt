package org.nemwiz.jiracommitmessage.handler

import com.intellij.openapi.vcs.BranchChangeListener
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory
import org.nemwiz.jiracommitmessage.listener.BranchCheckoutListener

class BranchCheckoutHandler : CheckinHandlerFactory() {
    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler {
        val messageBus = panel.project.messageBus.connect()
        messageBus.subscribe(BranchChangeListener.VCS_BRANCH_CHANGED, BranchCheckoutListener(panel))
        return object : CheckinHandler() {}
    }
}

