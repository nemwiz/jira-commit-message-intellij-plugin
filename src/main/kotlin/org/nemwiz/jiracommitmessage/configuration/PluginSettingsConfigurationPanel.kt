package org.nemwiz.jiracommitmessage.configuration

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class PluginSettingsConfigurationPanel {

    var mainPanel: JPanel
    var messageWrapperTypeDropdown: ComboBox<String> = ComboBox()
    var infixTypeDropdown: ComboBox<String> = ComboBox()
    private var prefixes = PluginSettingsState.instance.state.jiraProjectPrefixes
    var prefixesList: JBList<String>
    var prefixesModel: CollectionListModel<String>
    private var toolbar: ToolbarDecorator

    init {
        messageWrapperTypeDropdown.addItem(MessageWrapperType.NO_WRAPPER.type)
        messageWrapperTypeDropdown.addItem(MessageWrapperType.ROUND.type)
        messageWrapperTypeDropdown.addItem(MessageWrapperType.BOX.type)
        messageWrapperTypeDropdown.addItem(MessageWrapperType.CURLY.type)
        messageWrapperTypeDropdown.addItem(MessageWrapperType.ANGLE.type)
        messageWrapperTypeDropdown.addItem(MessageWrapperType.STAR.type)
        messageWrapperTypeDropdown.addItem(MessageWrapperType.VERTICAL_SLASH.type)
        messageWrapperTypeDropdown.addItem(MessageWrapperType.FORWARD_SLASH.type)
        messageWrapperTypeDropdown.addItem(MessageWrapperType.BACKSLASH.type)

        infixTypeDropdown.addItem(InfixType.NO_INFIX.type)
        infixTypeDropdown.addItem(InfixType.DASH.type)
        infixTypeDropdown.addItem(InfixType.DASH_SPACE.type)
        infixTypeDropdown.addItem(InfixType.COLON.type)
        infixTypeDropdown.addItem(InfixType.COLON_SPACE.type)

        prefixesModel = CollectionListModel<String>(prefixes)
        prefixesList = JBList(prefixesModel)
        prefixesList.setEmptyText("No prefixes configured")

        toolbar = ToolbarDecorator.createDecorator(prefixesList).disableUpDownActions()
        toolbar.setAddAction {
            run {
                val addPrefixDialog = AddPrefixDialog()
                if (addPrefixDialog.showAndGet()) {
                    prefixesModel.add(addPrefixDialog.addPrefixField.text)
                }
            }
        }

        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Commit message bracket/wrapper type"), messageWrapperTypeDropdown, 1, false)
            .addLabeledComponent(JBLabel("Commit message infix"), infixTypeDropdown, 1, false)
            .addLabeledComponent(JBLabel("JIRA project prefixes"), toolbar.createPanel(), 1, true)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return messageWrapperTypeDropdown
    }
}
