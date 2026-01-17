package pl.roque.ktm.can.models

/**
 * Dane przepustnicy z CAN ID 0x12A
 * Częstotliwość: 50ms
 * Autor: Damian Podoba, github.com/roq3
 * 
 * @property throttleOpen Stan przepustnicy (true=otwarta, false=zamknięta)
 * @property requestedThrottleMap Żądana mapa gazu (0=Mode 1, 1=Mode 2)
 * @property rideMode Nazwa trybu jazdy (Street, Rain, Sport, Track)
 */
data class ThrottleData(
    val throttleOpen: Boolean,
    val requestedThrottleMap: Int,
    val rideMode: String = "Unknown"
) {
    /**
     * Sprawdza czy żądana mapa to Mode 1
     */
    fun isMode1(): Boolean = requestedThrottleMap == 0
    
    /**
     * Sprawdza czy żądana mapa to Mode 2
     */
    fun isMode2(): Boolean = requestedThrottleMap == 1
}
