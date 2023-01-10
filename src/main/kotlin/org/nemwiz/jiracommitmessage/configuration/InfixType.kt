package org.nemwiz.jiracommitmessage.configuration

enum class InfixType(val type: String) {
    NO_INFIX("No infix"),
    DASH("-"),
    DASH_SPACE(" -"),
    COLON(":"),
    COLON_SPACE(" :"),
}