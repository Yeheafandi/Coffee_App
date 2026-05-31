package com.example.coffeeapp.data.model

data class CoffeeItemModel(
    val title: String = "",
    val description: String = "",
    val extra: String = "",
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val picUrl: String = "", // 🌟 جعلناها String مباشر لاستقبال الرابط الأول بأمان
    val categoryId: String = ""
)