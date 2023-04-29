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

class JiraCommitMessagePluginTest : BasePlatformTestCase() {

    fun testShowsWarningPopupWhenJiraProjectKeyIsNotConfigured() {
        PluginSettingsState.instance.state.jiraProjectKeys = emptyList()
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = false

        mockkConstructor(PluginNotifier::class)
        every { anyConstructed<PluginNotifier>().showWarning(any(), any(), any(), any()) } returns Unit

        val plugin = project.service<JiraCommitMessagePlugin>()
        val taskId = plugin.getCommitMessageFromBranchName("")

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
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = false

        val mockBranch1 = "hello/${dummyProjectKey2}-8172"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage1 = plugin.getCommitMessageFromBranchName(mockBranch1)

        assertEquals("(${dummyProjectKey2}-8172)", commitMessage1)

        val mockBranch2 = "hello/${dummyProjectKey1}_123-foo"

        val commitMessage2 = plugin.getCommitMessageFromBranchName(mockBranch2)

        assertEquals("(${dummyProjectKey1}_123)", commitMessage2)
    }

    fun testWrapsTheCommitMessageInSelectedWrapper() {

        val dummyProjectKey1 = "MARCOM"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = false
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.CURLY.type

        val mockBranch = "this-is-an-awesome-branch-${dummyProjectKey1}-22"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage = plugin.getCommitMessageFromBranchName(mockBranch)

        assertEquals("{${dummyProjectKey1}-22}", commitMessage)
    }

    fun testDoesNotWrapCommitMessageWhenNoWrapperIsSelected() {

        val dummyProjectKey1 = "MARCOM"
        val dummyProjectKey2 = "FINANCE"
        val dummyProjectKey3 = "LEGOS"
        PluginSettingsState.instance.state.jiraProjectKeys =
            listOf(dummyProjectKey1, dummyProjectKey2, dummyProjectKey3)
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = false
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.NO_WRAPPER.type

        val mockBranch = "fix/${dummyProjectKey2}-381"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage = plugin.getCommitMessageFromBranchName(mockBranch)

        assertEquals("${dummyProjectKey2}-381", commitMessage)
    }

    fun testAddsInfixToTheCommitMessageWhenInfixIsSpecified() {

        val dummyProjectKey1 = "INFIX"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = false
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.COLON.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.CURLY.type

        val mockBranch = "fix/${dummyProjectKey1}-999222"

        val plugin = project.service<JiraCommitMessagePlugin>()

        var commitMessage = plugin.getCommitMessageFromBranchName(mockBranch)

        assertEquals("{${dummyProjectKey1}-999222}:", commitMessage)

        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.DASH_SPACE.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.NO_WRAPPER.type

        commitMessage = plugin.getCommitMessageFromBranchName(mockBranch)

        assertEquals("${dummyProjectKey1}-999222 -", commitMessage)
    }

    fun testNoInfixIsShownWhenInfixIsNotSpecified() {

        val dummyProjectKey1 = "FROG"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = false
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val mockBranch = "fix/${dummyProjectKey1}-123"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage = plugin.getCommitMessageFromBranchName(mockBranch)

        assertEquals("(${dummyProjectKey1}-123)", commitMessage)
    }

    fun testReturnsEmptyStringWhenBranchNameIsNotSpecified() {

        val dummyProjectKey1 = "FROG"
        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage = plugin.getCommitMessageFromBranchName(null)

        assertEquals("", commitMessage)
    }

    fun testAutomaticJiraProjectKeyDetection() {
        val dummyProjectKey1 = "PRODUCT"
        PluginSettingsState.instance.state.jiraProjectKeys = emptyList()
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = true
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val mockBranch1 = "feat/this-${dummyProjectKey1}-123-is-a-cool-feature"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage1 = plugin.getCommitMessageFromBranchName(mockBranch1)

        assertEquals("(${dummyProjectKey1}-123)", commitMessage1)

        val mockBranch2 = "fix/${dummyProjectKey1}_123_ui-error"

        val commitMessage2 = plugin.getCommitMessageFromBranchName(mockBranch2)

        assertEquals("(${dummyProjectKey1}_123)", commitMessage2)
    }

    fun testReturnsEmptyStringWhenBranchNameDoesNotMatchAnyConfiguredJiraProjectKeys() {
        val dummyProjectKey1 = "MARCOM"
        val dummyProjectKey2 = "FINANCE"
        val dummyProjectKey3 = "LEGOS"

        PluginSettingsState.instance.state.jiraProjectKeys =
            listOf(dummyProjectKey1, dummyProjectKey2, dummyProjectKey3)
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = false
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val mockBranch = "feat/this-does-not-exist"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage = plugin.getCommitMessageFromBranchName(mockBranch)

        assertEquals("", commitMessage)
    }

