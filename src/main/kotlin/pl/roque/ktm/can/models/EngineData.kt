package pl.roque.ktm.can.models

/**
 * Dane silnika z CAN ID 0x120
 * Częstotliwość: 20ms
 * Autor: Damian Podoba, github.com/roq3
 * 
 * @property rpm Obroty silnika (obr/min)
 * @property throttle Pozycja gazu (0-255, 0-100%)
 * @property killSwitch Stan wyłącznika kill (0=STOP, 1=RUN)
 * @property throttleMap Aktywna mapa gazu (0=Mode 1, 1=Mode 2)
 */
data class EngineData(
    val rpm: Int,
    val throttle: Int,
    val killSwitch: Int,
    val throttleMap: Int
) {
    /**
     * Zwraca throttle jako procent (0-100)
     */
    fun throttlePercent(): Int = (throttle * 100) / 255
    
    /**
     * Sprawdza czy kill switch jest w pozycji RUN
     */
    fun isKillSwitchOn(): Boolean = killSwitch == 1
}
