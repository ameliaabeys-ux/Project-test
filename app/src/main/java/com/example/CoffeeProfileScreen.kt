package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.components.AchievementsSheet
import com.example.components.EditProfileSheet
import com.example.components.GlassCard
import com.example.components.GlassCircleButton
import com.example.components.HeroVideoPlayer
import com.example.components.StatType
import com.example.components.StatsDetailSheet
import com.example.model.CoffeeProfileData
import com.example.ui.theme.CoffeeBg
import com.example.ui.theme.CoffeeCard
import com.example.ui.theme.CoffeeCardBorder
import com.example.ui.theme.CoffeeGold
import com.example.ui.theme.CoffeeOuterBg
import com.example.ui.theme.CoffeeTextLabel
import com.example.ui.theme.CoffeeTextMuted
import com.example.ui.theme.CoffeeTextPrimary
import com.example.ui.theme.GlassBg
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun CoffeeProfileScreen() {
    var isMockupMode by remember { mutableStateOf(false) }

    // State for interactive modals and editing
    var profileName by remember { mutableStateOf("Dasha") }
    var signatureDrink by remember { mutableStateOf("Plum Parfait Latte") }
    var currentFavoriteIndex by remember { mutableIntStateOf(0) }
    var showAchievementsSheet by remember { mutableStateOf(false) }
    var selectedStatType by remember { mutableStateOf<StatType?>(null) }
    var showEditSheet by remember { mutableStateOf(false) }
    var isTeaserExpanded by remember { mutableStateOf(false) }

    val favoriteDrink = CoffeeProfileData.favoriteDrinks[currentFavoriteIndex % CoffeeProfileData.favoriteDrinks.size]

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isMockupMode) CoffeeOuterBg else CoffeeBg)
            .testTag("coffee_profile_screen")
    ) {
        val screenWidth = maxWidth

        if (isMockupMode) {
            // Render inside the 390x844px Phone Mockup Frame on top of warm radial gradients
            PhoneMockupContainer(
                onToggleMockup = { isMockupMode = !isMockupMode }
            ) {
                ProfileContent(
                    profileName = profileName,
                    signatureDrink = signatureDrink,
                    favoriteDrink = favoriteDrink,
                    isMockupMode = true,
                    onToggleMockup = { isMockupMode = !isMockupMode },
                    onEditClick = { showEditSheet = true },
                    onAchievementsClick = { showAchievementsSheet = true },
                    onStatClick = { stat -> selectedStatType = stat },
                    onShuffleFavorite = {
                        currentFavoriteIndex = (currentFavoriteIndex + 1) % CoffeeProfileData.favoriteDrinks.size
                    },
                    isTeaserExpanded = isTeaserExpanded,
                    onToggleTeaser = { isTeaserExpanded = !isTeaserExpanded }
                )
            }
        } else {
            // Native Fullscreen Device Layout
            ProfileContent(
                profileName = profileName,
                signatureDrink = signatureDrink,
                favoriteDrink = favoriteDrink,
                isMockupMode = false,
                onToggleMockup = { isMockupMode = !isMockupMode },
                onEditClick = { showEditSheet = true },
                onAchievementsClick = { showAchievementsSheet = true },
                onStatClick = { stat -> selectedStatType = stat },
                onShuffleFavorite = {
                    currentFavoriteIndex = (currentFavoriteIndex + 1) % CoffeeProfileData.favoriteDrinks.size
                },
                isTeaserExpanded = isTeaserExpanded,
                onToggleTeaser = { isTeaserExpanded = !isTeaserExpanded }
            )
        }

        // Modals
        if (showAchievementsSheet) {
            AchievementsSheet(onDismiss = { showAchievementsSheet = false })
        }

        selectedStatType?.let { stat ->
            StatsDetailSheet(
                statType = stat,
                onDismiss = { selectedStatType = null }
            )
        }

        if (showEditSheet) {
            EditProfileSheet(
                currentName = profileName,
                currentDrink = signatureDrink,
                onSave = { newName, newDrink ->
                    profileName = newName
                    signatureDrink = newDrink
                },
                onDismiss = { showEditSheet = false }
            )
        }
    }
}

/**
 * Centered Phone Mockup Frame (390x844px at zoom scale, 44px border-radius, warm radial backdrop)
 */
