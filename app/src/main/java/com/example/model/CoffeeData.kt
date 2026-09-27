package com.example.model

import androidx.annotation.DrawableRes
import com.example.R

data class FavoriteDrink(
    val id: String,
    val name: String,
    val countText: String,
    val description: String,
    val tastingNotes: String,
    val imageUrl: String,
    @DrawableRes val fallbackRes: Int
)

data class AchievementItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val tier: String,
    val isUnlocked: Boolean = true,
    val progress: Int = 100,
    val dateUnlocked: String = "Aug 2026"
)

data class CafeLocation(
    val name: String,
    val neighborhood: String,
    val visitCount: Int,
    val favoriteDrink: String
)

data class SandwichItem(
    val name: String,
    val bakery: String,
    val count: Int,
    val tags: String
)

data class RecentBrew(
    val title: String,
    val cafe: String,
    val timeAgo: String,
    val price: String
)

object CoffeeProfileData {
    val favoriteDrinks = listOf(
        FavoriteDrink(
            id = "latte",
            name = "Latte",
            countText = "Ordered 73 times",
            description = "Signature double-shot espresso poured into silky velvety microfoam.",
            tastingNotes = "Roasted hazelnut, dark cacao & vanilla sweet finish",
            imageUrl = "https://polo-pecan-73837341.figma.site/_assets/v11/976a811111808abc50be33c2483872dbdb6ad5a8.png",
            fallbackRes = R.drawable.ic_latte_art
        ),
        FavoriteDrink(
            id = "plum_parfait",
            name = "Plum Parfait Latte",
            countText = "Ordered 88 times",
            description = "House spiced plum compote with velvety steamed oat milk and espresso.",
            tastingNotes = "Spiced dark plum, brown sugar, cardamom cream",
            imageUrl = "https://polo-pecan-73837341.figma.site/_assets/v11/976a811111808abc50be33c2483872dbdb6ad5a8.png",
            fallbackRes = R.drawable.ic_latte_art
        ),
        FavoriteDrink(
            id = "cortado",
            name = "Artisan Cortado",
            countText = "Ordered 42 times",
            description = "1:1 ratio of rich extracted ristretto cut with warm textured milk.",
            tastingNotes = "Intense toffee, walnut, smooth crema balance",
            imageUrl = "https://polo-pecan-73837341.figma.site/_assets/v11/a8ba62db54d1e331b7beb36d69308e9b92516b99.png",
            fallbackRes = R.drawable.ic_coffee_drink
        ),
        FavoriteDrink(
            id = "cold_brew",
            name = "Reserve Cold Brew",
            countText = "Ordered 31 times",
            description = "20-hour slow steeped single-origin Ethiopian beans served on ice.",
            tastingNotes = "Blackberry, bergamot, dark chocolate crispness",
            imageUrl = "https://polo-pecan-73837341.figma.site/_assets/v11/a8ba62db54d1e331b7beb36d69308e9b92516b99.png",
            fallbackRes = R.drawable.ic_coffee_drink
        )
    )

    val achievements = listOf(
        AchievementItem("1", "First Sip", "Checked into first artisan coffee shop", "Bronze", true, 100, "Jan 2026"),
        AchievementItem("2", "Espresso Explorer", "Sampled 5 single origin roast origins", "Silver", true, 100, "Feb 2026"),
        AchievementItem("3", "Morning Regular", "10 check-ins before 8:30 AM", "Silver", true, 100, "Mar 2026"),
        AchievementItem("4", "Sandwich Connoisseur", "Enjoyed 25 artisan cafe sandwiches & pastries", "Gold", true, 100, "Apr 2026"),
        AchievementItem("5", "Café Nomad", "Visited 10 independent coffee spots", "Gold", true, 100, "May 2026"),
        AchievementItem("6", "Microfoam Master", "Ordered 50 handcrafted lattes", "Gold", true, 100, "Jun 2026"),
        AchievementItem("7", "Centurion Roaster", "Crossed the 100 coffee milestone", "Platinum", true, 100, "Jul 2026"),
        AchievementItem("8", "Pour-Over Scholar", "Tasted 12 V60 hand-poured single origins", "Gold", true, 100, "Aug 2026"),
        AchievementItem("9", "Plum Parfait Devotee", "Ordered the signature brew 15 times", "Rare", true, 100, "Aug 2026"),
        AchievementItem("10", "Weekend Roamer", "Explored cafes across 4 neighborhoods", "Silver", true, 100, "Sep 2026"),
        AchievementItem("11", "Roast Hunter", "Logged tasting notes on 20 distinctive beans", "Gold", true, 100, "Sep 2026"),
        AchievementItem("12", "Grand Connoisseur", "154 total drinks consumed milestone", "Master", true, 100, "Sep 2026")
    )

    val cafes = listOf(
        CafeLocation("Monmouth Coffee", "Covent Garden", 48, "Plum Parfait Latte"),
        CafeLocation("% Arabica", "Piccadilly", 36, "Spanish Latte"),
        CafeLocation("WatchHouse", "Bermondsey", 28, "Single Origin Cortado"),
        CafeLocation("Prufrock Coffee", "Farringdon", 19, "Filter V60 Brew"),
        CafeLocation("Colonna & Small's", "Heritage Quarter", 12, "Ethiopian Guji Cup"),
        CafeLocation("Workshop Coffee", "Marylebone", 11, "Flat White")
    )

    val sandwiches = listOf(
        SandwichItem("Prosciutto & Gruyère Croissant", "L'Ami Bakery", 14, "Flaky • Savory"),
        SandwichItem("Smoked Salmon Sourdough", "Monmouth Kitchen", 9, "Dill • Cream Cheese"),
        SandwichItem("Truffle Mushroom Focaccia", "WatchHouse Roasters", 7, "Warm Melted"),
        SandwichItem("Fig & Brie Brioche Roll", "Prufrock Cafe", 6, "Sweet & Savory")
    )

    val recentBrews = listOf(
        RecentBrew("Plum Parfait Latte", "Monmouth Coffee", "2 hours ago", "£4.80"),
        RecentBrew("Flat White (Oat)", "WatchHouse", "Yesterday, 9:15 AM", "£4.20"),
        RecentBrew("Prosciutto Croissant", "L'Ami Bakery", "Yesterday, 9:20 AM", "£5.50"),
        RecentBrew("Ethiopian Natural Filter", "Prufrock Coffee", "2 days ago", "£4.50")
    )
}
