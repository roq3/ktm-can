package pl.roque.ktm.can.models

/**
 * Dane czujników z CAN ID 0x540
 * Częstotliwość: 100ms
 * Autor: Damian Podoba, github.com/roq3
 * 
 * @property rpm Obroty silnika (obr/min) - wolniejsza aktualizacja niż 0x120
 * @property gear Aktualny bieg (0=neutral, 1-6=biegi, 7=nieznany/quickshifter?)
 * @property kickstandUp Stan nóżki bocznej (true=podniesiona, false=opuszczona)
 * @property kickstandErr Błąd czujnika nóżki (true=błąd)
 * @property coolantTemp Temperatura płynu chłodzącego (°C)
 */
data class SensorData(
    val rpm: Int,
    val gear: Int,
    val kickstandUp: Boolean,
    val kickstandErr: Boolean,
    val coolantTemp: Double
) {
    /**
     * Sprawdza czy nóżka jest bezpiecznie podniesiona
     */
    fun isKickstandSafe(): Boolean = kickstandUp && !kickstandErr
    
    /**
     * Sprawdza czy temperatura jest w normalnym zakresie (60-100°C)
     */
    fun isCoolantTempNormal(): Boolean = coolantTemp in 60.0..100.0
    
    /**
     * Sprawdza czy silnik się przegrzewa (>100°C)
     */
    fun isOverheating(): Boolean = coolantTemp > 100.0
    
    /**
     * Zwraca czytelną nazwę biegu
     */
    fun gearName(): String = when (gear) {
        0 -> "N"
        7 -> "?"
        else -> gear.toString()
    }
}
