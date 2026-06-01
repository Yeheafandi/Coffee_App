package com.example.coffeeapp.ui.screens.home

import com.example.coffeeapp.data.model.BannerModel
import com.example.coffeeapp.data.model.CategoryModel
import com.example.coffeeapp.data.model.CoffeeItemModel

data class HomeUiState(
    val banners: List<BannerModel> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val popularItems: List<CoffeeItemModel> = emptyList(),
    val items: List<CoffeeItemModel> = emptyList(),
    val favoriteItems: List<CoffeeItemModel> = emptyList(),
    val selectedCategoryId: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)