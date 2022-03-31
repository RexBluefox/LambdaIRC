package com.lambda

import com.lambda.client.LambdaMod
import com.lambda.client.plugin.api.Plugin
import com.lambda.client.util.threads.BackgroundJob
import com.lambda.commands.ExampleCommand
import com.lambda.commands.IrcCommands
import com.lambda.commands.TestCommand
import com.lambda.huds.ExampleLabelHud
import com.lambda.managers.ExampleManager
import com.lambda.modules.ExampleModule

internal object ExamplePlugin : Plugin() {

    override fun onLoad() {
        // Load any modules, commands, or HUD elements here
        //modules.add(ExampleModule)
        //commands.add(ExampleCommand)
        commands.add(TestCommand)
        commands.add(IrcCommands)
        //hudElements.add(ExampleLabelHud)
        //managers.add(ExampleManager)

        //bgJobs.add(BackgroundJob("ExampleJob", 10000L) { LambdaMod.LOG.info("Hello its me the BackgroundJob of your example plugin.") })
    }

    override fun onUnload() {
        // Here you can unregister threads etc...
    }
}