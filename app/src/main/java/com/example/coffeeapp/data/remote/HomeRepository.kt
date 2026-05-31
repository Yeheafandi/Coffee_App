package com.example.coffeeapp.data.remote

import com.example.coffeeapp.data.model.BannerModel
import com.example.coffeeapp.data.model.CategoryModel
import com.example.coffeeapp.data.model.CoffeeItemModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class HomeRepository {
    private val database = FirebaseDatabase.getInstance().reference

    // 1. جلب روابط البانر الإعلاني بأمان
    suspend fun getBanners(): List<BannerModel> {
        return try {
            val snapshot = database.child("Banner").get().await()
            snapshot.children.mapNotNull { it.getValue(BannerModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 2. جلب فئات القهوة بأمان
    suspend fun getCategories(): List<CategoryModel> {
        return try {
            val snapshot = database.child("Category").get().await()
            snapshot.children.mapNotNull { it.getValue(CategoryModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 3. جلب القائمة الأكثر شعبية مع معالجة ذكية ومخصصة لمصفوفة الروابط (Manual Parsing)
    suspend fun getPopularItems(): List<CoffeeItemModel> {
        return try {
            val snapshot = database.child("Popular").get().await()
            snapshot.children.mapNotNull { childSnapshot ->
                val title = childSnapshot.child("title").getValue(String::class.java) ?: ""
                val description = childSnapshot.child("description").getValue(String::class.java) ?: ""
                val extra = childSnapshot.child("extra").getValue(String::class.java) ?: ""
                val price = childSnapshot.child("price").getValue(Double::class.java) ?: 0.0
                val rating = childSnapshot.child("rating").getValue(Double::class.java) ?: 0.0
                val categoryId = childSnapshot.child("categoryId").getValue(String::class.java) ?: ""

                // قراءة الرابط الأول من مصفوفة picUrl بأمان كنص String
                val picUrlSnapshot = childSnapshot.child("picUrl")
                val imageUrl = if (picUrlSnapshot.hasChildren()) {
                    picUrlSnapshot.child("0").getValue(String::class.java) ?: ""
                } else {
                    picUrlSnapshot.getValue(String::class.java) ?: ""
                }

                CoffeeItemModel(
                    title = title,
                    description = description,
                    extra = extra,
                    price = price,
                    rating = rating,
                    picUrl = imageUrl, // ممرر كـ String متوافق مع الـ Model تماماً
                    categoryId = categoryId
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 4. جلب القائمة الكاملة للمشروبات للتصفية (عقدة Items) مصلحة بالكامل لتوافق الـ String
    suspend fun getItems(): List<CoffeeItemModel> {
        return try {
            val snapshot = database.child("Items").get().await()
            snapshot.children.mapNotNull { childSnapshot ->
                val title = childSnapshot.child("title").getValue(String::class.java) ?: ""
                val description = childSnapshot.child("description").getValue(String::class.java) ?: ""
                val extra = childSnapshot.child("extra").getValue(String::class.java) ?: ""
                val price = childSnapshot.child("price").getValue(Double::class.java) ?: 0.0
                val rating = childSnapshot.child("rating").getValue(Double::class.java) ?: 0.0
                val categoryId = childSnapshot.child("categoryId").getValue(String::class.java) ?: ""

                // 🌟 الحل الجذري: قراءة الرابط النصي الأول مباشرة ليطابق نوع الـ String في الـ Model
                val picUrlSnapshot = childSnapshot.child("picUrl")
                val imageUrl = if (picUrlSnapshot.hasChildren()) {
                    picUrlSnapshot.child("0").getValue(String::class.java) ?: ""
                } else {
                    picUrlSnapshot.getValue(String::class.java) ?: ""
                }

                CoffeeItemModel(
                    title = title,
                    description = description,
                    extra = extra,
                    price = price,
                    rating = rating,
                    picUrl = imageUrl, // 🌟 يمرر الآن كـ String بدون أي مشاكل أو خطأ أحمر!
                    categoryId = categoryId
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}