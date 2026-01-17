package pl.roque.ktm.can.decoders

import pl.roque.ktm.can.models.ThrottleData

/**
 * Dekoder dla CAN ID 0x12A: Throttle State
 * Częstotliwość: 50ms
 */
class ThrottleStateDecoder {

    /**
     * Dekoduje dane z CAN ID 0x12A
     */
    fun decode(data: ByteArray): ThrottleData? {
        if (data.size < 4) return null

        val throttleOpen = ((data[0].toInt() and 0b00000010) shr 1) == 0
        val requestedThrottleMap = (data[1].toInt() and 0b01000000) shr 6
        val rideMode = mapRideMode((data[1].toInt() and 0xFF), (data[3].toInt() and 0xFF))

        return ThrottleData(
            throttleOpen = throttleOpen,
            requestedThrottleMap = requestedThrottleMap,
            rideMode = rideMode
        )
    }

    /**
     * Dekoduje czy przepustnica jest otwarta
     */
    fun decodeThrottleOpen(data: ByteArray): Boolean? {
        if (data.size < 1) return null
        return ((data[0].toInt() and 0b00000010) shr 1) == 0
    }

    /**
     * Dekoduje żądaną mapę gazu
     */
    fun decodeRequestedThrottleMap(data: ByteArray): Int? {
        if (data.size < 2) return null
        return (data[1].toInt() and 0b01000000) shr 6
    }

    /**
     * Mapuje tryb jazdy na podstawie bajtów D1 i D3
     */
    private fun mapRideMode(modeByte: Int, statusByte: Int): String {
        return when (modeByte) {
            0xAD -> "Rain"
            0x2D -> "Street"
            0x6D -> "Sport"
            0xED -> "Track"            
            0x89 -> "Unknown"
            0xC9 -> "OFF"

            // 0xC9 / 0x3C modeByte 0x-37 statusByte=0x3C => wylaczony zaplon

            else -> "0x${modeByte.toString(16).uppercase()} / 0x${statusByte.toString(16).uppercase()}"
        }
    }
}