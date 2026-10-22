package com.maansiauto.panelconfigurator.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maansiauto.panelconfigurator.data.CustomerInfo
import com.maansiauto.panelconfigurator.data.EquipmentSelection
import com.maansiauto.panelconfigurator.data.PanelCatalog
import com.maansiauto.panelconfigurator.data.PanelRequest
import com.maansiauto.panelconfigurator.data.PanelRequestRepository
import com.maansiauto.panelconfigurator.data.RatingSpec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConfiguratorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PanelRequestRepository(application)

    // ---- wizard state (Compose state so every change redraws the screen) ----
    var step: Int by mutableStateOf(0)
        private set
    var panelTypeId: String? by mutableStateOf(null)
    var customPanelNote: String by mutableStateOf("")
    var rating: RatingSpec by mutableStateOf(RatingSpec())
    var equipmentSelections: Map<String, EquipmentSelection> by mutableStateOf(emptyMap())
    var customer: CustomerInfo by mutableStateOf(CustomerInfo())

    private val _lastSubmitted = MutableStateFlow<PanelRequest?>(null)
    val lastSubmitted: StateFlow<PanelRequest?> = _lastSubmitted

    val allRequests: StateFlow<List<PanelRequest>> =
        repository.requests.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun goToStep(target: Int) { step = target }
    fun nextStep() { if (step < 3) step += 1 }
    fun prevStep() { if (step > 0) step -= 1 }

    fun selectPanelType(id: String) { panelTypeId = id }

    fun toggleEquipment(item: com.maansiauto.panelconfigurator.data.EquipmentCatalogItem) {
        val current = equipmentSelections.toMutableMap()
        if (current.containsKey(item.id)) {
            current.remove(item.id)
        } else {
            current[item.id] = EquipmentSelection(
                itemId = item.id,
                name = item.name,
                unit = item.unit,
                rating = item.ratings.firstOrNull() ?: "",
                spec = item.specs.firstOrNull() ?: "",
                qty = 1
            )
        }
        equipmentSelections = current
    }

    fun updateEquipmentRating(itemId: String, value: String) {
        equipmentSelections = equipmentSelections.toMutableMap().apply {
            this[itemId]?.let { this[itemId] = it.copy(rating = value) }
        }
    }

    fun updateEquipmentSpec(itemId: String, value: String) {
        equipmentSelections = equipmentSelections.toMutableMap().apply {
            this[itemId]?.let { this[itemId] = it.copy(spec = value) }
        }
    }

    fun changeQty(itemId: String, delta: Int) {
        equipmentSelections = equipmentSelections.toMutableMap().apply {
            this[itemId]?.let {
                val newQty = (it.qty + delta).coerceAtLeast(1)
                this[itemId] = it.copy(qty = newQty)
            }
        }
    }

    fun canProceed(): Boolean = when (step) {
        0 -> panelTypeId != null && (panelTypeId != "custom" || customPanelNote.isNotBlank())
        1 -> rating.voltage.isNotBlank() && rating.currentRating.isNotBlank()
        2 -> equipmentSelections.isNotEmpty()
        3 -> customer.name.isNotBlank() && customer.phone.isNotBlank()
        else -> true
    }

    fun resetWizard() {
        step = 0
        panelTypeId = null
        customPanelNote = ""
        rating = RatingSpec()
        equipmentSelections = emptyMap()
        customer = CustomerInfo()
    }

    fun submit() {
        if (!canProceed()) return
        val panelInfo = PanelCatalog.panelTypes.find { it.id == panelTypeId }
        val request = PanelRequest(
            id = "MAC-" + System.currentTimeMillis().toString(36).uppercase(),
            submittedAt = System.currentTimeMillis(),
            panelTypeId = panelTypeId ?: "",
            panelTypeName = panelInfo?.name ?: (panelTypeId ?: ""),
            customPanelNote = if (panelTypeId == "custom") customPanelNote else "",
            rating = rating,
            equipment = equipmentSelections.values.toList(),
            customer = customer
        )
        // Show it on the confirmation screen right away, then save in the background.
        _lastSubmitted.value = request
        viewModelScope.launch {
            repository.save(request)
        }
    }

    fun deleteRequest(id: String) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun summaryText(request: PanelRequest): String {
        val df = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val sb = StringBuilder()
        sb.appendLine("MAANSI AUTO & ELECTRICAL CONTROL SYSTEM - Panel Requirement")
        sb.appendLine("Reference: ${request.id}")
        sb.appendLine("Date: ${df.format(Date(request.submittedAt))}")
        sb.appendLine()
        sb.appendLine("Panel Type: ${request.panelTypeName}")
        if (request.customPanelNote.isNotBlank()) sb.appendLine("Custom Requirement Notes: ${request.customPanelNote}")
        sb.appendLine("Voltage: ${request.rating.voltage} | Phase: ${request.rating.phase}")
        sb.appendLine("Current Rating: ${request.rating.currentRating}A | Short Circuit Rating: ${request.rating.shortCircuitRating.ifBlank { "-" }}")
        sb.appendLine("Enclosure: ${request.rating.enclosure.ifBlank { "-" }} | Mounting: ${request.rating.mounting.ifBlank { "-" }}")
        sb.appendLine()
        sb.appendLine("Bill of Materials:")
        request.equipment.forEachIndexed { i, e ->
            sb.appendLine("${i + 1}. ${e.name} - ${e.rating}, ${e.spec} - Qty: ${e.qty} ${e.unit}")
        }
        sb.appendLine()
        sb.appendLine("Customer: ${request.customer.name}${if (request.customer.company.isNotBlank()) " (${request.customer.company})" else ""}")
        sb.appendLine("Phone: ${request.customer.phone}${if (request.customer.email.isNotBlank()) " | Email: ${request.customer.email}" else ""}")
        if (request.customer.site.isNotBlank()) sb.appendLine("Site / Delivery Location: ${request.customer.site}")
        if (request.customer.notes.isNotBlank()) sb.appendLine("Notes: ${request.customer.notes}")
        return sb.toString()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ConfiguratorViewModel(application) as T
        }
    }
}
