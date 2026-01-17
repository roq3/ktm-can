package pl.roque.ktm.can.decoders

import pl.roque.ktm.can.models.WheelSpeedData
import pl.roque.ktm.can.utils.BitOperations.parseBigEndianUInt16
import pl.roque.ktm.can.utils.BitOperations.hiNibble
import pl.roque.ktm.can.utils.BitOperations.loNibble

/**
 * Dekoder dla CAN ID 0x12B: Wheel Speed/Lean
 * Częstotliwość: 10ms
 */
class WheelSpeedDecoder {

    /**
     * Dekoduje dane z CAN ID 0x12B
     */
    fun decode(data: ByteArray): WheelSpeedData? {
        if (data.size < 8) return null

        val frontWheel = parseBigEndianUInt16(data[0], data[1])
        val rearWheel = parseBigEndianUInt16(data[2], data[3])

        // Tilt: D5 (8 bitów) + D6 high nibble (4 bity) = 12 bitów
        val tiltValue = ((data[5].toInt() and 0xFF) shl 4) or hiNibble(data[6])
        val tilt = signed12(tiltValue)

        // Lean: D6 low nibble (4 bity) + D7 (8 bitów) = 12 bitów
        val leanValue = (loNibble(data[6]) shl 8) or (data[7].toInt() and 0xFF)
        val lean = signed12(leanValue)

        return WheelSpeedData(
            frontWheel = frontWheel,
            rearWheel = rearWheel,
            tilt = tilt,
            lean = lean
        )
    }

    /**
     * Dekoduje prędkość koła przedniego
     */
    fun decodeFrontWheelSpeed(data: ByteArray): Int? {
        if (data.size < 2) return null
        return parseBigEndianUInt16(data[0], data[1])
    }

    /**
     * Dekoduje prędkość koła tylnego
     */
    fun decodeRearWheelSpeed(data: ByteArray): Int? {
        if (data.size < 4) return null
        return parseBigEndianUInt16(data[2], data[3])
    }

    /**
     * Dekoduje kąt pochylenia (tilt)
     */
    fun decodeTilt(data: ByteArray): Int? {
        if (data.size < 8) return null
        val tiltValue = ((data[5].toInt() and 0xFF) shl 4) or hiNibble(data[6])
        return signed12(tiltValue)
    }

    /**
     * Dekoduje kąt przechyłu (lean)
     */
    fun decodeLean(data: ByteArray): Int? {
        if (data.size < 8) return null
        val leanValue = (loNibble(data[6]) shl 8) or (data[7].toInt() and 0xFF)
        return signed12(leanValue)
    }

    /**
     * Konwertuje 12-bitową wartość unsigned na signed (two's complement)
     */
    private fun signed12(value: Int): Int {
        return if ((value and 0x800) != 0) {
            value or 0xFFFFF000.toInt()
        } else {
            value
        }
    }
}