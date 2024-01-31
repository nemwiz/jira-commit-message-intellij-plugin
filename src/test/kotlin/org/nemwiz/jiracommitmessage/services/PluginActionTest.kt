package org.nemwiz.jiracommitmessage.services

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.mockk.*
import org.nemwiz.jiracommitmessage.action.PluginAction
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState

class PluginActionTest : BasePlatformTestCase() {

    fun testAppendsJiraIssueToExistingCommitMessageWhenThisOptionIsEnabled() {

        val commitPanelMock = mockkClass(Document::class)
        val refreshablePanelMock = mockkClass(Refreshable::class)
        val actionEventMock = mockkClass(AnActionEvent::class)

        val existingCommitMessage = "This message was already shown in the panel"
        every { commitPanelMock.text } returns existingCommitMessage

        every { Refreshable.PANEL_KEY.getData(actionEventMock.dataContext) } returns refreshablePanelMock
        every { actionEventMock.dataContext.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT) } returns commitPanelMock

        every { commitPanelMock.setText(any()) } just Runs

        PluginSettingsState.instance.state.isPrependJiraIssueOnActionClick = true

        val newCommitMessage = "(PROJ-456):"

        val pluginAction = PluginAction()

        pluginAction.setCommitMessage(actionEventMock, newCommitMessage)

        verify { commitPanelMock.setText(String.format("%s%s", newCommitMessage, existingCommitMessage)) }
    }

    fun testDoesNotAppendJiraIssueToExistingCommitMessageWhenThisOptionIsDisabled() {

        val commitPanelMock = mockkClass(Document::class)
        val refreshablePanelMock = mockkClass(Refreshable::class)
        val actionEventMock = mockkClass(AnActionEvent::class)

        every { Refreshable.PANEL_KEY.getData(actionEventMock.dataContext) } returns refreshablePanelMock
        every { actionEventMock.dataContext.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT) } returns commitPanelMock

        every { commitPanelMock.setText(any()) } just Runs

        PluginSettingsState.instance.state.isPrependJiraIssueOnActionClick = false
        val newCommitMessage = "(PROJ-456):"

        val pluginAction = PluginAction()

        pluginAction.setCommitMessage(actionEventMock, newCommitMessage)

        verify { commitPanelMock.setText(newCommitMessage) }
    }

}