package com.example.busbuddy.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.busbuddy.R

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                DashboardContent()
            }
        }
    }
}

@Composable
fun DashboardContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.dashboard),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to BusBuddy!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // BOX GRID
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SquareDashboardButton(
                        text = "📄 View Bookings",
                        onClick = { context.startActivity(Intent(context, BookingListActivity::class.java)) },
                        modifier = Modifier
                            .weight(1f)
                    )
                    SquareDashboardButton(
                        text = "➕ Add Booking",
                        onClick = { context.startActivity(Intent(context, AddBookingActivity::class.java)) },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SquareDashboardButton(
                        text = "👤 Edit Profile",
                        onClick = { context.startActivity(Intent(context, EditProfileActivity::class.java)) },
                        modifier = Modifier
                            .weight(1f)
                    )
                    SquareDashboardButton(
                        text = "✏️ Edit Bookings",
                        onClick = { context.startActivity(Intent(context, EditBookingActivity::class.java)) },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SquareDashboardButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE9EEBD),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


class BookingListActivity : ComponentActivity()
class EditBookingActivity : ComponentActivity()
