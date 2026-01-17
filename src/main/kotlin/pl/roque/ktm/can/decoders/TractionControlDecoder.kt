package pl.roque.ktm.can.decoders

import pl.roque.ktm.can.models.TractionData

/**
 * Dekoder dla CAN ID 0x450: Traction Control Button
 * Częstotliwość: 50ms
 */
class TractionControlDecoder {

    /**
     * Dekoduje dane z CAN ID 0x450
     */
    fun decode(data: ByteArray): TractionData? {
        if (data.size < 3) return null

        val tractionControlButton = data[2].toInt() and 0b00000001

        return TractionData(
            tractionControlButton = tractionControlButton
        )
    }

    /**
     * Dekoduje stan przycisku traction control
     */
    fun decodeTractionControlButton(data: ByteArray): Int? {
        if (data.size < 3) return null
        return data[2].toInt() and 0b00000001
    }
}