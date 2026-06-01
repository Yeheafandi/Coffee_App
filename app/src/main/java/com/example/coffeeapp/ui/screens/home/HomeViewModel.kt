package com.example.coffeeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeapp.data.model.BannerModel
import com.example.coffeeapp.data.model.CategoryModel
import com.example.coffeeapp.data.model.CoffeeItemModel
import com.example.coffeeapp.data.remote.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class HomeViewModel : ViewModel() {

    private val repository = HomeRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }
            try {
                val bannersList = repository.getBanners()
                val categoriesList = repository.getCategories()
                val popularList = repository.getPopularItems()
                val allItemsList = repository.getItems()

                _uiState.update {
                    it.copy(
                        banners = bannersList,
                        categories = categoriesList,
                        popularItems = popularList,
                        items = allItemsList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "فشل تحميل بيانات المتجر: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun onCategorySelect(categoryId: Int) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun toggleFavorite(coffeeItem: CoffeeItemModel) {
        _uiState.update { currentState ->
            val currentFavorites = currentState.favoriteItems.toMutableList()

            val isAlreadyFavorite = currentFavorites.any { it.title == coffeeItem.title }

            if (isAlreadyFavorite) {
                currentFavorites.removeAll { it.title == coffeeItem.title }
            } else {
                currentFavorites.add(coffeeItem)
            }

            currentState.copy(favoriteItems = currentFavorites)
        }
    }
}