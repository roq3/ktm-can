package pl.roque.ktm.can.decoders

import pl.roque.ktm.can.models.BrakeData
import pl.roque.ktm.can.utils.BitOperations.parseBigEndianUInt16

/**
 * Dekoder dla CAN ID 0x290: Brakes
 * Częstotliwość: 10ms
 */
class BrakeDecoder {

    /**
     * Dekoduje dane z CAN ID 0x290
     */
    fun decode(data: ByteArray): BrakeData? {
        if (data.size < 4) return null

        val frontBrake = parseBigEndianUInt16(data[0], data[1])
        val rearBrake = parseBigEndianUInt16(data[2], data[3])

        return BrakeData(
            frontBrake = frontBrake,
            rearBrake = rearBrake
        )
    }

    /**
     * Dekoduje ciśnienie hamulca przedniego
     */
    fun decodeFrontBrake(data: ByteArray): Int? {
        if (data.size < 2) return null
        return parseBigEndianUInt16(data[0], data[1])
    }

    /**
     * Dekoduje ciśnienie hamulca tylnego
     */
    fun decodeRearBrake(data: ByteArray): Int? {
        if (data.size < 4) return null
        return parseBigEndianUInt16(data[2], data[3])
    }
}