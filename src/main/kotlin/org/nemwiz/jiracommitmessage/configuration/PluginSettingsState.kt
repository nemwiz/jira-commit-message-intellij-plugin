package org.nemwiz.jiracommitmessage.configuration

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "CommitMessageStoredConfiguration", storages = [Storage("CommitMessageStoredConfiguration.xml")])
class PluginSettingsState : PersistentStateComponent<PluginSettingsState.PluginState> {

    var pluginState: PluginState = PluginState()

    override fun getState(): PluginState {
        return pluginState
    }

    override fun loadState(state: PluginState) {
        XmlSerializerUtil.copyBean(state, this.pluginState)
    }

    companion object {
        val instance: PluginSettingsState
            get() = ApplicationManager.getApplication().getService(PluginSettingsState::class.java)
    }

    class PluginState {
        var messageWrapperType = MessageWrapperType.ROUND.type
        var messageInfixType = InfixType.NO_INFIX.type
        var jiraProjectKeys = emptyList<String>()
    }
}
