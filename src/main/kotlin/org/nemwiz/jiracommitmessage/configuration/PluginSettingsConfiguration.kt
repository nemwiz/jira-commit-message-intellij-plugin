package org.nemwiz.jiracommitmessage.configuration

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class PluginSettingsConfiguration : Configurable {

    private lateinit var pluginSettingsConfigurationPanel: PluginSettingsConfigurationPanel
    private val pluginSettingsState
        get() = PluginSettingsState.instance.state

    override fun createComponent(): JComponent? {
        pluginSettingsConfigurationPanel = PluginSettingsConfigurationPanel()
        return pluginSettingsConfigurationPanel.mainPanel
    }

    override fun isModified(): Boolean {
        return pluginSettingsConfigurationPanel.jiraProjectPrefixField.text !=
            pluginSettingsState.jiraProjectPrefix
    }

    override fun apply() {
        pluginSettingsState.jiraProjectPrefix = pluginSettingsConfigurationPanel.jiraProjectPrefixField.text
    }

    override fun getDisplayName(): String {
        return "JIRA Id Commit Message"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return pluginSettingsConfigurationPanel.getPreferredFocusedComponent()
    }

    override fun reset() {
        pluginSettingsConfigurationPanel.jiraProjectPrefixField.text = pluginSettingsState.jiraProjectPrefix
    }
}
