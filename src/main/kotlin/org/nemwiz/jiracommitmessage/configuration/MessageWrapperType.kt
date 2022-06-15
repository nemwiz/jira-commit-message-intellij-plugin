package org.nemwiz.jiracommitmessage.configuration

enum class MessageWrapperType(val type: String) {
    NO_WRAPPER("No wrapper"),
    ROUND("()"),
    CURLY("{}"),
    BOX("[]"),
    ANGLE("<>"),
    STAR("**"),
    FORWARD_SLASH("//"),
    BACKSLASH("\\\\"),
    VERTICAL_SLASH("||")
}
