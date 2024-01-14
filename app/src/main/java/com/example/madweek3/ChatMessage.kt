package com.example.madweek3

data class ChatMessage(
    val message: String,
    val name: String,
    val isOwnMessage: Boolean // true if the message is sent by the user, false otherwise
)