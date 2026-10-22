package com.maansiauto.panelconfigurator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maansiauto.panelconfigurator.ui.theme.Background
import com.maansiauto.panelconfigurator.ui.theme.Border
import com.maansiauto.panelconfigurator.ui.theme.GreenSoft
import com.maansiauto.panelconfigurator.ui.theme.Green
import com.maansiauto.panelconfigurator.ui.theme.Ink
import com.maansiauto.panelconfigurator.ui.theme.InkSoft
import com.maansiauto.panelconfigurator.ui.theme.Navy
import com.maansiauto.panelconfigurator.ui.theme.Surface

private data class Step(val title: String, val desc: String)

private val steps = listOf(
    Step("1. Panel Type", "Choose the category that matches your application."),
    Step("2. Rating & Specs", "Set voltage, current rating and enclosure."),
    Step("3. Equipment", "Select components, specs and quantities."),
    Step("4. Review & Submit", "Get a requirement list and send it to our team.")
)

@Composable
fun LandingScreen(onStart: () -> Unit, onTeamView: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        AppTopBar(onTeamView = onTeamView)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = spacedBy(14.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .background(GreenSoft, RoundedCornerShape(100.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text("⚡ Electrical Panel Configurator", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Green)
                }
            }
            item {
                Text(
                    "Configure the panel you need. We'll turn it into a quotation.",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                    lineHeight = 32.sp
                )
            }
            item {
                Text(
                    "Pick a panel type, set the rating, choose your equipment, and get a clear requirement list — no electrical background needed. Our engineering team uses exactly what you select to prepare your design and quote.",
                    fontSize = 14.sp,
                    color = InkSoft,
                    lineHeight = 21.sp
                )
            }
            item {
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Navy),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Start Configuration", fontWeight = FontWeight.Bold)
                    Icon(Icons.Filled.ChevronRight, contentDescription = null)
                }
            }
            items(steps) { s ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Border),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Icon(Icons.Filled.Bolt, contentDescription = null, tint = Navy)
                        Text(s.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                        Text(s.desc, fontSize = 12.5.sp, color = InkSoft, lineHeight = 17.sp)
                    }
                }
            }
        }
    }
}
