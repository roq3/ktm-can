package pl.roque.ktm.can.decoders

/**
 * Dekoder dla CAN ID 0x550: Kill Switch
 * Częstotliwość: nieznana
 */
class KillSwitchDecoder {

    /**
     * Dekoduje stan kill switch z CAN ID 0x550
     * Zwraca true jeśli silnik jest uruchomiony (RUN), false jeśli zatrzymany (STOP)
     */
    fun decodeKillSwitch(data: ByteArray): Boolean? {
        if (data.size < 1) return null
        val killRaw = data[0].toInt() and 0xFF
        return (killRaw and 0x10) != 0
    }

    /**
     * Dekoduje surową wartość kill switch
     */
    fun decodeKillSwitchRaw(data: ByteArray): Int? {
        if (data.size < 1) return null
        return data[0].toInt() and 0xFF
    }
}