package pl.roque.ktm.can.decoders

import pl.roque.ktm.can.models.EngineData
import pl.roque.ktm.can.utils.BitOperations.parseBigEndianUInt16

/**
 * Dekoder dla CAN ID 0x120: Throttle/Mode + RPM
 * Częstotliwość: 20ms
 */
class ThrottleModeDecoder {

    /**
     * Dekoduje dane z CAN ID 0x120
     */
    fun decode(data: ByteArray): EngineData? {
        if (data.size < 5) return null

        val rpm = parseBigEndianUInt16(data[0], data[1])
        val throttle = data[2].toInt() and 0xFF
        val killSwitchRaw = (data[3].toInt() and 0b00010000) shr 4
        val killSwitch = if (killSwitchRaw == 0) 1 else 0
        val throttleMap = data[4].toInt() and 0b00000001

        return EngineData(
            rpm = rpm,
            throttle = throttle,
            killSwitch = killSwitch,
            throttleMap = throttleMap
        )
    }

    /**
     * Dekoduje pozycję przepustnicy
     */
    fun decodeThrottlePosition(data: ByteArray): Int? {
        if (data.size < 3) return null
        return data[2].toInt() and 0xFF
    }

    /**
     * Dekoduje RPM
     */
    fun decodeRpm(data: ByteArray): Int? {
        if (data.size < 2) return null
        return parseBigEndianUInt16(data[0], data[1])
    }
}