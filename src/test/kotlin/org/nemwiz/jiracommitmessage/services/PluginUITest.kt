package org.nemwiz.jiracommitmessage.services

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.*
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.utils.Keyboard
import com.intellij.remoterobot.utils.waitFor
import org.junit.Ignore
import org.junit.Test
import java.time.Duration
import kotlin.test.assertEquals

class PluginUITest {

    val TWO_MINUTES = 2L

    @Test
    @Ignore
    fun generateCommitMessageWithPlugin() {

        val robot = RemoteRobot("http://127.0.0.1:8082")
        val keyboard = Keyboard(robot)

        cloneRepositoryAndWaitForProjectToLoad(robot)
        configurePluginWithJiraProjectKeys(robot, keyboard)
        switchToABranchWithJiraProjectKey(robot, keyboard)
        makeACodeChangeAndOpenCommitDialog(robot, keyboard)
        verifyCommitMessageAndPluginActions(robot, keyboard)
    }

    private fun verifyCommitMessageAndPluginActions(robot: RemoteRobot, keyboard: Keyboard) {
        val commitMessageField =
            robot.find<TextEditorFixture>(byXpath("//div[@class='MyEditorTextField']"), Duration.ofMinutes(TWO_MINUTES))
        assertEquals("(DUMMY-1234)", commitMessageField.editor.text)

        keyboard.enterText("sometext")

        assertEquals("sometext", commitMessageField.editor.text)

        robot.find<ComponentFixture>(byXpath("//div[@tooltiptext='JIRA Id Commit Message Action']")).click()

        assertEquals("(DUMMY-1234)", commitMessageField.editor.text)
    }

    private fun makeACodeChangeAndOpenCommitDialog(robot: RemoteRobot, keyboard: Keyboard) {
        robot.find<ComponentFixture>(byXpath("//div[@class='ProjectViewTree']")).doubleClick()

        keyboard.enterText("#change")

        robot.find<ComponentFixture>(byXpath("//div[contains(@tooltiptext.key, 'git4idea.vcs.name')]"))
            .click()

        robot.find<ComponentFixture>(byXpath("//div[contains(@myvisibleactions, 'By')]//div[contains(@myaction.key, 'action.CheckinProject.text')]"))
            .click()
    }

    private fun switchToABranchWithJiraProjectKey(robot: RemoteRobot, keyboard: Keyboard) {
        robot.find<ComponentFixture>(byXpath("//div[@class='MultipleTextValues']")).click()
        robot.find<JListFixture>(byXpath("//div[@class='MyList']")).clickItem("DUMMY-1234", false)

        keyboard.enter()
    }

    private fun configurePluginWithJiraProjectKeys(robot: RemoteRobot, keyboard: Keyboard) {
        robot.find<ComponentFixture>(
            byXpath("//div[contains(@text.key, 'group.FileMenu.text')]"),
            Duration.ofMinutes(TWO_MINUTES)
        ).click()

        keyboard.down()
        keyboard.down()
        keyboard.down()
        keyboard.down()
        keyboard.down()
        keyboard.enter()

        robot.find<JTreeFixture>(byXpath("//div[@class='MyTree']"), Duration.ofMinutes(TWO_MINUTES))
            .expandAll()
        robot.find<JTreeFixture>(byXpath("//div[@class='MyTree']"), Duration.ofMinutes(TWO_MINUTES))
            .clickRowWithText("JIRA Id Commit Message")
        robot.find<ComponentFixture>(
            byXpath("//div[contains(@myaction.key, 'button.add.a')]"),
            Duration.ofMinutes(TWO_MINUTES)
        ).click()
        robot.find<JTextFieldFixture>(byXpath("//div[@class='JBTextField']")).text = "DUMMY"
        robot.find<ComponentFixture>(byXpath("//div[@class='CustomFrameDialogContent'][.//div[@class='JBTextField']]//div[@text.key='button.ok']"))
            .click()
        robot.find<ComponentFixture>(
            byXpath(
                "//div[contains(@" +
                    "text.key, 'button.apply')]"
            )
        ).click()
        robot.find<ComponentFixture>(byXpath("//div[@text.key='button.ok']")).click()
    }

    private fun cloneRepositoryAndWaitForProjectToLoad(robot: RemoteRobot) {
        robot.find<ComponentFixture>(byXpath("//div[@accessiblename.key='action.Vcs.VcsClone.text']")).click()
        robot.find<JTextFieldFixture>(byXpath("//div[@class='BorderlessTextField']")).text =
            "https://github.com/nemwiz/jira-commit-message-intellij-plugin.git"

        robot.find<ComponentFixture>(byXpath("//div[@text.key='clone.dialog.clone.button']")).click()

        waitFor(
            Duration.ofMinutes(TWO_MINUTES * 3),
            Duration.ofSeconds(10),
            "Project to load",
            { "" },
            {
                robot.find<ComponentFixture>(
                    byXpath(
                        "//div[@class='Tree']"
                    ),
                    Duration.ofMinutes(TWO_MINUTES * 3)

                ).data.hasText("finished")
            })
    }

}