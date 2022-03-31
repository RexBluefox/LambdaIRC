package com.lambda.commands

import com.lambda.client.command.ClientCommand
import com.lambda.client.manager.managers.FriendManager
import com.lambda.client.util.text.MessageSendHelper
import com.lambda.managers.ExampleManager

object TestCommand : ClientCommand(
    name = "test",
    description = "test command"
) {
    init {
        execute("Send Test") {
            MessageSendHelper.sendChatMessage("Test Successfull")
        }
    }
}