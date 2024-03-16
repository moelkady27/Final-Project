package com.example.finalproject.socket

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.finalproject.storage.AppReferences
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketHandler(
    context: Context
) {

    companion object {
        private const val TAG = "SocketHandler"
        private const val SERVER_URL = "https://home-finder-back-end-i7ca.onrender.com/"
    }

    val userIddddd = AppReferences.getUserId(context as Activity)

    private var socket: Socket? = null

    init {
        try {
            val options = IO.Options().apply {
                forceNew = true
                query="userId=$userIddddd"
            }
            socket = IO.socket(SERVER_URL, options)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing socket: ${e.message}")
        }
    }

    fun connect(callback: (Boolean) -> Unit) {
        socket?.connect()
        socket?.on(Socket.EVENT_CONNECT) {
            Log.d(TAG, "Socket connected")
            callback(true)
        }
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun sendMessage(receiverId: String, messageContent: String) {
        val jsonObject = JSONObject().apply {
            put("receiverId", receiverId)
            put("messageContent", messageContent)
            Log.e("messageContent" , messageContent)
        }
        socket?.emit("sendMessage", jsonObject)
    }

    fun stopListeningForMessages() {
        socket?.off("newMessage")
    }

    fun on(eventName: String, handler: (JSONObject) -> Unit) {
        try {
            socket?.on(eventName) { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    handler(data)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error listening to event: $eventName ----- $e")
        }
    }

}