package com.example.coffeeapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coffeeapp.R
import com.example.coffeeapp.data.model.CoffeeItemModel
import com.example.coffeeapp.navigation.CoffeeScreen
import com.example.coffeeapp.ui.theme.CoffeeBrownMain
import com.example.coffeeapp.ui.theme.CoffeeCreamBG

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    val filteredItems = remember(uiState.items, uiState.selectedCategoryId) {
        uiState.items.filter { it.categoryId == uiState.selectedCategoryId.toString() }
    }

    Box(modifier = modifier.fillMaxSize().background(CoffeeCreamBG)) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = CoffeeBrownMain, modifier = Modifier.align(Alignment.Center))
        } else if (uiState.errorMessage.isNotEmpty()) {
            Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center).padding(16.dp))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                        Text(text = stringResource(R.string.welcome_message), fontSize = 16.sp, color = Color.Gray)
                        Text(text = stringResource(R.string.choose_coffee), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain)
                    }
                }

                if (uiState.banners.isNotEmpty()) {
                    item {
                        AsyncImage(
                            model = uiState.banners.first().url,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                item {
                    Text(text = stringResource(R.string.categories), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        items(uiState.categories) { category ->
                            val isSelected = category.id == uiState.selectedCategoryId
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) CoffeeBrownMain else Color.White)
                                    .clickable { homeViewModel.onCategorySelect(category.id) }
                                    .padding(horizontal = 20.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = category.title,
                                    color = if (isSelected) Color.White else CoffeeBrownMain,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                item { Text(text = stringResource(R.string.available_drinks), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain) }

                if (filteredItems.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                            Text(text = stringResource(R.string.no_drinks_in_category), color = Color.Gray)
                        }
                    }
                } else {
                    items(filteredItems) { coffeeItem ->
                        CoffeePopularCard(coffeeItem = coffeeItem, onClick = { navigateToDetail(navController, coffeeItem) })
                    }
                }

                item { Text(text = stringResource(R.string.popular_items), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain) }
                items(uiState.popularItems) { coffeeItem ->
                    CoffeePopularCard(coffeeItem = coffeeItem, onClick = { navigateToDetail(navController, coffeeItem) })
                }
            }
        }
    }
}

fun navigateToDetail(navController: NavController, coffeeItem: CoffeeItemModel) {
    val encodedUrl = java.net.URLEncoder.encode(coffeeItem.picUrl, "UTF-8")
    navController.navigate("${CoffeeScreen.Detail.name}/${coffeeItem.title}/${coffeeItem.description}/${coffeeItem.price.toFloat()}/${coffeeItem.rating.toFloat()}/${coffeeItem.extra}/$encodedUrl")
}

@Composable
fun CoffeePopularCard(coffeeItem: CoffeeItemModel, onClick: () -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = coffeeItem.picUrl, contentDescription = null, modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = coffeeItem.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain)
                Text(text = coffeeItem.extra, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 2.dp))
                Text(text = coffeeItem.description, fontSize = 12.sp, color = Color.DarkGray, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$${coffeeItem.price}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain)
                    Text(text = "⭐ ${String.format("%.1f", coffeeItem.rating)}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFFFA500))
                }
            }
        }
    }
}