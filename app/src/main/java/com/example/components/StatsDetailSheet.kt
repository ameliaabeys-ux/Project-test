package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CoffeeProfileData
import com.example.ui.theme.CoffeeBg
import com.example.ui.theme.CoffeeCard
import com.example.ui.theme.CoffeeCardBorder
import com.example.ui.theme.CoffeeGold
import com.example.ui.theme.CoffeeTextMuted
import com.example.ui.theme.CoffeeTextPrimary

enum class StatType {
    DRINKS,
    SANDWICHES,
    CAFES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsDetailSheet(
    statType: StatType,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CoffeeBg,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 38.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(Color(0x35FFFFFF))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .testTag("stats_detail_sheet")
        ) {
            when (statType) {
                StatType.DRINKS -> DrinksBreakdown()
                StatType.SANDWICHES -> SandwichesBreakdown()
                StatType.CAFES -> CafesBreakdown()
            }
        }
    }
}

@Composable
private fun DrinksBreakdown() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "154 Drinks Consumed",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = CoffeeTextPrimary
            )
            Text(
                text = "Favorite beverage: Latte (73 orders)",
                fontSize = 14.sp,
                color = CoffeeTextMuted
            )
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0x18FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_coffee_drink),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(18.dp))

    Text(
        text = "RECENT ORDERS",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0x70EDE4D8),
        letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(CoffeeProfileData.recentBrews) { brew ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CoffeeCard)
                    .border(1.dp, CoffeeCardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = brew.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = CoffeeTextPrimary
                        )
                        Text(
                            text = "${brew.cafe} • ${brew.timeAgo}",
                            fontSize = 13.sp,
                            color = CoffeeTextMuted
                        )
                    }
                    Text(
                        text = brew.price,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = CoffeeGold
                    )
                }
            }
        }
    }
}

@Composable
private fun SandwichesBreakdown() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "36 Sandwiches Eaten",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = CoffeeTextPrimary
            )
            Text(
                text = "Artisan bakery bites & savory brunch",
                fontSize = 14.sp,
                color = CoffeeTextMuted
            )
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0x18FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sandwich),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(18.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(CoffeeProfileData.sandwiches) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CoffeeCard)
                    .border(1.dp, CoffeeCardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = item.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = CoffeeTextPrimary
                        )
                        Text(
                            text = "${item.bakery} • ${item.tags}",
                            fontSize = 13.sp,
                            color = CoffeeTextMuted
                        )
                    }
                    Text(
                        text = "${item.count} ordered",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = CoffeeGold
                    )
                }
            }
        }
    }
}

@Composable
private fun CafesBreakdown() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "12 Cafés Visited",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = CoffeeTextPrimary
            )
            Text(
                text = "Independent roasteries & specialty bars",
                fontSize = 14.sp,
                color = CoffeeTextMuted
            )
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0x18FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cafe_building),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(18.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(CoffeeProfileData.cafes) { cafe ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CoffeeCard)
                    .border(1.dp, CoffeeCardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = cafe.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = CoffeeTextPrimary
                        )
                        Text(
                            text = "${cafe.neighborhood} • Top: ${cafe.favoriteDrink}",
                            fontSize = 13.sp,
                            color = CoffeeTextMuted
                        )
                    }
                    Text(
                        text = "${cafe.visitCount} visits",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = CoffeeGold
                    )
                }
            }
        }
    }
}
