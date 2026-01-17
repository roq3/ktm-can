package pl.roque.ktm.can.models

/**
 * Dane kontroli trakcji z CAN ID 0x450
 * Częstotliwość: 50ms
 * 
 * @property tractionControlButton Stan przycisku TC (0=nie wciśnięty, 1=wciśnięty)
 */
data class TractionData(
    val tractionControlButton: Int
) {
    /**
     * Sprawdza czy przycisk TC jest wciśnięty
     */
    fun isButtonPressed(): Boolean = tractionControlButton == 1
}
