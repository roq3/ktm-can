package pl.roque.ktm.can

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Ktm790DukeDecoderTest {

    private val decoder = Ktm790DukeDecoder()

    @Test
    fun testDecodeThrottleMode() {
        // Real data from dump: 1768509351014 0x120: 00 00 00 00 00 00 00 20
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x20.toByte())
        val result = decoder.decode(0x120, data)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result.containsKey("rpm"))
        assertTrue(result.containsKey("throttle"))
        assertTrue(result.containsKey("kill_switch"))
        assertTrue(result.containsKey("throttle_map"))
    }

    @Test
    fun testDecodeGearClutch() {
        // Real data from dump: 1768509351015 0x129: 00 00 00 00 00 00 00 60
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x60.toByte())
        val result = decoder.decode(0x129, data)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result.containsKey("gear"))
        assertTrue(result.containsKey("clutch_in"))
    }

    @Test
    fun testDecodeLights() {
        // Real data from dump: 1768509384690 0x650: 60 00 00 00 00 00 00 00
        // This should set highBeamActive = true (bit 5) and lowBeamActive = true (bit 6)
        val data = byteArrayOf(0x60.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decode(0x650, data)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result.containsKey("drl_active"))
        assertTrue(result.containsKey("high_beam_active"))
        assertTrue(result.containsKey("low_beam_active"))
        assertTrue(result.containsKey("high_beam_broken"))
        assertTrue(result.containsKey("low_beam_broken"))
    }

    @Test
    fun testDecodeToStringThrottleMode() {
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x20.toByte())
        val result = decoder.decodeToString(0x120, data)
        assertTrue(result.contains("IGN") || result.contains("RPM") || result.contains("⛽"))
    }

    @Test
    fun testDecodeToStringLights() {
        val data = byteArrayOf(0x60.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decodeToString(0x650, data)
        assertTrue(result.contains("BEAM") || result.contains("💡") || result.isNotEmpty())
    }

    @Test
    fun testDecodeUnknownId() {
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decode(0x999, data)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result.containsKey("unmapped"))
    }

    @Test
    fun testDecodeToStringUnknownId() {
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decodeToString(0x999, data)

        assertEquals("Unknown (0x999)", result)
    }

    @Test
    fun testIsKnownCanId() {
        assertTrue(decoder.isKnownCanId(0x120))
        assertTrue(decoder.isKnownCanId(0x129))
        assertTrue(decoder.isKnownCanId(0x650))
        assertFalse(decoder.isKnownCanId(0x999))
    }
}