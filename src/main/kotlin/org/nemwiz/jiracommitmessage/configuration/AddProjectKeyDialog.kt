package org.nemwiz.jiracommitmessage.configuration

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class AddProjectKeyDialog : DialogWrapper(true) {

    lateinit var addProjectKeyField: JBTextField

    init {
        init()
        title = "Add JIRA project key"
    }

    override fun createCenterPanel(): JComponent? {
        addProjectKeyField = JBTextField()

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Project key"), addProjectKeyField, 1, true)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun doValidate(): ValidationInfo? {
        if (addProjectKeyField.text.isNotEmpty()) {
            return null
        } else {
            return ValidationInfo("Project key is required")
        }
    }
}
