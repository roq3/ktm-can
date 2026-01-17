package pl.roque.ktm.can.decoders

import pl.roque.ktm.can.models.LightsData

/**
 * Dekoder dla CAN ID 0x650: Lights/LED Status
 * Częstotliwość: nieznana
 */
class LightsDecoder {

    /**
     * Dekoduje stan świateł LED z CAN ID 0x650
     *
     * Format ramki:
     * - 32-bitowe słowo (4 bajty)
     * - Bit 3: DRL Active
     * - Bit 5: High Beam Active
     * - Bit 6: Low Beam Active
     * - Bit 26: High Beam broken
     * - Bit 27: Low Beam broken
     */
    fun decode(data: ByteArray): LightsData? {
        if (data.size < 4) return null

        // Dekodowanie bitów z całego 32-bitowego słowa
        val word = (data[0].toInt() and 0xFF) or
                  ((data[1].toInt() and 0xFF) shl 8) or
                  ((data[2].toInt() and 0xFF) shl 16) or
                  ((data[3].toInt() and 0xFF) shl 24)

        return LightsData(
            drlActive = (word and (1 shl 3)) != 0,
            highBeamActive = (word and (1 shl 5)) != 0,  // SWAP: bit 5 -> High Beam
            lowBeamActive = (word and (1 shl 6)) != 0,   // SWAP: bit 6 -> Low Beam
            highBeamBroken = (word and (1 shl 26)) != 0,
            lowBeamBroken = (word and (1 shl 27)) != 0
        )
    }

    /**
     * Dekoduje czy światła dzienne są aktywne
     */
    fun decodeDrlActive(data: ByteArray): Boolean? {
        if (data.size < 4) return null
        val word = (data[0].toInt() and 0xFF) or
                  ((data[1].toInt() and 0xFF) shl 8) or
                  ((data[2].toInt() and 0xFF) shl 16) or
                  ((data[3].toInt() and 0xFF) shl 24)
        return (word and (1 shl 3)) != 0
    }

    /**
     * Dekoduje czy długie światła są aktywne
     */
    fun decodeHighBeamActive(data: ByteArray): Boolean? {
        if (data.size < 4) return null
        val word = (data[0].toInt() and 0xFF) or
                  ((data[1].toInt() and 0xFF) shl 8) or
                  ((data[2].toInt() and 0xFF) shl 16) or
                  ((data[3].toInt() and 0xFF) shl 24)
        return (word and (1 shl 5)) != 0
    }

    /**
     * Dekoduje czy krótkie światła są aktywne
     */
    fun decodeLowBeamActive(data: ByteArray): Boolean? {
        if (data.size < 4) return null
        val word = (data[0].toInt() and 0xFF) or
                  ((data[1].toInt() and 0xFF) shl 8) or
                  ((data[2].toInt() and 0xFF) shl 16) or
                  ((data[3].toInt() and 0xFF) shl 24)
        return (word and (1 shl 6)) != 0
    }
}