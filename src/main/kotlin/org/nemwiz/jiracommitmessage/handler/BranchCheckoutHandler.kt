package org.nemwiz.jiracommitmessage.handler

import com.intellij.openapi.components.service
import com.intellij.openapi.vcs.BranchChangeListener
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory
import org.nemwiz.jiracommitmessage.listener.BranchCheckoutListener
import org.nemwiz.jiracommitmessage.services.MyProjectService

class BranchCheckoutHandler : CheckinHandlerFactory() {
    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler {
        val service = panel.project.service<MyProjectService>()
        val messageBus = panel.project.messageBus.connect(service)
        messageBus.subscribe(BranchChangeListener.VCS_BRANCH_CHANGED, BranchCheckoutListener(panel, service))
        return object : CheckinHandler() {}
    }
}

