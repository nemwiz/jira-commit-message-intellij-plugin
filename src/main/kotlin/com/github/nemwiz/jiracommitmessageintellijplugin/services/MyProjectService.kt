package com.github.nemwiz.jiracommitmessageintellijplugin.services

import com.intellij.openapi.project.Project
import com.github.nemwiz.jiracommitmessageintellijplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
