package org.nemwiz.jiracommitmessage.services

import org.nemwiz.jiracommitmessage.configuration.InfixType
import org.nemwiz.jiracommitmessage.configuration.MessageWrapperType
import java.util.*

class CommitMessageBuilder(private val jiraIssue: String?) {

    private var commitMessage: String = ""

    init {
        jiraIssue?.let { commitMessage = jiraIssue }
    }

    fun withWrapper(wrapper: String): CommitMessageBuilder {
        if (wrapper != MessageWrapperType.NO_WRAPPER.type) {
            commitMessage = String.format(
                Locale.US,
                "%s%s%s",
                wrapper.substring(0, 1),
                commitMessage,
                wrapper.substring(1, 2)
            )
        }
        return this
    }

    fun withInfix(infix: String): CommitMessageBuilder {
        if (infix !== InfixType.NO_INFIX.type) {
            commitMessage = String.format(
                Locale.US,
                "%s%s",
                commitMessage,
                infix
            )
        }
        return this
    }

    fun withConventionalCommit(conventionalCommitType: String?): CommitMessageBuilder {
        conventionalCommitType?.let {
            commitMessage = String.format(Locale.US, "%s%s", conventionalCommitType, commitMessage)
        }
        return this
    }

    fun getCommitMessage(): String {
        return if (jiraIssue == null) "" else commitMessage
    }

}