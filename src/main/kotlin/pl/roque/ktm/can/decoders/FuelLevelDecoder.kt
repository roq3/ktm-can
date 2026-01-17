package pl.roque.ktm.can.decoders

/**
 * Dekoder dla CAN ID 0x552: Fuel Level
 * Częstotliwość: nieznana
 */
class FuelLevelDecoder {

    /**
     * Dekoduje poziom paliwa z CAN ID 0x552
     * Zwraca procent paliwa (100 = pełny, 0 = pusty)
     */
    fun decodeFuelLevel(data: ByteArray): Int? {
        if (data.size < 1) return null

        val d0 = data[0].toInt() and 0xFF
        val fuelPercent = ((256 - d0) / 255.0 * 100).toInt()

        // Zweryfikuj realistyczny zakres (0-100%)
        return maxOf(0, minOf(100, fuelPercent))
    }

    /**
     * Dekoduje surową wartość poziomu paliwa
     */
    fun decodeFuelLevelRaw(data: ByteArray): Int? {
        if (data.size < 1) return null
        return data[0].toInt() and 0xFF
    }
}