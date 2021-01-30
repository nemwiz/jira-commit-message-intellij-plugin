package org.nemwiz.jiracommitmessage.configuration

enum class MessageWrapperType(val type: String) {
    ROUND("()"),
    CURLY("{}"),
    BOX("[]"),
    ANGLE("<>"),
    STAR("**"),
    FORWARD_SLASH("//"),
    BACKSLASH("\\\\"),
    VERTICAL_SLASH("||")
}