    fun testReturnsEmptyStringWhenJiraProjectKeyCantBeAutomaticallyDetectedFromBranchName() {
        PluginSettingsState.instance.state.jiraProjectKeys = emptyList()
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = true
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val mockBranch = "fix/some-bug"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage = plugin.getCommitMessageFromBranchName(mockBranch)

        assertEquals("", commitMessage)
    }

    fun testReturnConventionalCommitInfixWhenConvetionalCommitsOptionIsOn() {
        val dummyProjectKey1 = "LEGOS"

        PluginSettingsState.instance.state.jiraProjectKeys = listOf(dummyProjectKey1)
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = false
        PluginSettingsState.instance.state.isConventionalCommit = true
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val mockBranch1 = "feat/awesome-LEGOS-123"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage1 = plugin.getCommitMessageFromBranchName(mockBranch1)

        assertEquals("feat(${dummyProjectKey1}-123)", commitMessage1)

        val mockBranch2 = "chore-awesome-LEGOS-123"

        val commitMessage2 = plugin.getCommitMessageFromBranchName(mockBranch2)

        assertEquals("chore(${dummyProjectKey1}-123)", commitMessage2)
    }

    fun testReturnConventionalCommitInfixWhenConvetionalCommitsOptionIsOnAndAutomaticDetectIsOn() {
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = true
        PluginSettingsState.instance.state.isConventionalCommit = true
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.COLON.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val mockBranch1 = "fix/awesome-PLUGIN-831"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage1 = plugin.getCommitMessageFromBranchName(mockBranch1)

        assertEquals("fix(PLUGIN-831):", commitMessage1)

        val mockBranch2 = "docs-awesome-PLUGIN-831"

        val commitMessage2 = plugin.getCommitMessageFromBranchName(mockBranch2)

        assertEquals("docs(PLUGIN-831):", commitMessage2)
    }

    fun testDoesNotAddConventionalCommitInfixWhenConventionalCommitsAreNotOn() {
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = true
        PluginSettingsState.instance.state.isConventionalCommit = false
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.BOX.type

        val mockBranch = "test/awesome-PLUGIN-831"

        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessage = plugin.getCommitMessageFromBranchName(mockBranch)

        assertEquals("[PLUGIN-831]", commitMessage)
    }

    fun testOnlyAddsInfixFromTheSupportedConventionalCommitInfix() {
        PluginSettingsState.instance.state.isAutoDetectJiraProjectKey = true
        PluginSettingsState.instance.state.isConventionalCommit = true
        PluginSettingsState.instance.pluginState.messageInfixType = InfixType.NO_INFIX.type
        PluginSettingsState.instance.state.messageWrapperType = MessageWrapperType.ROUND.type

        val branches = listOf(
            "test/PROJ-123",
            "ci/PROJ-123",
            "chore/PROJ-123",
            "build/PROJ-123",
            "fix/PROJ-123",
            "feat/PROJ-123",
            "docs/PROJ-123",
            "perf/PROJ-123",
            "refactor/PROJ-123",
            "style/PROJ-123",
            "unknown/PROJ-123",
            "dummy/PROJ-123",
        )


        val plugin = project.service<JiraCommitMessagePlugin>()

        val commitMessages = branches.map { b ->  plugin.getCommitMessageFromBranchName(b)}

        assertEquals(branches.size, commitMessages.size)

        assertFalse(commitMessages.contains("dummy(PROJ-123)"))
        assertFalse(commitMessages.contains("unknown(PROJ-123)"))

        assertTrue(commitMessages.contains("test(PROJ-123)"))
        assertTrue(commitMessages.contains("ci(PROJ-123)"))
        assertTrue(commitMessages.contains("chore(PROJ-123)"))
        assertTrue(commitMessages.contains("build(PROJ-123)"))
        assertTrue(commitMessages.contains("fix(PROJ-123)"))
        assertTrue(commitMessages.contains("feat(PROJ-123)"))
        assertTrue(commitMessages.contains("docs(PROJ-123)"))
        assertTrue(commitMessages.contains("perf(PROJ-123)"))
        assertTrue(commitMessages.contains("refactor(PROJ-123)"))
        assertTrue(commitMessages.contains("style(PROJ-123)"))
    }
}