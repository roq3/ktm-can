package pl.roque.ktm.can.models

/**
 * Dane prędkości kół i przechyłu z CAN ID 0x12B
 * Częstotliwość: 10ms
 * Autor: Damian Podoba, github.com/roq3
 * 
 * @property frontWheel Prędkość koła przedniego (jednostki CAN)
 * @property rearWheel Prędkość koła tylnego (jednostki CAN)
 * @property tilt Kąt pochylenia (pitch) w jednostkach CAN
 * @property lean Kąt przechyłu (roll) w jednostkach CAN
 * 
 * Uwaga: Kąty są wartościami signed 12-bit:
 * - 0x000 = neutralny
 * - 0x001 = pochylenie w prawo/przód
 * - 0xFFF = pochylenie w lewo/tył
 */
data class WheelSpeedData(
    val frontWheel: Int,
    val rearWheel: Int,
    val tilt: Int,
    val lean: Int
) {
    /**
     * Sprawdza czy motocykl jest przechylony w prawo
     */
    fun isLeaningRight(): Boolean = lean > 0
    
    /**
     * Sprawdza czy motocykl jest przechylony w lewo
     */
    fun isLeaningLeft(): Boolean = lean < 0
    
    /**
     * Sprawdza czy przednie koło się kręci
     */
    fun isFrontWheelMoving(): Boolean = frontWheel > 0
    
    /**
     * Sprawdza czy tylne koło się kręci
     */
    fun isRearWheelMoving(): Boolean = rearWheel > 0
    
    /**
     * Detekcja wheelie (przednie koło wolniejsze niż tylne)
     */
    fun isPossibleWheelie(): Boolean = rearWheel > 0 && frontWheel < (rearWheel * 0.3)

    /**
     * Prędkość koła przedniego w km/h
     */
    fun getFrontWheelSpeedKmh(): Double = frontWheel / 16.0

    /**
     * Prędkość koła tylnego w km/h
     */
    fun getRearWheelSpeedKmh(): Double = rearWheel / 16.0
}
