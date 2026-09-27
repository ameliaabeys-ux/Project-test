package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CoffeeBg
import com.example.ui.theme.CoffeeGold
import com.example.ui.theme.CoffeeTextMuted
import com.example.ui.theme.CoffeeTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileSheet(
    currentName: String,
    currentDrink: String,
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var name by remember { mutableStateOf(currentName) }
    var favoriteDrink by remember { mutableStateOf(currentDrink) }

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
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .testTag("edit_profile_sheet")
        ) {
            Text(
                text = "Edit Coffee Profile",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = CoffeeTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Update your profile name and signature beverage",
                fontSize = 14.sp,
                color = CoffeeTextMuted
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Profile Name") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CoffeeTextPrimary,
                    unfocusedTextColor = CoffeeTextPrimary,
                    focusedBorderColor = CoffeeGold,
                    unfocusedBorderColor = Color(0x35FFFFFF),
                    focusedLabelColor = CoffeeGold,
                    unfocusedLabelColor = CoffeeTextMuted
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("name_input_field")
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = favoriteDrink,
                onValueChange = { favoriteDrink = it },
                label = { Text("Signature Drink") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CoffeeTextPrimary,
                    unfocusedTextColor = CoffeeTextPrimary,
                    focusedBorderColor = CoffeeGold,
                    unfocusedBorderColor = Color(0x35FFFFFF),
                    focusedLabelColor = CoffeeGold,
                    unfocusedLabelColor = CoffeeTextMuted
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("signature_drink_input_field")
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onSave(name.trim().ifEmpty { currentName }, favoriteDrink.trim().ifEmpty { currentDrink })
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoffeeGold,
                    contentColor = Color(0xFF180A06)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_profile_button")
            ) {
                Text(
                    text = "Save Profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
