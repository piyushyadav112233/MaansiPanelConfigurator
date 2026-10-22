package com.maansiauto.panelconfigurator.data

object PanelCatalog {

    val panelTypes = listOf(
        PanelType("lt-distribution", "LT Distribution Panel", "Splits incoming LT power into multiple outgoing feeders for a building or facility."),
        PanelType("pcc", "Power Control Center (PCC)", "Main incoming control and bus-coupling for facility-wide power distribution."),
        PanelType("mcc", "Motor Control Center (MCC)", "Controls and protects multiple motor feeders from a single panel."),
        PanelType("apfc", "APFC Panel", "Automatic power factor correction to cut reactive-power penalties."),
        PanelType("ups", "UPS Panel", "Input, output and bypass control for an uninterruptible power system."),
        PanelType("sync", "Synchronization Panel", "Synchronizes generators or sources onto a common bus."),
        PanelType("machine", "Machine / Canister Control Panel", "Purpose-built control panel for a specific machine or production line."),
        PanelType("custom", "Custom Panel", "Not sure which category fits? Pick this and describe what you need.")
    )

    val voltageOptions = listOf(
        "230V AC (Single Phase)", "415V AC (Three Phase)", "690V AC (Three Phase)", "Other / Not sure"
    )
    val shortCircuitOptions = listOf("16 kA", "25 kA", "35 kA", "50 kA", "65 kA")
    val enclosureOptions = listOf(
        "IP42 (Indoor, general)", "IP54 (Indoor, dusty)", "IP55 (Semi-outdoor)", "IP65 (Outdoor)"
    )
    val mountingOptions = listOf("Floor Standing", "Wall Mounted", "Not sure")

