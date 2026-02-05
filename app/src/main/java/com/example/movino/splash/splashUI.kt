package com.example.movino.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movino.R
import com.example.movino.ui.theme.OnPrimaryLight
import com.example.movino.ui.theme.PrimaryLight

@Composable
fun SplashUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryLight),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(R.drawable.ic_movino_icon),
                tint = OnPrimaryLight,
                contentDescription = null
            )
            Spacer(Modifier.height(16.dp))
            Text("Movino", style = MaterialTheme.typography.headlineSmall, color = OnPrimaryLight)
        }

    }
}

@Preview
@Composable
private fun Preview() {
    SplashUI()

}