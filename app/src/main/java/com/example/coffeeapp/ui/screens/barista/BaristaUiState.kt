package com.example.coffeeapp.ui.screens.barista



data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

data class BaristaUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage("مرحباً بك في ركن الذكاء الاصطناعي المطور ☕✨. أنا الباريستا الذكي الخاص بك. أخبرني كيف تبدو درجات الحرارة اليوم أو كيف هو مزاجك وسأقترح لك المشروب المثالي فوراً!", false)
    ),
    val inputText: String = "",
    val isAiThinking: Boolean = false
)