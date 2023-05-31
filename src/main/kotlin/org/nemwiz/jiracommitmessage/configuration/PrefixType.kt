package org.nemwiz.jiracommitmessage.configuration

enum class PrefixType(val type: String) {
    NO_PREFIX("No prefix"),
    DASH("-"),
    COLON(":"),
    HASH("#"),
}