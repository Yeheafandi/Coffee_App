package com.example.coffeeapp.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coffeeapp.R
import com.example.coffeeapp.data.model.CoffeeItemModel
import com.example.coffeeapp.ui.screens.home.HomeViewModel
import com.example.coffeeapp.ui.theme.CoffeeBrownMain
import com.example.coffeeapp.ui.theme.CoffeeCreamBG
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    title: String,
    description: String,
    price: Double,
    rating: Double,
    extra: String,
    picUrl: String,
    homeViewModel: HomeViewModel
) {
    val uiState by homeViewModel.uiState.collectAsState()

    val currentCoffeeItem = remember {
        CoffeeItemModel(
            title = title,
            description = description,
            price = price,
            rating = rating,
            extra = extra,
            picUrl = picUrl,
            categoryId = ""
        )
    }

    val isFavorite = uiState.favoriteItems.any { it.title == title }
    var selectedSizeIndex by remember { mutableStateOf(1) } // الوسط افتراضياً

    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val dynamicPrice = remember(selectedSizeIndex) {
        when (selectedSizeIndex) {
            0 -> price
            1 -> price + 0.5
            else -> price + 1.0
        }
    }

    val sizeOptions = listOf(
        stringResource(R.string.size_small),
        stringResource(R.string.size_medium),
        stringResource(R.string.size_large)
    )

    val currentSizeLabel = sizeOptions[selectedSizeIndex]

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.detail_screen_title), fontWeight = FontWeight.Bold, color = CoffeeBrownMain) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CoffeeBrownMain)
                    }
                },
                actions = {
                    IconButton(onClick = { homeViewModel.toggleFavorite(currentCoffeeItem) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else CoffeeBrownMain
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoffeeCreamBG)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                tonalElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "السعر", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            text = String.format("$%.2f", dynamicPrice),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoffeeBrownMain
                        )
                    }

                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrownMain),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(56.dp)
                            .width(180.dp)
                    ) {
                        Text(text = stringResource(R.string.buy_now), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        },
        containerColor = CoffeeCreamBG
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. صورة المشروب
            AsyncImage(
                model = picUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )

            // تفاصيل البيانات
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // 🌟 تعديل الاسم والتقييم لمنع الالتفاف العشوائي وتنسيق الأرقام
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                    ) {
                        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain)
                        Text(text = extra, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(text = "⭐", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", rating), // 🌟 تقريب الكسر لخانة واحدة عشرية فقط
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoffeeBrownMain
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                // 2. الوصف
                Text(text = stringResource(R.string.description_title), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain)
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 8.dp),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 3. الحجم
                Text(text = stringResource(R.string.size_title), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    sizeOptions.forEachIndexed { index, size ->
                        val isSelected = index == selectedSizeIndex
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) CoffeeBrownMain.copy(alpha = 0.15f) else Color.White)
                                .border(
                                    width = 1.5.dp,
                                    color = if (isSelected) CoffeeBrownMain else Color.LightGray.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedSizeIndex = index }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = size,
                                color = if (isSelected) CoffeeBrownMain else Color.Gray,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // 4. منطق النافذة الحوارية (AlertDialog) لملخص الطلب
    // 4. منطق النافذة الحوارية (AlertDialog) لملخص الطلب
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.confirm_order_title), // استبدال النص الثابت
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrownMain,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.confirm_order_message), fontSize = 14.sp, color = Color.DarkGray) // استبدال النص الثابت
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))

                    Text(text = "${stringResource(R.string.coffee_label)}: $title", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text(text = "${stringResource(R.string.size_selected)}: $currentSizeLabel", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text(
                        text = "${stringResource(R.string.total_label)}: " + String.format("$%.2f", dynamicPrice),
                        fontWeight = FontWeight.Bold,
                        color = CoffeeBrownMain,
                        fontSize = 15.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "تم طلب $title ($currentSizeLabel) بنجاح! 🎉",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrownMain)
                ) {
                    Text(text = stringResource(R.string.confirm_button), color = Color.White, fontWeight = FontWeight.Bold) // استبدال النص الثابت
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(R.string.cancel_button), color = Color.Gray) // استبدال النص الثابت
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}