package com.maansiauto.panelconfigurator.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maansiauto.panelconfigurator.data.PanelRequest
import com.maansiauto.panelconfigurator.ui.theme.Background
import com.maansiauto.panelconfigurator.ui.theme.Border
import com.maansiauto.panelconfigurator.ui.theme.Ink
import com.maansiauto.panelconfigurator.ui.theme.InkSoft
import com.maansiauto.panelconfigurator.ui.theme.Navy
import com.maansiauto.panelconfigurator.ui.theme.Surface
import com.maansiauto.panelconfigurator.util.copyToClipboard
import com.maansiauto.panelconfigurator.util.shareText
import com.maansiauto.panelconfigurator.viewmodel.ConfiguratorViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TeamViewScreen(
    viewModel: ConfiguratorViewModel,
    onBack: () -> Unit
) {
    val requests by viewModel.allRequests.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        AppTopBar(
            onBack = onBack,
            backLabel = "Home"
        )

        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
        ) {
            Text(
                text = "Submitted Requests",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )

            Text(
                text = "${requests.size} request${if (requests.size != 1) "s" else ""} saved on this device — for the quotation & design team.",
                fontSize = 12.5.sp,
                color = InkSoft,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (requests.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No requests yet. Once a customer submits a configuration, it will appear here.",
                    color = InkSoft,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 4.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(requests) { req ->
                    RequestRow(
                        req = req,
                        viewModel = viewModel,
                        context = context
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestRow(
    req: PanelRequest,
    viewModel: ConfiguratorViewModel,
    context: Context
) {
    var expanded by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    val df = remember {
        SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale.getDefault()
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Border,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableRow {
                        expanded = !expanded
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${req.customer.name}${if (req.customer.company.isNotBlank()) " — ${req.customer.company}" else ""}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Ink
                    )

                    Text(
                        text = "${req.panelTypeName} · ${req.equipment.size} items · ${df.format(Date(req.submittedAt))}",
                        fontSize = 12.sp,
                        color = InkSoft
                    )
                }

                Icon(
                    imageVector = if (expanded) {
                        Icons.Filled.ExpandLess
                    } else {
                        Icons.Filled.ExpandMore
                    },
                    contentDescription = if (expanded) {
                        "Collapse request"
                    } else {
                        "Expand request"
                    },
                    tint = Navy
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(
                        text = "Phone: ${req.customer.phone}",
                        fontSize = 12.5.sp,
                        color = Ink
                    )

                    if (req.customer.email.isNotBlank()) {
                        Text(
                            text = "Email: ${req.customer.email}",
                            fontSize = 12.5.sp,
                            color = Ink
                        )
                    }

                    Text(
                        text = "Voltage: ${req.rating.voltage} · Current: ${req.rating.currentRating}A",
                        fontSize = 12.5.sp,
                        color = Ink
                    )

                    if (req.customer.site.isNotBlank()) {
                        Text(
                            text = "Site: ${req.customer.site}",
                            fontSize = 12.5.sp,
                            color = Ink
                        )
                    }

                    Text(
                        text = "Bill of Materials",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = Ink,
                        modifier = Modifier.padding(
                            top = 10.dp,
                            bottom = 4.dp
                        )
                    )

                    req.equipment.forEachIndexed { i, equipment ->
                        Text(
                            text = "${i + 1}. ${equipment.name} — ${equipment.rating}, ${equipment.spec} — Qty: ${equipment.qty} ${equipment.unit}",
                            fontSize = 12.sp,
                            color = InkSoft,
                            modifier = Modifier.padding(vertical = 1.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                copyToClipboard(
                                    context,
                                    viewModel.summaryText(req)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )

                            Text(
                                text = " Copy",
                                fontSize = 12.sp
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                shareText(
                                    context,
                                    "Panel requirement ${req.id} - ${req.customer.name}",
                                    viewModel.summaryText(req)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )

                            Text(
                                text = " Share",
                                fontSize = 12.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { confirmDelete = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )

                            Text(
                                text = " Delete",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete this request?") },
            text = {
                Text("${req.customer.name} - ${req.panelTypeName}. This cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDelete = false
                        viewModel.deleteRequest(req.id)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Helper to make a Row clickable.
private fun Modifier.clickableRow(
    onClick: () -> Unit
): Modifier = this.clickable(onClick = onClick)
