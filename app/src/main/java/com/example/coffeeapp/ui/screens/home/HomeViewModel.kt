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

// كائن الحالة الموحد للشاشة الرئيسية تماشياً مع معايير Lab 6
data class HomeUiState(
    val banners: List<BannerModel> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val popularItems: List<CoffeeItemModel> = emptyList(),
    val items: List<CoffeeItemModel> = emptyList(), // القائمة المضافة لفلترتها بناءً على الفئة
    val favoriteItems: List<CoffeeItemModel> = emptyList(), // 🌟 القائمة الجديدة لتخزين عناصر المفضلة ديناميكياً
    val selectedCategoryId: Int = 0, // لتتبع الفئة النشطة عند الفلترة
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

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
                // جلب البيانات بالتوازي من Firebase
                val bannersList = repository.getBanners()
                val categoriesList = repository.getCategories()
                val popularList = repository.getPopularItems()
                val allItemsList = repository.getItems() // جلب المشروبات من عقدة Items في الـ Firebase

                _uiState.update {
                    it.copy(
                        banners = bannersList,
                        categories = categoriesList,
                        popularItems = popularList,
                        items = allItemsList, // تخزين القائمة العامة في الـ State بنجاح
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

    // 🌟 دالة إدارة المفضلة الذكية (إضافة أو إزالة المشروب بناءً على وجوده مسبقاً)
    fun toggleFavorite(coffeeItem: CoffeeItemModel) {
        _uiState.update { currentState ->
            val currentFavorites = currentState.favoriteItems.toMutableList()

            // التحقق بالاسم لمعرفة إن كان المشروب مضافاً بالفعل أم لا
            val isAlreadyFavorite = currentFavorites.any { it.title == coffeeItem.title }

            if (isAlreadyFavorite) {
                currentFavorites.removeAll { it.title == coffeeItem.title } // إزالة إذا كان موجوداً
            } else {
                currentFavorites.add(coffeeItem) // إضافة إذا لم يكن موجوداً
            }

            // إعادة نسخ الحالة مع التحديث الجديد للمفضلة
            currentState.copy(favoriteItems = currentFavorites)
        }
    }
}