@Composable
private fun PhoneMockupContainer(
    onToggleMockup: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // Background radial gradients over #070402 as specified:
                // 1) radial-gradient(ellipse 65% 55% at 15% 52%, rgba(168, 78, 10, 0.22))
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(168, 78, 10, 56), Color.Transparent),
                        center = Offset(size.width * 0.15f, size.height * 0.52f),
                        radius = size.width * 0.65f
                    )
                )
                // 2) radial-gradient(ellipse 52% 48% at 83% 26%, rgba(122, 52, 8, 0.17))
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(122, 52, 8, 43), Color.Transparent),
                        center = Offset(size.width * 0.83f, size.height * 0.26f),
                        radius = size.width * 0.52f
                    )
                )
                // 3) radial-gradient(ellipse 44% 52% at 56% 92%, rgba(98, 36, 5, 0.14))
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(98, 36, 5, 36), Color.Transparent),
                        center = Offset(size.width * 0.56f, size.height * 0.92f),
                        radius = size.width * 0.44f
                    )
                )
                // 4) radial-gradient(ellipse 30% 30% at 72% 75%, rgba(60, 20, 5, 0.10))
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(60, 20, 5, 26), Color.Transparent),
                        center = Offset(size.width * 0.72f, size.height * 0.75f),
                        radius = size.width * 0.30f
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Mockup frame (390x844dp scaled at 0.78, black bezel, 44dp radius, strong shadow)
        Box(
            modifier = Modifier
                .scale(0.85f)
                .size(width = 390.dp, height = 844.dp)
                .shadow(
                    elevation = 28.dp,
                    shape = RoundedCornerShape(44.dp),
                    ambientColor = Color(0x60000000),
                    spotColor = Color(0xB0000000)
                )
                .clip(RoundedCornerShape(44.dp))
                .background(Color.Black)
                .border(2.dp, Color(0x33444444), RoundedCornerShape(44.dp))
        ) {
            // Speaker / Island capsule at top
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp)
                    .size(width = 110.dp, height = 24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F0F0F))
            )

            // Inner .screen div (390x844dp, #180a06, hidden scrollbar)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CoffeeBg)
            ) {
                content()
            }
        }
    }
}

/**
 * Main Profile Content
 */
