@file:OptIn(ExperimentalMaterial3Api::class)

package com.maansiauto.panelconfigurator.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maansiauto.panelconfigurator.data.EquipmentCatalogItem
import com.maansiauto.panelconfigurator.data.PanelCatalog
import com.maansiauto.panelconfigurator.ui.theme.Background
import com.maansiauto.panelconfigurator.ui.theme.Border
import com.maansiauto.panelconfigurator.ui.theme.BorderStrong
import com.maansiauto.panelconfigurator.ui.theme.Ink
import com.maansiauto.panelconfigurator.ui.theme.InkSoft
import com.maansiauto.panelconfigurator.ui.theme.Navy
import com.maansiauto.panelconfigurator.ui.theme.Surface
import com.maansiauto.panelconfigurator.ui.theme.SurfaceSunken
import com.maansiauto.panelconfigurator.viewmodel.ConfiguratorViewModel

private val stepLabels = listOf(
    "Panel Type",
    "Rating & Specs",
    "Equipment",
    "Review & Submit"
)

@Composable
fun WizardScreen(
    viewModel: ConfiguratorViewModel,
    onCancel: () -> Unit,
    onSubmitted: () -> Unit,
    onTeamView: () -> Unit
) {
    val listState = rememberLazyListState()

    // Start every step at the top of the page.
    LaunchedEffect(viewModel.step) {
        listState.scrollToItem(0)
    }

    // Phone Back button goes to the previous step; on step 1 it leaves the wizard.
    BackHandler {
        if (viewModel.step > 0) {
            viewModel.prevStep()
        } else {
            onCancel()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        AppTopBar(
            onTeamView = onTeamView,
            onBack = onCancel,
            backLabel = "Cancel"
        )

        StepRail(current = viewModel.step)

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                when (viewModel.step) {
                    0 -> PanelTypeStep(viewModel)
                    1 -> RatingStep(viewModel)
                    2 -> EquipmentStep(viewModel)
                    3 -> ReviewStep(viewModel)
                }
            }
        }

        WizardNav(
            viewModel = viewModel,
            onSubmitted = onSubmitted
        )
    }
}

@Composable
private fun StepRail(current: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        stepLabels.forEachIndexed { i, label ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(
                            color = if (i <= current) Navy else Color.White,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.5.dp,
                            color = if (i <= current) Navy else BorderStrong,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (i < current) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    } else {
                        Text(
                            text = "${i + 1}",
                            fontSize = 11.sp,
                            color = if (i == current) Color.White else InkSoft,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = if (i == current) Ink else InkSoft,
                    fontWeight = if (i == current) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    }
                )
            }
        }
    }
}

@Composable
private fun WizardNav(
    viewModel: ConfiguratorViewModel,
    onSubmitted: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { viewModel.prevStep() },
            enabled = viewModel.step > 0
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = null
            )
            Text("Back")
        }

        if (viewModel.step < 3) {
            Button(
                onClick = { viewModel.nextStep() },
                enabled = viewModel.canProceed(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Navy
                )
            ) {
                Text("Continue")
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null
                )
            }
        } else {
            Button(
                onClick = {
                    viewModel.submit()
                    onSubmitted()
                },
                enabled = viewModel.canProceed(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Navy
                )
            ) {
                Text("Submit requirement")
            }
        }
    }
}

/* Step 1: Panel type */

