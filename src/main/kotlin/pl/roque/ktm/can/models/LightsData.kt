package pl.roque.ktm.can.models

/**
 * Stan świateł LED z CAN ID 0x650
 * Na podstawie dokumentacji KTM Duke 390
 *
 * @property drlActive Bit 3 - DRL Active (Daytime Running Lights)
 * @property highBeamActive Bit 5 - High Beam Active
 * @property lowBeamActive Bit 6 - Low Beam Active
 * @property highBeamBroken Bit 26 - High Beam broken
 * @property lowBeamBroken Bit 27 - Low Beam broken
 */
data class LightsData(
    val drlActive: Boolean,
    val highBeamActive: Boolean,
    val lowBeamActive: Boolean,
    val highBeamBroken: Boolean,
    val lowBeamBroken: Boolean
)