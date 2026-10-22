package com.maansiauto.panelconfigurator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maansiauto.panelconfigurator.ui.theme.Background
import com.maansiauto.panelconfigurator.ui.theme.Border
import com.maansiauto.panelconfigurator.ui.theme.Green
import com.maansiauto.panelconfigurator.ui.theme.GreenSoft
import com.maansiauto.panelconfigurator.ui.theme.Ink
import com.maansiauto.panelconfigurator.ui.theme.InkSoft
import com.maansiauto.panelconfigurator.ui.theme.Navy
import com.maansiauto.panelconfigurator.ui.theme.Surface
import com.maansiauto.panelconfigurator.ui.theme.SurfaceSunken
import com.maansiauto.panelconfigurator.util.TeamContact
import com.maansiauto.panelconfigurator.util.copyToClipboard
import com.maansiauto.panelconfigurator.util.emailTeam
import com.maansiauto.panelconfigurator.util.shareText
import com.maansiauto.panelconfigurator.util.whatsappTeam
import com.maansiauto.panelconfigurator.viewmodel.ConfiguratorViewModel

@Composable
fun ConfirmationScreen(
    viewModel: ConfiguratorViewModel,
    onHome: () -> Unit,
    onConfigureAnother: () -> Unit,
    onTeamView: () -> Unit
) {
    val request by viewModel.lastSubmitted.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        AppTopBar(onTeamView = onTeamView, onBack = onHome, backLabel = "Home")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(14.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.size(56.dp).background(GreenSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Green, modifier = Modifier.size(30.dp))
                    }
                    Text("Requirement saved", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.padding(top = 14.dp))
                    request?.let {
                        Text(
                            "Reference ${it.id} has been saved on this device. Tap \"Send to MAANSI team\" below so our engineering and quotation team receives it and can get back to you with pricing and design.",
                            fontSize = 13.5.sp, color = InkSoft, modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            item {
                request?.let { req ->
                    Card(
                        modifier = Modifier.fillMaxWidth().border(1.dp, Border, RoundedCornerShape(10.dp)),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Requirement Summary", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                OutlinedButton(onClick = { copyToClipboard(context, viewModel.summaryText(req)) }) {
                                    Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Text("Copy", fontSize = 12.sp)
                                }
                            }
                            Text(
                                viewModel.summaryText(req),
                                fontSize = 12.sp,
                                color = Ink,
                                lineHeight = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .background(SurfaceSunken, RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }

            item {
                request?.let { req ->
                    val summary = viewModel.summaryText(req)
                    val subject = "Panel requirement ${req.id} - ${req.customer.name}"
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { shareText(context, subject, summary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Navy)
                        ) {
                            Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("  Send to MAANSI team", fontWeight = FontWeight.Bold)
                        }
                        if (TeamContact.WHATSAPP.isNotBlank()) {
                            OutlinedButton(
                                onClick = { whatsappTeam(context, summary) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Send on WhatsApp")
                            }
                        }
                        if (TeamContact.EMAIL.isNotBlank()) {
                            OutlinedButton(
                                onClick = { emailTeam(context, subject, summary) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Filled.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text("  Send by email")
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onConfigureAnother, modifier = Modifier.fillMaxWidth()) {
                        Text("Configure another panel")
                    }
                    OutlinedButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
                        Text("Back to home")
                    }
                }
            }
        }
    }
}
