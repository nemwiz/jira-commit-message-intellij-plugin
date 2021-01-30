package org.nemwiz.jiracommitmessage.services

import com.intellij.openapi.project.Project
import git4idea.GitLocalBranch
import git4idea.GitUtil.getRepositoryManager
import org.nemwiz.jiracommitmessage.configuration.PluginSettingsState
import java.util.regex.Matcher
import java.util.regex.Pattern

class MyProjectService(private val project: Project) {

    fun getTaskIdFromBranchName(): String? {
        val jiraProjectPrefix = PluginSettingsState.instance.state.jiraProjectPrefix
        val selectedMessageWrapper = PluginSettingsState.instance.state.messageWrapperType
        val repositoryManager = getRepositoryManager(project)
        val branch = repositoryManager.repositories[0].currentBranch
        val matcher = branch?.let { matchBranchNameThroughRegex(jiraProjectPrefix, it) }

        return matcher?.let {
            return if (matcher.find()) {
                String.format("%s%s%s",
                    selectedMessageWrapper.substring(0, 1),
                    matcher.group(0),
                    selectedMessageWrapper.substring(1, 2)
                )
            } else {
                ""
            }
        }
    }

    private fun matchBranchNameThroughRegex(valueToMatch: String?, branch: GitLocalBranch): Matcher? {
        val pattern = Pattern.compile(String.format("%s+-[0-9]+", valueToMatch))
        return pattern.matcher(branch.name)
    }
}