@Composable
private fun PanelTypeStep(viewModel: ConfiguratorViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "What kind of panel do you need?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )

        Text(
            text = "Choose the category closest to your application.",
            fontSize = 13.sp,
            color = InkSoft
        )

        PanelCatalog.panelTypes.forEach { panel ->
            val selected = viewModel.panelTypeId == panel.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.5.dp,
                        color = if (selected) Navy else Border,
                        shape = RoundedCornerShape(10.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) {
                        SurfaceSunken
                    } else {
                        Surface
                    }
                ),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    viewModel.selectPanelType(panel.id)
                }
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = panel.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Ink
                    )

                    Text(
                        text = panel.desc,
                        fontSize = 12.sp,
                        color = InkSoft,
                        modifier = Modifier.padding(top = 3.dp),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        if (viewModel.panelTypeId == "custom") {
            OutlinedTextField(
                value = viewModel.customPanelNote,
                onValueChange = {
                    viewModel.customPanelNote = it
                },
                label = {
                    Text("Describe what you need")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }
    }
}

/* Step 2: Rating and specifications */

@Composable
private fun RatingStep(viewModel: ConfiguratorViewModel) {
    var rating by remember {
        mutableStateOf(viewModel.rating)
    }

    fun push() {
        viewModel.rating = rating
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Panel rating & specifications",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )

        Text(
            text = "Not sure about something? Pick your best guess — our team will confirm it.",
            fontSize = 13.sp,
            color = InkSoft
        )

        LabeledDropdown(
            label = "Voltage",
            value = rating.voltage,
            options = PanelCatalog.voltageOptions,
            onSelect = {
                val phase = when {
                    it.contains("Single") -> "Single Phase"
                    it.contains("Three") -> "Three Phase"
                    else -> rating.phase
                }
                rating = rating.copy(voltage = it, phase = phase)
                push()
            }
        )

        LabeledDropdown(
            label = "Phase",
            value = rating.phase,
            options = listOf("Single Phase", "Three Phase"),
            onSelect = {
                rating = rating.copy(phase = it)
                push()
            }
        )

        OutlinedTextField(
            value = rating.currentRating,
            onValueChange = { input ->
                if (input.all { c -> c.isDigit() || c == '.' }) {
                    rating = rating.copy(currentRating = input)
                    push()
                }
            },
            label = {
                Text("Current Rating (Amps)")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        LabeledDropdown(
            label = "Short Circuit Rating (optional)",
            value = rating.shortCircuitRating,
            options = PanelCatalog.shortCircuitOptions,
            onSelect = {
                rating = rating.copy(shortCircuitRating = it)
                push()
            }
        )

        LabeledDropdown(
            label = "Enclosure Protection (optional)",
            value = rating.enclosure,
            options = PanelCatalog.enclosureOptions,
            onSelect = {
                rating = rating.copy(enclosure = it)
                push()
            }
        )

        LabeledDropdown(
            label = "Mounting (optional)",
            value = rating.mounting,
            options = PanelCatalog.mountingOptions,
            onSelect = {
                rating = rating.copy(mounting = it)
                push()
            }
        )
    }
}

@Composable
private fun LabeledDropdown(
    label: String,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = InkSoft
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text("Select")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/* Step 3: Equipment */

@Composable
private fun EquipmentStep(viewModel: ConfiguratorViewModel) {
    var showAll by remember {
        mutableStateOf(viewModel.panelTypeId == "custom")
    }

    val recommended = PanelCatalog.equipmentCatalog.filter {
        it.recommendedFor.contains(viewModel.panelTypeId)
    }

    val others = PanelCatalog.equipmentCatalog.filterNot {
        it.recommendedFor.contains(viewModel.panelTypeId)
    }

    val equipmentList = if (showAll) {
        PanelCatalog.equipmentCatalog
    } else {
        recommended
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Choose your equipment",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )

        Text(
            text = "Items commonly used in this panel type are shown first. Add anything else you need below.",
            fontSize = 13.sp,
            color = InkSoft
        )

        equipmentList.forEach { item ->
            EquipmentRow(
                item = item,
                viewModel = viewModel
            )
        }

        if (!showAll && others.isNotEmpty()) {
            OutlinedButton(
                onClick = {
                    showAll = true
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Text("Show all equipment types (${others.size} more)")
            }
        }
    }
}

@Composable
private fun EquipmentRow(
    item: EquipmentCatalogItem,
    viewModel: ConfiguratorViewModel
) {
    val selection = viewModel.equipmentSelections[item.id]
    val included = selection != null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (included) Navy else Border,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (included) {
                SurfaceSunken
            } else {
                Surface
            }
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = included,
                    onCheckedChange = {
                        viewModel.toggleEquipment(item)
                    }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, top = 10.dp)
                ) {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = Ink
                    )

                    Text(
                        text = "Unit: ${item.unit}",
                        fontSize = 11.5.sp,
                        color = InkSoft
                    )
                }
            }

            if (included && selection != null) {
                Column(
                    modifier = Modifier.padding(
                        start = 32.dp,
                        top = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LabeledDropdown(
                        label = item.ratingLabel,
                        value = selection.rating,
                        options = item.ratings,
                        onSelect = {
                            viewModel.updateEquipmentRating(
                                item.id,
                                it
                            )
                        }
                    )

                    LabeledDropdown(
                        label = item.specLabel,
                        value = selection.spec,
                        options = item.specs,
                        onSelect = {
                            viewModel.updateEquipmentSpec(
                                item.id,
                                it
                            )
                        }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Qty",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkSoft
                        )

                        IconButton(
                            onClick = {
                                viewModel.changeQty(item.id, -1)
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Decrease quantity",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "${selection.qty}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        IconButton(
                            onClick = {
                                viewModel.changeQty(item.id, 1)
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Increase quantity",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/* Step 4: Review and submit */

@Composable
private fun ReviewStep(viewModel: ConfiguratorViewModel) {
    val panelInfo = PanelCatalog.panelTypes.find {
        it.id == viewModel.panelTypeId
    }

    val equipmentList = viewModel.equipmentSelections.values.toList()

    var customer by remember {
        mutableStateOf(viewModel.customer)
    }

    fun push() {
        viewModel.customer = customer
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Review & submit",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )

        Text(
            text = "Check your selections, add your contact details, and send the requirement to our team.",
            fontSize = 13.sp,
            color = InkSoft
        )

        SectionCard(title = "Panel") {
            Text(
                text = "${panelInfo?.name ?: ""}${if (viewModel.customPanelNote.isNotBlank()) " — ${viewModel.customPanelNote}" else ""}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = Ink
            )

            Text(
                text = "${viewModel.rating.voltage} · ${viewModel.rating.phase} · ${viewModel.rating.currentRating}A" +
                        (if (viewModel.rating.shortCircuitRating.isNotBlank()) {
                            " · ${viewModel.rating.shortCircuitRating}"
                        } else {
                            ""
                        }) +
                        (if (viewModel.rating.enclosure.isNotBlank()) {
                            " · ${viewModel.rating.enclosure}"
                        } else {
                            ""
                        }),
                fontSize = 12.5.sp,
                color = InkSoft,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        SectionCard(title = "Bill of Materials (${equipmentList.size} items)") {
            if (equipmentList.isEmpty()) {
                Text(
                    text = "No equipment selected.",
                    fontSize = 12.5.sp,
                    color = InkSoft
                )
            } else {
                equipmentList.forEachIndexed { index, equipment ->
                    Text(
                        text = "${index + 1}. ${equipment.name} — ${equipment.rating}, ${equipment.spec} — Qty: ${equipment.qty} ${equipment.unit}",
                        fontSize = 12.5.sp,
                        color = Ink,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }

        SectionCard(title = "Your details") {
            OutlinedTextField(
                value = customer.name,
                onValueChange = {
                    customer = customer.copy(name = it)
                    push()
                },
                label = {
                    Text("Name *")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = customer.company,
                onValueChange = {
                    customer = customer.copy(company = it)
                    push()
                },
                label = {
                    Text("Company")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = customer.phone,
                onValueChange = {
                    customer = customer.copy(phone = it)
                    push()
                },
                label = {
                    Text("Phone *")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = customer.email,
                onValueChange = {
                    customer = customer.copy(email = it)
                    push()
                },
                label = {
                    Text("Email")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = customer.site,
                onValueChange = {
                    customer = customer.copy(site = it)
                    push()
                },
                label = {
                    Text("Site / Delivery Location")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = customer.notes,
                onValueChange = {
                    customer = customer.copy(notes = it)
                    push()
                },
                label = {
                    Text("Additional Notes")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
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
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Ink
            )

            content()
        }
    }
}