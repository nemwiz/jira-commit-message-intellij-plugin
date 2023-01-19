package org.nemwiz.jiracommitmessage.services

import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import org.nemwiz.jiracommitmessage.configuration.InfixType
import org.nemwiz.jiracommitmessage.configuration.MessageWrapperType
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState
import org.nemwiz.jiracommitmessage.provider.PluginNotifier

class MyProjectServiceTest : BasePlatformTestCase() {

    fun testShowsWarningPopupWhenJiraProjectKeyIsNotConfigured() {
        PluginSettingsState.instance.state.jiraProjectKeys = emptyList()

        mockkConstructor(PluginNotifier::class)
        every { anyConstructed<PluginNotifier>().showWarning(any(), any(), any(), any()) } returns Unit

        val projectService = project.service<MyProjectService>()
        val taskId = projectService.getCommitMessageFromBranchName("")

        verify {
            anyConstructed<PluginNotifier>().showWarning(
                any(),
                "Missing configuration",
                "Please configure your JIRA project key under Settings > Tools > JIRA Id Commit Message",
                any()
            )
        }
        assertEquals("", taskId)
    }

    fun testMatchesTheProjectKeyWithBranchNameAndReturnsACommitMessage() {

        val dummyProjectKey1 = "PRJT"
        val dummyProjectKey2 = "LEGOS"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1, dummyProjectKey2)

        val mockBranch = "feat/${dummyProjectKey2}-8172"

        val projectService = project.service<MyProjectService>()

        val commitMessage = projectService.getCommitMessageFromBranchName(mockBranch)

        assertEquals("(${dummyProjectKey2}-8172)", commitMessage)
    }

    fun testWrapsTheCommitMessageInSelectedWrapper() {

        val dummyProjectKey1 = "MARCOM"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.CURLY.type

        val mockBranch = "this-is-an-awesome-branch-${dummyProjectKey1}-22"

        val projectService = project.service<MyProjectService>()

        val commitMessage = projectService.getCommitMessageFromBranchName(mockBranch)

        assertEquals("{${dummyProjectKey1}-22}", commitMessage)
    }

    fun testDoesNotWrapCommitMessageWhenNoWrapperIsSelected() {

        val dummyProjectKey1 = "MARCOM"
        val dummyProjectKey2 = "FINANCE"
        val dummyProjectKey3 = "LEGOS"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1, dummyProjectKey2, dummyProjectKey3)
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.NO_WRAPPER.type

        val mockBranch = "fix/${dummyProjectKey2}-381"

        val projectService = project.service<MyProjectService>()

        val commitMessage = projectService.getCommitMessageFromBranchName(mockBranch)

        assertEquals("${dummyProjectKey2}-381", commitMessage)
    }

    fun testAddsInfixToTheCommitMessageWhenInfixIsSpecified() {

        val dummyProjectKey1 = "INFIX"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.COLON.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.CURLY.type

        val mockBranch = "fix/${dummyProjectKey1}-999222"

        val projectService = project.service<MyProjectService>()

        var commitMessage = projectService.getCommitMessageFromBranchName(mockBranch)

        assertEquals("{${dummyProjectKey1}-999222}:", commitMessage)

        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.DASH_SPACE.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.NO_WRAPPER.type

        commitMessage = projectService.getCommitMessageFromBranchName(mockBranch)

        assertEquals("${dummyProjectKey1}-999222 -", commitMessage)
    }

    fun testNoInfixIsShownWhenInfixIsNotSpecified() {

        val dummyProjectKey1 = "FROG"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val mockBranch = "fix/${dummyProjectKey1}-123"

        val projectService = project.service<MyProjectService>()

        val commitMessage = projectService.getCommitMessageFromBranchName(mockBranch)

        assertEquals("(${dummyProjectKey1}-123)", commitMessage)
    }

    fun testReturnsEmptyStringWhenBranchNameIsNotSpecified() {

        val dummyProjectKey1 = "FROG"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val projectService = project.service<MyProjectService>()

        val commitMessage = projectService.getCommitMessageFromBranchName(null)

        assertEquals("", commitMessage)
    }
}