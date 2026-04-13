package com.vyn.player.ui.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PermissionCard(permissionName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "Required Permission", style = MaterialTheme.typography.titleMedium)
            Text(text = permissionName, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "VYN Player needs media access to load and play songs stored on your device.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
