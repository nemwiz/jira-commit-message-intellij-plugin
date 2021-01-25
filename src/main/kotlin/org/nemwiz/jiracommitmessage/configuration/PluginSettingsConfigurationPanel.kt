package org.nemwiz.jiracommitmessage.configuration

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class PluginSettingsConfigurationPanel {

    var mainPanel: JPanel
    var jiraProjectPrefixField: JBTextField = JBTextField()
    var commitPrefixFormatField: JBTextField = JBTextField()

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("JIRA project prefix "), jiraProjectPrefixField, 1, false)
            .addLabeledComponent(JBLabel("Commit prefix format "), commitPrefixFormatField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return jiraProjectPrefixField
    }
}
