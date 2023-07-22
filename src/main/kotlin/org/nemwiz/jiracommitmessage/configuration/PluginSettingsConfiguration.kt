package org.nemwiz.jiracommitmessage.configuration

import com.intellij.openapi.options.Configurable
import com.intellij.ui.CollectionListModel
import javax.swing.JComponent

class PluginSettingsConfiguration : Configurable {

    private lateinit var pluginSettingsConfigurationPanel: PluginSettingsConfigurationPanel
    private val pluginSettingsState
        get() = PluginSettingsState.instance.state

    override fun createComponent(): JComponent {
        pluginSettingsConfigurationPanel = PluginSettingsConfigurationPanel()
        return pluginSettingsConfigurationPanel.mainPanel
    }

    override fun isModified(): Boolean {
        return pluginSettingsConfigurationPanel
            .messageWrapperTypeDropdown.selectedItem != pluginSettingsState.messageWrapperType ||
            pluginSettingsConfigurationPanel.prefixTypeDropdown.selectedItem != pluginSettingsState.messagePrefixType ||
            pluginSettingsConfigurationPanel.infixTypeDropdown.selectedItem != pluginSettingsState.messageInfixType ||
            pluginSettingsConfigurationPanel.isConventionalCommitCheckbox.isSelected != pluginSettingsState.isConventionalCommit ||
            pluginSettingsConfigurationPanel.isAutoDetectJiraProjectKeyCheckbox.isSelected != pluginSettingsState.isAutoDetectJiraProjectKey ||
            setOf(pluginSettingsConfigurationPanel.projectKeysModel.items) != setOf(pluginSettingsState.jiraProjectKeys)
    }

    override fun apply() {
        pluginSettingsState.messageWrapperType = pluginSettingsConfigurationPanel
            .messageWrapperTypeDropdown
            .selectedItem
            .toString()
        pluginSettingsState.messagePrefixType = pluginSettingsConfigurationPanel
            .prefixTypeDropdown
            .selectedItem
            .toString()
        pluginSettingsState.messageInfixType = pluginSettingsConfigurationPanel
            .infixTypeDropdown
            .selectedItem
            .toString()
        pluginSettingsState.isConventionalCommit =
            pluginSettingsConfigurationPanel.isConventionalCommitCheckbox.isSelected
        pluginSettingsState.isAutoDetectJiraProjectKey =
            pluginSettingsConfigurationPanel.isAutoDetectJiraProjectKeyCheckbox.isSelected
        pluginSettingsState.jiraProjectKeys = pluginSettingsConfigurationPanel.projectKeysModel.items
    }

    override fun getDisplayName(): String {
        return "JIRA Id Commit Message"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return pluginSettingsConfigurationPanel.getPreferredFocusedComponent()
    }

    override fun reset() {
        pluginSettingsConfigurationPanel
            .messageWrapperTypeDropdown
            .selectedItem = pluginSettingsState.messageWrapperType
        pluginSettingsConfigurationPanel
            .prefixTypeDropdown
            .selectedItem = pluginSettingsState.messagePrefixType
        pluginSettingsConfigurationPanel
            .infixTypeDropdown
            .selectedItem = pluginSettingsState.messageInfixType
        pluginSettingsConfigurationPanel
            .isConventionalCommitCheckbox
            .isSelected = pluginSettingsState.isConventionalCommit
        pluginSettingsConfigurationPanel
            .isAutoDetectJiraProjectKeyCheckbox
            .isSelected = pluginSettingsState.isAutoDetectJiraProjectKey
        pluginSettingsConfigurationPanel.projectKeysModel = CollectionListModel(pluginSettingsState.jiraProjectKeys)
        pluginSettingsConfigurationPanel.projectKeysList.model = pluginSettingsConfigurationPanel.projectKeysModel
    }
}