@Composable
private fun ProfileContent(
    profileName: String,
    signatureDrink: String,
    favoriteDrink: com.example.model.FavoriteDrink,
    isMockupMode: Boolean,
    onToggleMockup: () -> Unit,
    onEditClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onStatClick: (StatType) -> Unit,
    onShuffleFavorite: () -> Unit,
    isTeaserExpanded: Boolean,
    onToggleTeaser: () -> Unit
) {
    val scrollState = rememberScrollState()
    val statusBarPadding = if (isMockupMode) 24.dp else WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    // Smooth cubic-bezier curve from prompt: cubic-bezier(0.16, 1, 0.3, 1)
    val easeSpec = CubicBezierEasing(0.16f, 1.0f, 0.3f, 1.0f)

    // Entry animation values
    val heroAlpha = remember { Animatable(0f) }
    val heroScale = remember { Animatable(1.02f) }

    val topBtnOffset = remember { Animatable(-10f) }
    val topBtnAlpha = remember { Animatable(0f) }

    val laurelsAlpha = remember { Animatable(0f) }

    val nameOffset = remember { Animatable(16f) }
    val nameAlpha = remember { Animatable(0f) }

    val subtitleOffset = remember { Animatable(16f) }
    val subtitleAlpha = remember { Animatable(0f) }

    val pillOffset = remember { Animatable(16f) }
    val pillAlpha = remember { Animatable(0f) }

    val stat1Offset = remember { Animatable(16f) }
    val stat1Alpha = remember { Animatable(0f) }
    val stat2Offset = remember { Animatable(16f) }
    val stat2Alpha = remember { Animatable(0f) }
    val stat3Offset = remember { Animatable(16f) }
    val stat3Alpha = remember { Animatable(0f) }

    val favoriteOffset = remember { Animatable(16f) }
    val favoriteAlpha = remember { Animatable(0f) }

    val teaserAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Hero reveal: opacity 0 -> 1, scale 1.01 -> 1, 1.9s, cubic-bezier(0.16, 1, 0.3, 1)
        launch {
            heroAlpha.animateTo(1f, animationSpec = tween(1900, easing = easeSpec))
        }
        launch {
            heroScale.animateTo(1f, animationSpec = tween(1900, easing = easeSpec))
        }

        // Top bar dropIn from translateY(-10dp), 0.7s, staggered 0.35s / 0.42s
        launch {
            delay(350)
            topBtnOffset.animateTo(0f, animationSpec = tween(700, easing = easeSpec))
        }
        launch {
            delay(350)
            topBtnAlpha.animateTo(1f, animationSpec = tween(700, easing = easeSpec))
        }

        // Laurels fadeIn 0.8s, delay 0.5s
        launch {
            delay(500)
            laurelsAlpha.animateTo(0.6f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        }

        // Name fadeRise from translateY(16px), 0.7s, delay 0.5s
        launch {
            delay(500)
            nameOffset.animateTo(0f, animationSpec = tween(700, easing = easeSpec))
        }
        launch {
            delay(500)
            nameAlpha.animateTo(1f, animationSpec = tween(700, easing = easeSpec))
        }

        // Subtitle delay 0.58s
        launch {
            delay(580)
            subtitleOffset.animateTo(0f, animationSpec = tween(700, easing = easeSpec))
        }
        launch {
            delay(580)
            subtitleAlpha.animateTo(1f, animationSpec = tween(700, easing = easeSpec))
        }

        // Achievements pill delay 0.66s
        launch {
            delay(660)
            pillOffset.animateTo(0f, animationSpec = tween(700, easing = easeSpec))
        }
        launch {
            delay(660)
            pillAlpha.animateTo(1f, animationSpec = tween(700, easing = easeSpec))
        }

        // Stat cards staggered at 0.74s, 0.80s, 0.86s
        launch {
            delay(740)
            stat1Offset.animateTo(0f, animationSpec = tween(700, easing = easeSpec))
        }
        launch {
            delay(740)
            stat1Alpha.animateTo(1f, animationSpec = tween(700, easing = easeSpec))
        }

        launch {
            delay(800)
            stat2Offset.animateTo(0f, animationSpec = tween(700, easing = easeSpec))
        }
        launch {
            delay(800)
            stat2Alpha.animateTo(1f, animationSpec = tween(700, easing = easeSpec))
        }

        launch {
            delay(860)
            stat3Offset.animateTo(0f, animationSpec = tween(700, easing = easeSpec))
        }
        launch {
            delay(860)
            stat3Alpha.animateTo(1f, animationSpec = tween(700, easing = easeSpec))
        }

        // Favorite card delay 0.94s
        launch {
            delay(940)
            favoriteOffset.animateTo(0f, animationSpec = tween(700, easing = easeSpec))
        }
        launch {
            delay(940)
            favoriteAlpha.animateTo(1f, animationSpec = tween(700, easing = easeSpec))
        }

        // Next card fadeIn delay 1.02s
        launch {
            delay(1020)
            teaserAlpha.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CoffeeBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HERO SECTION (430px tall, video looping muted autoplay, bottom gradient fade)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
                    .alpha(heroAlpha.value)
                    .scale(heroScale.value)
            ) {
                HeroVideoPlayer()
            }

            // IDENTITY SECTION (overlaps hero by -112px margin-top)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-112).dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Flanking Laurels around "Dasha"
                    // Left and right laurel images 73px tall, 0.6 opacity, flanking the name
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        // Left Laurel: positioned to the left of the name
                        Image(
                            painter = painterResource(id = com.example.R.drawable.ic_laurel_left),
                            contentDescription = "Laurel left",
                            modifier = Modifier
                                .offset(x = (-92).dp)
                                .height(73.dp)
                                .width(36.dp)
                                .alpha(laurelsAlpha.value)
                        )

                        // Centered name "Dasha" (28px, weight 500)
                        Text(
                            text = profileName,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Medium,
                            color = CoffeeTextPrimary,
                            letterSpacing = 0.5.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .offset { IntOffset(0, nameOffset.value.dp.roundToPx()) }
                                .alpha(nameAlpha.value)
                                .testTag("profile_name")
                        )

                        // Right Laurel: positioned to the right of the name
                        Image(
                            painter = painterResource(id = com.example.R.drawable.ic_laurel_right),
                            contentDescription = "Laurel right",
                            modifier = Modifier
                                .offset(x = 92.dp)
                                .height(73.dp)
                                .width(36.dp)
                                .alpha(laurelsAlpha.value)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Subtitle "Plum Parfait Latte" (15px, muted color rgba(235, 220, 205, 0.55))
                    Text(
                        text = signatureDrink,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = CoffeeTextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .offset { IntOffset(0, subtitleOffset.value.dp.roundToPx()) }
                            .alpha(subtitleAlpha.value)
                            .testTag("profile_subtitle")
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // ACHIEVEMENTS PILL (centered, 30px below identity, 54px tall, 225px wide, 27px radius)
                    AchievementsPillButton(
                        modifier = Modifier
                            .offset { IntOffset(0, pillOffset.value.dp.roundToPx()) }
                            .alpha(pillAlpha.value),
                        onClick = onAchievementsClick
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    // STATS GRID (3 columns, 12px gap, 16px horizontal padding)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 500.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card 1: drinks consumed (154)
                        StatCard(
                            imageRes = com.example.R.drawable.ic_coffee_drink,
                            imageUrl = "https://polo-pecan-73837341.figma.site/_assets/v11/a8ba62db54d1e331b7beb36d69308e9b92516b99.png",
                            number = "154",
                            label = "drinks consumed",
                            modifier = Modifier
                                .weight(1f)
                                .offset { IntOffset(0, stat1Offset.value.dp.roundToPx()) }
                                .alpha(stat1Alpha.value),
                            testTag = "stat_card_drinks",
                            onClick = { onStatClick(StatType.DRINKS) }
                        )

                        // Card 2: sandwiches eaten (36)
                        StatCard(
                            imageRes = com.example.R.drawable.ic_sandwich,
                            imageUrl = "https://polo-pecan-73837341.figma.site/_assets/v11/953600065119f54f64ab9edb076b3cbb289fcff8.png",
                            number = "36",
                            label = "sandwiches eaten",
                            modifier = Modifier
                                .weight(1f)
                                .offset { IntOffset(0, stat2Offset.value.dp.roundToPx()) }
                                .alpha(stat2Alpha.value),
                            testTag = "stat_card_sandwiches",
                            onClick = { onStatClick(StatType.SANDWICHES) }
                        )

                        // Card 3: cafes visited (12)
                        StatCard(
                            imageRes = com.example.R.drawable.ic_cafe_building,
                            imageUrl = "https://polo-pecan-73837341.figma.site/_assets/v11/aef68e05f729a30ed177f74c2cece578c05bfdba.png",
                            number = "12",
                            label = "cafes visited",
                            modifier = Modifier
                                .weight(1f)
                                .offset { IntOffset(0, stat3Offset.value.dp.roundToPx()) }
                                .alpha(stat3Alpha.value),
                            testTag = "stat_card_cafes",
                            onClick = { onStatClick(StatType.CAFES) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // FAVORITE CARD (12px below stats, 16px horizontal margin, 110px height, 24px radius, flex row)
                    FavoriteDrinkCard(
                        drink = favoriteDrink,
                        onShuffle = onShuffleFavorite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 500.dp)
                            .offset { IntOffset(0, favoriteOffset.value.dp.roundToPx()) }
                            .alpha(favoriteAlpha.value)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // PARTIAL NEXT CARD (34px tall with flat bottom corners, teaser for scroll)
                    PartialTeaserCard(
                        isExpanded = isTeaserExpanded,
                        onToggle = onToggleTeaser,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 500.dp)
                            .alpha(teaserAlpha.value)
                    )
                }
            }
        }

        // TOP BAR WITH TWO GLASS CIRCLE BUTTONS (absolute top 18px + status bar)
        // Edit icon on left, X close SVG on right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = statusBarPadding + 18.dp)
                .offset { IntOffset(0, topBtnOffset.value.dp.roundToPx()) }
                .alpha(topBtnAlpha.value),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Edit Glass Circle Button (58x44px, 22px radius)
            GlassCircleButton(
                onClick = onEditClick,
                testTag = "top_bar_edit_button"
            ) {
                Icon(
                    painter = painterResource(id = com.example.R.drawable.ic_edit),
                    contentDescription = "Edit profile",
                    tint = CoffeeTextPrimary,
                    modifier = Modifier.size(19.dp)
                )
            }

            // Center: Frame/Native View Mode Switcher
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(GlassBg)
                    .border(1.dp, Color(0x22FFFFFF), CircleShape)
                    .clickable { onToggleMockup() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("toggle_mockup_view_button")
            ) {
                Text(
                    text = if (isMockupMode) "📱 Phone Frame" else "✨ Native View",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = CoffeeGold
                )
            }

            // Right Close (X) Glass Circle Button (58x44px, 22px radius)
            GlassCircleButton(
                onClick = onToggleMockup,
                testTag = "top_bar_close_button"
            ) {
                Icon(
                    painter = painterResource(id = com.example.R.drawable.ic_close),
                    contentDescription = "Close / Toggle",
                    tint = CoffeeTextPrimary,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}

/**
 * Achievements Pill: 54px tall, 225px wide, 27px radius
 * Trophy icon (18px) + text "12 achievements" (18px, weight 500)
 */
@Composable
private fun AchievementsPillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = androidx.compose.animation.core.spring(dampingRatio = 0.65f, stiffness = 450f),
        label = "pill_scale"
    )

    val shape = RoundedCornerShape(27.dp)

    Box(
        modifier = modifier
            .testTag("achievements_pill_button")
            .size(width = 225.dp, height = 54.dp)
            .scale(scale)
            .clip(shape)
            .background(GlassBg)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0x35FFFFFF),
                        Color(0x10FFFFFF),
                        Color(0x05FFFFFF)
                    )
                ),
                shape = shape
            )
            .drawBehind {
                // Subtle liquid light rim highlight
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, Color(0x38FFFFFF), Color.Transparent)
                    ),
                    start = Offset(24f, 1f),
                    end = Offset(size.width - 24f, 1f),
                    strokeWidth = 1.5f
                )
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = com.example.R.drawable.ic_trophy),
                contentDescription = "Trophy",
                tint = CoffeeGold,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "12 achievements",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = CoffeeTextPrimary,
                letterSpacing = 0.2.sp
            )
        }
    }
}

