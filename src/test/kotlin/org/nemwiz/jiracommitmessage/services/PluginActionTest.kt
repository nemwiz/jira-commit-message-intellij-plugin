package org.nemwiz.jiracommitmessage.services

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.openapi.vcs.ui.Refreshable
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.mockk.*
import org.nemwiz.jiracommitmessage.action.PluginAction
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState

class PluginActionTest : BasePlatformTestCase() {

    fun testAppendsJiraIssueToExistingCommitMessageWhenThisOptionIsEnabled() {

        val commitPanelMock = mockkClass(CommitMessage::class)
        val refreshablePanelMock = mockkClass(Refreshable::class)
        val actionEventMock = mockkClass(AnActionEvent::class)
        val documentMock = mockkClass(Document::class)

        val existingCommitMessage = "This message was already shown in the panel"
        every { documentMock.text } returns existingCommitMessage

        every { Refreshable.PANEL_KEY.getData(actionEventMock.dataContext) } returns refreshablePanelMock
        every { VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(actionEventMock.dataContext) } returns commitPanelMock
        every { actionEventMock.dataContext.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT) } returns documentMock

        every { commitPanelMock.setCommitMessage(any()) } just Runs

        PluginSettingsState.instance.state.isPrependJiraIssueOnActionClick = true

        val newCommitMessage = "(PROJ-456):"

        val pluginAction = PluginAction()

        pluginAction.setCommitMessage(actionEventMock, newCommitMessage)

        verify { commitPanelMock.setCommitMessage(String.format("%s %s", newCommitMessage, existingCommitMessage)) }
    }

    fun testDoesNotAppendJiraIssueToExistingCommitMessageWhenThisOptionIsDisabled() {

        val commitPanelMock = mockkClass(CommitMessage::class)
        val refreshablePanelMock = mockkClass(Refreshable::class)
        val actionEventMock = mockkClass(AnActionEvent::class)

        every { Refreshable.PANEL_KEY.getData(actionEventMock.dataContext) } returns refreshablePanelMock
        every { VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(actionEventMock.dataContext) } returns commitPanelMock

        every { commitPanelMock.setCommitMessage(any()) } just Runs

        PluginSettingsState.instance.state.isPrependJiraIssueOnActionClick = false
        val newCommitMessage = "(PROJ-456):"

        val pluginAction = PluginAction()

        pluginAction.setCommitMessage(actionEventMock, newCommitMessage)

        verify { commitPanelMock.setCommitMessage(newCommitMessage) }
    }

}