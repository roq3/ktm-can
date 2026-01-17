package pl.roque.ktm.can.decoders

import pl.roque.ktm.can.models.SensorData
import pl.roque.ktm.can.utils.BitOperations.parseBigEndianUInt16
import pl.roque.ktm.can.utils.BitOperations.parseBigEndianSInt16
import pl.roque.ktm.can.utils.BitOperations.loNibble

/**
 * Dekoder dla CAN ID 0x540: Sensors
 * Częstotliwość: 100ms
 */
class SensorDecoder {

    /**
     * Dekoduje dane z CAN ID 0x540
     */
    fun decode(data: ByteArray): SensorData? {
        if (data.size < 8) return null

        val rpm = parseBigEndianUInt16(data[1], data[2])
        val gear = loNibble(data[3])
        val kickstandUp = ((data[1].toInt() and 0b10000000) shr 7) == 0
        val kickstandErr = ((data[4].toInt() and 0b10000000) shr 7) == 1
        val coolantTemp = parseBigEndianSInt16(data[6], data[7]) / 10.0

        return SensorData(
            rpm = rpm,
            gear = gear,
            kickstandUp = kickstandUp,
            kickstandErr = kickstandErr,
            coolantTemp = coolantTemp
        )
    }

    /**
     * Dekoduje RPM z 0x540
     */
    fun decodeRpm(data: ByteArray): Int? {
        if (data.size < 3) return null
        return parseBigEndianUInt16(data[1], data[2])
    }

    /**
     * Dekoduje bieg z 0x540
     */
    fun decodeGear(data: ByteArray): Int? {
        if (data.size < 4) return null
        return loNibble(data[3])
    }

    /**
     * Dekoduje stan nóżki
     */
    fun decodeKickstandUp(data: ByteArray): Boolean? {
        if (data.size < 2) return null
        return ((data[1].toInt() and 0b10000000) shr 7) == 0
    }

    /**
     * Dekoduje błąd nóżki
     */
    fun decodeKickstandError(data: ByteArray): Boolean? {
        if (data.size < 5) return null
        return ((data[4].toInt() and 0b10000000) shr 7) == 1
    }

    /**
     * Dekoduje temperaturę chłodziwa
     */
    fun decodeCoolantTemp(data: ByteArray): Double? {
        if (data.size < 8) return null
        return parseBigEndianSInt16(data[6], data[7]) / 10.0
    }
}