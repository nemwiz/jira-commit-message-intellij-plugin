package org.nemwiz.jiracommitmessage.configuration

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class AddPrefixDialog : DialogWrapper(true) {

    lateinit var addPrefixField: JBTextField

    init {
        init()
        title = "Add JIRA prefix"
    }

    override fun createCenterPanel(): JComponent? {
        addPrefixField = JBTextField()

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Prefix"), addPrefixField, 1, true)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun doValidate(): ValidationInfo? {
        if (addPrefixField.text.isNotEmpty()) {
            return null
        } else {
            return ValidationInfo("Prefix is required")
        }
    }
}