    val equipmentCatalog = listOf(
        EquipmentCatalogItem("mccb", "MCCB (Moulded Case Circuit Breaker)", "Nos", "Rated Current",
            listOf("63A", "100A", "125A", "160A", "200A", "250A", "400A", "630A", "800A"),
            "Poles / Breaking Capacity", listOf("3P, 25kA", "3P, 35kA", "4P, 25kA", "4P, 35kA"),
            listOf("lt-distribution", "pcc", "mcc", "ups", "sync", "machine")),
        EquipmentCatalogItem("acb", "ACB (Air Circuit Breaker)", "Nos", "Rated Current",
            listOf("800A", "1000A", "1250A", "1600A", "2000A", "2500A", "3200A", "4000A"),
            "Poles / Operation", listOf("3P, Manual", "3P, Motorized", "4P, Manual", "4P, Motorized"),
            listOf("lt-distribution", "pcc", "ups", "sync")),
        EquipmentCatalogItem("contactor", "Power Contactor", "Nos", "Rated Current",
            listOf("9A", "18A", "32A", "40A", "65A", "95A", "150A", "225A"),
            "Duty", listOf("AC-3 Duty (Motor)", "AC-1 Duty (Resistive)"),
            listOf("mcc", "apfc", "machine")),
        EquipmentCatalogItem("olr", "Overload Relay", "Nos", "Current Range",
            listOf("0.4-0.6A", "1-1.6A", "4-6A", "9-13A", "17-25A", "30-40A", "55-70A"),
            "Type", listOf("Bimetallic", "Electronic"),
            listOf("mcc", "machine")),
        EquipmentCatalogItem("vfd", "VFD (Variable Frequency Drive)", "Nos", "Motor Rating",
            listOf("1 HP", "3 HP", "5 HP", "10 HP", "20 HP", "50 HP", "100 HP"),
            "Control Type", listOf("V/F Control", "Vector Control"),
            listOf("mcc", "machine")),
        EquipmentCatalogItem("plc", "PLC / Automation Controller", "Set", "I/O Size",
            listOf("Compact - 16 I/O", "Compact - 32 I/O", "Modular - Expandable"),
            "Display", listOf("With HMI Screen", "Without HMI"),
            listOf("machine", "sync")),
        EquipmentCatalogItem("meter", "Digital Multifunction Meter", "Nos", "Function Level",
            listOf("Basic (V, A, Hz)", "Advanced (kWh, PF, THD)"),
            "Panel Cutout", listOf("96 x 96 mm", "72 x 72 mm"),
            listOf("lt-distribution", "pcc", "mcc", "apfc", "ups", "sync")),
        EquipmentCatalogItem("capacitor", "APFC Capacitor Bank", "Nos", "Rating",
            listOf("5 kVAR", "10 kVAR", "25 kVAR", "50 kVAR"),
            "Duty", listOf("Standard Duty", "Heavy Duty"),
            listOf("apfc")),
        EquipmentCatalogItem("apfcRelay", "APFC Relay / Controller", "Nos", "Steps",
            listOf("6 Step", "12 Step"),
            "Type", listOf("Standard", "With Harmonic Filter"),
            listOf("apfc")),
        EquipmentCatalogItem("syncRelay", "Synchronizing Relay / Module", "Set", "Mode",
            listOf("Manual Sync", "Auto Sync"),
            "Checking", listOf("Single Check", "Dual Check"),
            listOf("sync")),
        EquipmentCatalogItem("staticSwitch", "Static Transfer Switch", "Nos", "Rated Current",
            listOf("63A", "100A", "200A", "400A"),
            "Transfer Type", listOf("Make-Before-Break", "Break-Before-Make"),
            listOf("ups")),
        EquipmentCatalogItem("busbar", "Copper Busbar", "Set", "Current Rating",
            listOf("200A", "400A", "630A", "800A", "1000A", "1600A"),
            "Finish", listOf("Tinned Copper", "Bare Copper"),
            listOf("lt-distribution", "pcc", "mcc", "ups", "sync", "machine")),
        EquipmentCatalogItem("indicator", "Indicator Lamps (R/Y/B)", "Set", "Size",
            listOf("22 mm LED"),
            "Type", listOf("Standard", "With Buzzer"),
            listOf("lt-distribution", "pcc", "mcc", "apfc", "ups", "sync", "machine")),
        EquipmentCatalogItem("selectorSwitch", "Selector Switch", "Nos", "Positions",
            listOf("2 Position", "3 Position"),
            "Return", listOf("Spring Return", "Maintained"),
            listOf("mcc", "sync", "machine")),
        EquipmentCatalogItem("pushbutton", "Push Button Station", "Set", "Function",
            listOf("Start / Stop", "Start / Stop / Emergency"),
            "Size", listOf("22 mm", "30 mm"),
            listOf("mcc", "machine")),
        EquipmentCatalogItem("terminal", "Terminal Blocks", "Set", "Size",
            listOf("4 sqmm", "6 sqmm", "10 sqmm", "16 sqmm", "25 sqmm"),
            "Type", listOf("Standard", "Feed-through"),
            listOf("lt-distribution", "pcc", "mcc", "apfc", "ups", "sync", "machine")),
        EquipmentCatalogItem("cableGland", "Cable Glands", "Nos", "Size",
            listOf("1/2\"", "3/4\"", "1\"", "1.5\"", "2\""),
            "Type", listOf("Single Compression", "Double Compression"),
            listOf("lt-distribution", "pcc", "mcc", "apfc", "ups", "sync", "machine")),
        EquipmentCatalogItem("coolingFan", "Panel Cooling Fan", "Nos", "Size",
            listOf("4 inch", "6 inch", "8 inch"),
            "Filter", listOf("With Filter", "Without Filter"),
            listOf("mcc", "ups", "machine")),
        EquipmentCatalogItem("spd", "Surge Protection Device (SPD)", "Nos", "Type",
            listOf("Type 1", "Type 2", "Type 1+2"),
            "Phase", listOf("Single Phase", "Three Phase"),
            listOf("lt-distribution", "pcc", "ups"))
    )
}
