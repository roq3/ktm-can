package pl.roque.ktm.can.decoders

import pl.roque.ktm.can.models.GearData
import pl.roque.ktm.can.utils.BitOperations.hiNibble

/**
 * Dekoder dla CAN ID 0x129: Gear/Clutch
 * Częstotliwość: 20ms
 */
class GearClutchDecoder {

    /**
     * Dekoduje dane z CAN ID 0x129
     */
    fun decode(data: ByteArray): GearData? {
        if (data.size < 1) return null

        val gear = hiNibble(data[0])
        val clutchIn = ((data[0].toInt() and 0b00001000) shr 3) == 1

        return GearData(
            gear = gear,
            clutchIn = clutchIn
        )
    }

    /**
     * Dekoduje pozycję biegu
     */
    fun decodeGear(data: ByteArray): Int? {
        if (data.size < 1) return null
        return hiNibble(data[0])
    }

    /**
     * Dekoduje stan sprzęgła
     */
    fun decodeClutch(data: ByteArray): Boolean? {
        if (data.size < 1) return null
        return ((data[0].toInt() and 0b00001000) shr 3) == 1
    }
}