package pl.roque.ktm.can.models

/**
 * Dane skrzyni biegów z CAN ID 0x129
 * Częstotliwość: 20ms
 * 
 * @property gear Aktualny bieg (0=neutral, 1-6=biegi)
 * @property clutchIn Stan sprzęgła (true=wciśnięte, false=puszczone)
 */
data class GearData(
    val gear: Int,
    val clutchIn: Boolean
) {
    /**
     * Sprawdza czy motocykl jest na neutraku
     */
    fun isNeutral(): Boolean = gear == 0
    
    /**
     * Zwraca czytelną nazwę biegu
     */
    fun gearName(): String = when (gear) {
        0 -> "N"
        else -> gear.toString()
    }
}