/**
 * Stat Card: semi-transparent rgba(255,255,255,0.06), 24px border-radius, 84x84 image, number 25px, label 13px
 */
@Composable
private fun StatCard(
    imageRes: Int,
    imageUrl: String,
    number: String,
    label: String,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    GlassCard(
        modifier = modifier.testTag(testTag),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = CoffeeCard,
        borderColor = CoffeeCardBorder,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image (84x84) with remote Coil loading and local fallback
            Box(
                modifier = Modifier.size(84.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = imageRes),
                    error = painterResource(id = imageRes),
                    contentDescription = label,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(84.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Number: 25px, weight 500
            Text(
                text = number,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                color = CoffeeTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Label: 13px, color #BAAA9A8C
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = CoffeeTextLabel,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * Favorite card: 110px height, 24px radius, flex row with 16px gap
 * Latte image 108x108 contain
 * Text column: "Favorite" (13px), "Latte" (19px), "Ordered 73 times" (13px)
 * Shuffle button on right (glass circle, icon 19px)
 */
@Composable
private fun FavoriteDrinkCard(
    drink: com.example.model.FavoriteDrink,
    onShuffle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    GlassCard(
        modifier = modifier
            .height(110.dp)
            .testTag("favorite_drink_card"),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = CoffeeCard,
        borderColor = CoffeeCardBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Latte image 108x108, object-fit contain
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .offset(x = (-4).dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(drink.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = drink.fallbackRes),
                    error = painterResource(id = drink.fallbackRes),
                    contentDescription = drink.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(108.dp)
                )
            }

            // Text Column: Favorite (13px), Title (19px), Subtitle (13px)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Favorite",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = CoffeeTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = drink.name,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = CoffeeTextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = drink.countText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = CoffeeTextLabel
                )
            }

            // Shuffle Button on right (glass circle, local icon 19px)
            GlassCircleButton(
                onClick = onShuffle,
                modifier = Modifier.padding(end = 6.dp),
                testTag = "shuffle_favorite_button"
            ) {
                Icon(
                    painter = painterResource(id = com.example.R.drawable.ic_shuffle),
                    contentDescription = "Shuffle favorite",
                    tint = CoffeeTextPrimary,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}

/**
 * Partial next card: Same card style but only 34px tall with flat bottom corners (teaser for scroll)
 * When clicked, gracefully expands to reveal recent brews and orders!
 */
@Composable
private fun PartialTeaserCard(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val teaserShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp)

    Column(
        modifier = modifier
            .clip(teaserShape)
            .background(CoffeeCard)
            .border(1.dp, CoffeeCardBorder, teaserShape)
            .clickable { onToggle() }
            .testTag("partial_teaser_card")
    ) {
        // Teaser top handle row (34px tall)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(Color(0x30FFFFFF))
                )
            }
        }

        // Expanded Recent Brews content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Recent Brews Timeline",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = CoffeeTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                CoffeeProfileData.recentBrews.forEach { brew ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = brew.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = CoffeeTextPrimary
                            )
                            Text(
                                text = "${brew.cafe} • ${brew.timeAgo}",
                                fontSize = 12.sp,
                                color = CoffeeTextMuted
                            )
                        }
                        Text(
                            text = brew.price,
                            fontSize = 14.sp,
                            color = CoffeeGold
                        )
                    }
                }
            }
        }
    }
}
