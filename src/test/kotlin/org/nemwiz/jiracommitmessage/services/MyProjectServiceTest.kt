package org.nemwiz.jiracommitmessage.services

import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import org.nemwiz.jiracommitmessage.configuration.MessageWrapperType
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState
import org.nemwiz.jiracommitmessage.provider.PluginNotifier

class MyProjectServiceTest : BasePlatformTestCase() {

    fun testShowsWarningPopupWhenJiraPrefixIsNotConfigured() {
        PluginSettingsState.instance.state.jiraProjectPrefixes = emptyList()

        mockkConstructor(PluginNotifier::class)
        every { anyConstructed<PluginNotifier>().showWarning(any(), any(), any(), any(), any()) } returns Unit

        val projectService = project.service<MyProjectService>()
        val taskId = projectService.getTaskIdFromBranchName("")

        verify {
            anyConstructed<PluginNotifier>().showWarning(
                any(),
                "Missing configuration",
                "",
                "Please configure your JIRA project prefix under Settings > Tools > JIRA Id Commit Message",
                any()
            )
        }
        assertEquals("", taskId)
    }

    fun testMatchesThePrefixNameWithBranchNameAndReturnsACommitMessage() {

        val dummyPrefix1 = "PRJT"
        val dummyPrefix2 = "LEGOS"
        PluginSettingsState.instance.state.jiraProjectPrefixes = listOf(dummyPrefix1, dummyPrefix2)

        val mockBranch = "feat/${dummyPrefix2}-8172"

        val projectService = project.service<MyProjectService>()

        val commitMessage = projectService.getTaskIdFromBranchName(mockBranch)

        assertEquals("(${dummyPrefix2}-8172)", commitMessage)
    }

    fun testWrapsTheCommitMessageInSelectedWrapper() {

        val dummyPrefix1 = "MARCOM"
        PluginSettingsState.instance.state.jiraProjectPrefixes = listOf(dummyPrefix1)
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.CURLY.type

        val mockBranch = "this-is-an-awesome-branch-${dummyPrefix1}-22"

        val projectService = project.service<MyProjectService>()

        val commitMessage = projectService.getTaskIdFromBranchName(mockBranch)

        assertEquals("{${dummyPrefix1}-22}", commitMessage)
    }

    fun testDoesNotWrapCommitMessageWhenNoWrapperIsSelected() {

        val dummyPrefix1 = "MARCOM"
        val dummyPrefix2 = "FINANCE"
        val dummyPrefix3 = "LEGOS"
        PluginSettingsState.instance.state.jiraProjectPrefixes = listOf(dummyPrefix1, dummyPrefix2, dummyPrefix3)
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.NO_WRAPPER.type

        val mockBranch = "fix/${dummyPrefix2}-381"

        val projectService = project.service<MyProjectService>()

        val commitMessage = projectService.getTaskIdFromBranchName(mockBranch)

        assertEquals("${dummyPrefix2}-381", commitMessage)
    }
}