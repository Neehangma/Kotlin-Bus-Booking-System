package com.example.busbuddy.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBar(title: String, onNavigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextFieldColors(): TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
    // For focused state
    focusedTextColor = Color.White,
    focusedBorderColor = Color.Cyan,
    focusedLabelColor = Color.Cyan,
    cursorColor = Color.Cyan,

    // For unfocused state
    unfocusedTextColor = Color.White,
    unfocusedBorderColor = Color.Gray,
    unfocusedLabelColor = Color.LightGray,

    // For disabled state (e.g., readOnly fields)
    disabledTextColor = Color.LightGray,
    disabledBorderColor = Color.DarkGray,
    disabledLabelColor = Color.DarkGray,
    disabledLeadingIconColor = Color.Gray,
    disabledTrailingIconColor = Color.Gray,

    // General
    containerColor = Color.Black.copy(alpha = 0.3f)
)

