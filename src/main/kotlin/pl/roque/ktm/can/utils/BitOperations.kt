package pl.roque.ktm.can.utils

/**
 * Pomocnicze operacje bitowe do parsowania wiadomości CAN
 */
object BitOperations {
    
    /**
     * Wyciąga niższy nibble (4 bity) z bajtu
     * @param b Bajt wejściowy
     * @return Wartość 0-15
     */
    fun loNibble(b: Byte): Int = b.toInt() and 0x0F
    
    /**
     * Wyciąga wyższy nibble (4 bity) z bajtu
     * @param b Bajt wejściowy
     * @return Wartość 0-15
     */
    fun hiNibble(b: Byte): Int = (b.toInt() shr 4) and 0x0F
    
    /**
     * Konwertuje 12-bitową wartość do signed integer (two's complement)
     * Używane do kątów przechyłu/pochylenia
     * @param value 12-bitowa wartość (0-4095)
     * @return Signed integer (-2048 do 2047)
     */
    fun signed12(value: Int): Int {
        return if (value and 0b100000000000 != 0) {
            -(value and 0b100000000000) or (value and 0b011111111111)
        } else {
            value
        }
    }
    
    /**
     * Inwersja bitowa (NOT operation)
     * @param value Wartość 0-255
     * @return Wartość zanegowana (0-255)
     */
    fun invert(value: Int): Int = value.inv() and 0xFF
    
    /**
     * Parsuje 16-bitową wartość big-endian z dwóch bajtów
     * @param high Bajt wysokiego rzędu
     * @param low Bajt niskiego rzędu
     * @return Wartość 0-65535
     */
    fun parseBigEndianUInt16(high: Byte, low: Byte): Int {
        return ((high.toInt() and 0xFF) shl 8) or (low.toInt() and 0xFF)
    }
    
    /**
     * Parsuje 16-bitową SIGNED wartość big-endian z dwóch bajtów
     * @param high Bajt wysokiego rzędu
     * @param low Bajt niskiego rzędu
     * @return Wartość -32768 do 32767
     */
    fun parseBigEndianSInt16(high: Byte, low: Byte): Int {
        val unsigned = ((high.toInt() and 0xFF) shl 8) or (low.toInt() and 0xFF)
        return if (unsigned > 32767) unsigned - 65536 else unsigned
    }
}
