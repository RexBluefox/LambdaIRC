package com.lambda.commands

import com.lambda.client.LambdaMod
import com.lambda.client.command.ClientCommand
import com.lambda.client.command.commands.SayCommand.string
import com.lambda.client.util.text.MessageSendHelper
import java.io.PrintWriter
import java.net.Socket
import java.util.*

object IrcCommands : ClientCommand(
    name = "irc",
    description = "IRC Commands"
) {


    init {
        val socket = Socket("irc.esper.net", 5555)
        LambdaMod.LOG.info("created socket")
        val out = PrintWriter(socket.getOutputStream(), true)
        LambdaMod.LOG.info("created output")
        val input = Scanner(socket.getInputStream())
        LambdaMod.LOG.info("created input")
        LambdaMod.LOG.info("Created write function")
        literal("start") {
            execute("Start Client") {
                write("NICK", mc.session.username, out)
                write("USER", "${mc.session.username} 0 * :${mc.session.username}", out);
                LambdaMod.LOG.info("Join: nick")
                MessageSendHelper.sendChatMessage("Joined IRC as ${mc.session.username}")
                var t1 = readMessages(input, out)
                t1.start()
                LambdaMod.LOG.info("Started thread 1")
            }
        }

        literal("send", "s") {
           string("message") { msgArg ->
                execute("Send Hi") {
                    //send(arg.value)
                    write("PRIVMSG", "#2b2t ${msgArg.value}", out)
                    LambdaMod.LOG.info("${mc.session.username.bold()}: ${msgArg.value}")
                    MessageSendHelper.sendChatMessage("${mc.session.username.bold()}: ${msgArg.value}")
                }
            }
        }

        literal("join", "j") {
            string("channel") { arg ->
                execute("Join") {
                    write("JOIN", arg.value, out)
                    LambdaMod.LOG.info("Join: joined ${arg.value}")
                }
            }
        }
    }

    class readMessages(input: Scanner, out: PrintWriter) : Thread() {
        var inputNew = input
        var out = out
        override fun run() {
            while (inputNew.hasNext()) {
                val serverMessage: String = inputNew.nextLine()
                if("PRIVMSG" in serverMessage) {
                    val tiles: List<String> = serverMessage.split(" ")
                    val all_user_data = tiles[0]
                    val command = tiles[1]
                    val channelname = tiles[2]
                    var messageRaw = tiles.subList(3, tiles.size)
                    var name = all_user_data.split("!")[0]
                    name = name.substring(1, name.length)
                    var message = messageRaw.joinToString(" ")
                    message = message.substring(1,message.length)
                    //name = name.bold()
                    //MessageSendHelper.sendChatMessage("<<< $serverMessage")
                    MessageSendHelper.sendChatMessage("${name.bold()}: $message")
                    LambdaMod.LOG.info("LambdaIRC: $name sent $message")
                }
                else{
                    MessageSendHelper.sendChatMessage("INFO: $serverMessage")
                    LambdaMod.LOG.info("<<< $serverMessage")
                }
                if (serverMessage.startsWith("PING")) {
                    val pingContents = serverMessage.split(" ")[1]
                    LambdaMod.LOG.info("Pingcontent: $pingContents")
                    write("PONG", pingContents, out)
                }
            }
        }
    }

    fun write(command: String, message: String, out: PrintWriter) {
        val fullmessage = "$command $message"
        out.print("$fullmessage \r\n")
        out.flush()
    }
}

private fun String.bold(): String {
    var String = "§l$this§r"
    return String
}







