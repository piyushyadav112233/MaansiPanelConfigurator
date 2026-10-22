package com.maansiauto.panelconfigurator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maansiauto.panelconfigurator.ui.theme.Border
import com.maansiauto.panelconfigurator.ui.theme.Ink
import com.maansiauto.panelconfigurator.ui.theme.InkSoft
import com.maansiauto.panelconfigurator.ui.theme.Navy
import com.maansiauto.panelconfigurator.ui.theme.Surface

@Composable
fun AppTopBar(
    onTeamView: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    backLabel: String = "Back"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Navy, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Column {
                Text("MAANSI AUTO & ELECTRICAL", fontSize = 13.sp, lineHeight = 17.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Ink)
                Text("Panel Configurator", fontSize = 11.sp, lineHeight = 15.sp, color = InkSoft)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onTeamView != null) {
                TextButton(onClick = onTeamView) { Text("Team View", color = Navy, fontSize = 13.sp) }
            }
            if (onBack != null) {
                TextButton(onClick = onBack) { Text(backLabel, color = Navy, fontSize = 13.sp) }
            }
        }
    }
}