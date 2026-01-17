package pl.roque.ktm.can.decoders

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pl.roque.ktm.can.models.LightsData

class LightsDecoderTest {

    private val decoder = LightsDecoder()

    @Test
    fun testDecodeAllLightsOn() {
        // Set all relevant bits: bit 3 (DRL), bit 5 (High), bit 6 (Low), bit 26 (High broken), bit 27 (Low broken)
        val data = byteArrayOf(
            0x68.toByte(), // byte 0: 0x08 (bit 3) | 0x20 (bit 5) | 0x40 (bit 6) = 0x68
            0x00.toByte(), // byte 1
            0x00.toByte(), // byte 2
            0x0C.toByte()  // byte 3: 0x04 (bit 26) | 0x08 (bit 27) = 0x0C
        )
        val result = decoder.decode(data)

        assertNotNull(result)
        assertTrue(result is LightsData)
        val lightsData = result as LightsData
        assertTrue(lightsData.drlActive)
        assertTrue(lightsData.highBeamActive)
        assertTrue(lightsData.lowBeamActive)
        assertTrue(lightsData.highBeamBroken)
        assertTrue(lightsData.lowBeamBroken)
    }

    @Test
    fun testDecodeAllLightsOff() {
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decode(data)

        assertNotNull(result)
        assertTrue(result is LightsData)
        val lightsData = result as LightsData
        assertFalse(lightsData.drlActive)
        assertFalse(lightsData.highBeamActive)
        assertFalse(lightsData.lowBeamActive)
        assertFalse(lightsData.highBeamBroken)
        assertFalse(lightsData.lowBeamBroken)
    }

    @Test
    fun testDecodeDrlActiveOnly() {
        // Only bit 3 set (DRL active)
        val data = byteArrayOf(0x08.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decode(data)

        assertNotNull(result)
        assertTrue(result is LightsData)
        val lightsData = result as LightsData
        assertTrue(lightsData.drlActive)
        assertFalse(lightsData.highBeamActive)
        assertFalse(lightsData.lowBeamActive)
        assertFalse(lightsData.highBeamBroken)
        assertFalse(lightsData.lowBeamBroken)
    }

    @Test
    fun testDecodeHighBeamActiveOnly() {
        // Only bit 5 set (High beam active)
        val data = byteArrayOf(0x20.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decode(data)

        assertNotNull(result)
        assertTrue(result is LightsData)
        val lightsData = result as LightsData
        assertFalse(lightsData.drlActive)
        assertTrue(lightsData.highBeamActive)
        assertFalse(lightsData.lowBeamActive)
        assertFalse(lightsData.highBeamBroken)
        assertFalse(lightsData.lowBeamBroken)
    }

    @Test
    fun testDecodeLowBeamActiveOnly() {
        // Only bit 6 set (Low beam active)
        val data = byteArrayOf(0x40.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decode(data)

        assertNotNull(result)
        assertTrue(result is LightsData)
        val lightsData = result as LightsData
        assertFalse(lightsData.drlActive)
        assertFalse(lightsData.highBeamActive)
        assertTrue(lightsData.lowBeamActive)
        assertFalse(lightsData.highBeamBroken)
        assertFalse(lightsData.lowBeamBroken)
    }

    @Test
    fun testDecodeHighBeamBrokenOnly() {
        // Only bit 26 set (High beam broken) - bit 26 is in byte 3, position 2
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x04.toByte())
        val result = decoder.decode(data)

        assertNotNull(result)
        assertTrue(result is LightsData)
        val lightsData = result as LightsData
        assertFalse(lightsData.drlActive)
        assertFalse(lightsData.highBeamActive)
        assertFalse(lightsData.lowBeamActive)
        assertTrue(lightsData.highBeamBroken)
        assertFalse(lightsData.lowBeamBroken)
    }

    @Test
    fun testDecodeLowBeamBrokenOnly() {
        // Only bit 27 set (Low beam broken) - bit 27 is in byte 3, position 3
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x08.toByte())
        val result = decoder.decode(data)

        assertNotNull(result)
        assertTrue(result is LightsData)
        val lightsData = result as LightsData
        assertFalse(lightsData.drlActive)
        assertFalse(lightsData.highBeamActive)
        assertFalse(lightsData.lowBeamActive)
        assertFalse(lightsData.highBeamBroken)
        assertTrue(lightsData.lowBeamBroken)
    }

    @Test
    fun testDecodePartialData() {
        val data = byteArrayOf(0x0F.toByte(), 0xF0.toByte())
        val result = decoder.decode(data)

        assertNull(result) // Should return null for insufficient data
    }

    @Test
    fun testDecodeDrlActive() {
        // Test the specific DRL active decoding method
        val data = byteArrayOf(0x08.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decodeDrlActive(data)

        assertNotNull(result)
        assertTrue(result!!)
    }

    @Test
    fun testDecodeDrlInactive() {
        val data = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
        val result = decoder.decodeDrlActive(data)

        assertNotNull(result)
        assertFalse(result!!)
    }
}