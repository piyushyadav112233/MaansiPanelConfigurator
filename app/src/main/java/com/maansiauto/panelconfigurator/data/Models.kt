package com.maansiauto.panelconfigurator.data

data class PanelType(
    val id: String,
    val name: String,
    val desc: String
)

data class EquipmentCatalogItem(
    val id: String,
    val name: String,
    val unit: String,
    val ratingLabel: String,
    val ratings: List<String>,
    val specLabel: String,
    val specs: List<String>,
    val recommendedFor: List<String>
)

data class EquipmentSelection(
    val itemId: String,
    val name: String,
    val unit: String,
    val rating: String,
    val spec: String,
    val qty: Int
)

data class RatingSpec(
    val voltage: String = "",
    val phase: String = "Three Phase",
    val currentRating: String = "",
    val shortCircuitRating: String = "",
    val enclosure: String = "",
    val mounting: String = ""
)

data class CustomerInfo(
    val name: String = "",
    val company: String = "",
    val phone: String = "",
    val email: String = "",
    val site: String = "",
    val notes: String = ""
)

data class PanelRequest(
    val id: String,
    val submittedAt: Long,
    val panelTypeId: String,
    val panelTypeName: String,
    val customPanelNote: String,
    val rating: RatingSpec,
    val equipment: List<EquipmentSelection>,
    val customer: CustomerInfo
)
