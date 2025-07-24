package com.example.busbuddy.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.busbuddy.R
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.password
import kotlin.text.clear
import kotlin.text.isNotBlank


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold { padding ->
                    LoginBody(padding)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun LoginBody(paddingValues: androidx.compose.foundation.layout.PaddingValues) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? android.app.Activity // Keep this if you need the Activity instance for finish()

    var email by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var password by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var passwordVisible by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var rememberMe by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        email = sharedPreferences.getString("email", "") ?: ""
        password = sharedPreferences.getString("password", "") ?: ""
    }

    androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.White)
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        // ... (Image, Spacer, Email TextField, Password TextField, Remember me Row - all unchanged) ...

        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(40.dp))

        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.logo),
            contentDescription = "Bus Logo",
            modifier = androidx.compose.ui.Modifier.size(120.dp)
        )

        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(40.dp))

        androidx.compose.material3.OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            label = { androidx.compose.material3.Text("Email") },
            leadingIcon = { androidx.compose.material3.Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Email),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = androidx.compose.ui.graphics.Color.Green,
                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Gray,
                containerColor = androidx.compose.ui.graphics.Color(0xFFF0F0F0)
            )
        )

        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

        androidx.compose.material3.OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            label = { androidx.compose.material3.Text("Password") },
            leadingIcon = { androidx.compose.material3.Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.VisibilityOff
                else Icons.Filled.Visibility

                androidx.compose.material3.Icon(
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    modifier = androidx.compose.ui.Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = androidx.compose.ui.graphics.Color.Green,
                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Gray,
                containerColor = androidx.compose.ui.graphics.Color(0xFFF0F0F0)
            )
        )

        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

        androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            androidx.compose.foundation.layout.Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                androidx.compose.material3.Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = androidx.compose.material3.CheckboxDefaults.colors(
                        checkedColor = androidx.compose.ui.graphics.Color.Green,
                        checkmarkColor = androidx.compose.ui.graphics.Color.White
                    )
                )
                androidx.compose.material3.Text(text = "Remember me")
            }

            androidx.compose.material3.Text(
                text = "Forgot Password?",
                color = androidx.compose.ui.graphics.Color.Blue,
                modifier = androidx.compose.ui.Modifier.clickable {
                    Toast.makeText(context, "Forgot Password clicked", Toast.LENGTH_SHORT).show()


        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))

        androidx.compose.material3.Button(
            onClick = {
                if (rememberMe) {
                    editor.putString("email", email)
                    editor.putString("password", password)
                    editor.apply()
                } else {
                    editor.clear()
                    editor.apply()
                }

                if (email.isNotBlank() && password.isNotBlank()) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    // TODO: Replace with actual navigation to your main app screen after login
                    // Example: context.startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish() // Finishes LoginActivity
                } else {
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            androidx.compose.material3.Text(text = "Login")
        }

        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

        androidx.compose.material3.Text(
            text = "Don't have an account? Register",
            color = androidx.compose.ui.graphics.Color.Blue,
            modifier = androidx.compose.ui.Modifier
                .align(androidx.compose.ui.Alignment.End)
                .clickable {

                    Toast.makeText(context, "Register clicked", Toast.LENGTH_SHORT).show() // You can keep the Toast
                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)

        )
    }
}
