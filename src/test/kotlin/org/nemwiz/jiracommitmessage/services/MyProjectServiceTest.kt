package org.nemwiz.jiracommitmessage.services

import com.intellij.openapi.components.service
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState
import org.nemwiz.jiracommitmessage.provider.PluginNotifier

class MyProjectServiceTest : BasePlatformTestCase() {

    fun testShowsWarningPopupWhenJiraPrefixIsNotConfigured() {
        PluginSettingsState.instance.state.jiraProjectPrefixes = emptyList()

        mockkConstructor(PluginNotifier::class)
        every { anyConstructed<PluginNotifier>().showWarning(any(), any(), any(), any(), any()) } returns Unit

        val projectService = project.service<MyProjectService>()
        val taskId = projectService.getTaskIdFromBranchName()

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
}