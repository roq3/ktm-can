package pl.roque.ktm.can.models

/**
 * Dane hamulca z CAN ID 0x290
 * Częstotliwość: 10ms
 * Autor: Damian Podoba, github.com/roq3
 * 
 * @property frontBrake Ciśnienie hamulca przedniego (0-65535)
 * @property rearBrake Ciśnienie hamulca tylnego (0-65535)
 */
data class BrakeData(
    val frontBrake: Int,
    val rearBrake: Int
) {
    /**
     * Sprawdza czy przedni hamulec jest wciśnięty
     */
    fun isFrontBraking(): Boolean = frontBrake > 0

    /**
     * Sprawdza czy tylny hamulec jest wciśnięty
     */
    fun isRearBraking(): Boolean = rearBrake > 0

    /**
     * Sprawdza czy jakikolwiek hamulec jest wciśnięty
     */
    fun isBraking(): Boolean = isFrontBraking() || isRearBraking()
    
    /**
     * Zwraca poziom hamowania (0-100%)
     * Zakładając że maksymalne ciśnienie to ~1000
     */
    fun frontBrakingPercent(): Int {
        val maxPressure = 1000
        return ((frontBrake * 100) / maxPressure).coerceIn(0, 100)
    }

    /**
     * Zwraca poziom hamowania (0-100%) - maksymalna wartość z przedniego i tylnego
     */
    fun brakingPercent(): Int = maxOf(frontBrakingPercent(), rearBrakingPercent())

    /**
     * Zwraca poziom hamowania (0-100%)
     * Zakładając że maksymalne ciśnienie to ~1000
     */
    fun rearBrakingPercent(): Int {
        val maxPressure = 1000
        return ((rearBrake * 100) / maxPressure).coerceIn(0, 100)
    }
}
