package org.nemwiz.jiracommitmessage.configuration

enum class WritePositionType(val type: String) {
    POST_PEND("Postpend"),
    PRE_PEND("Prepend"),
    OVERWRITE("Overwrite")
}
