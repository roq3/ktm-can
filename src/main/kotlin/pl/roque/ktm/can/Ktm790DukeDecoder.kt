package pl.roque.ktm.can

import pl.roque.ktm.can.decoders.*
import pl.roque.ktm.can.models.*

/**
 * Główny dekoder CAN dla KTM 790 Duke
 * Wersja: 2026-01-17 (refaktoryzacja)
 * Autor: Damian Podoba
 * Github: https://github.com/roq3
 *
 * Refaktoryzacja: Podzielony na podrzędne dekodery dla każdego CAN ID
 * Główny dekoder deleguje zadania do specjalizowanych dekoderów
 */
class Ktm790DukeDecoder(
    private val enableAssertions: Boolean = false
) {
    // Podrzędne dekodery
    private val throttleModeDecoder = ThrottleModeDecoder()
    private val gearClutchDecoder = GearClutchDecoder()
    private val throttleStateDecoder = ThrottleStateDecoder()
    private val wheelSpeedDecoder = WheelSpeedDecoder()
    private val brakeDecoder = BrakeDecoder()
    private val tractionControlDecoder = TractionControlDecoder()
    private val sensorDecoder = SensorDecoder()
    private val fuelLevelDecoder = FuelLevelDecoder()
    private val killSwitchDecoder = KillSwitchDecoder()
    private val lightsDecoder = LightsDecoder()

    companion object {
        const val CAN_ID_THROTTLE_MODE = 0x120   // Przepustnica/tryb + RPM (790 Duke)
        const val CAN_ID_GEAR_CLUTCH = 0x129     // Bieg i sprzęgło
        const val CAN_ID_THROTTLE_STATE = 0x12A  // Stan przepustnicy, żądana mapa
        const val CAN_ID_WHEEL_SPEED = 0x12B     // Prędkość kół, lean/tilt
        const val CAN_ID_BRAKES = 0x290          // Hamulce
        const val CAN_ID_TC_BUTTON = 0x450       // Przycisk traction control
        const val CAN_ID_SENSOR = 0x540          // Temperatura, czujniki
        const val CAN_ID_KILL_SWITCH = 0x550     // Kill Switch
        const val CAN_ID_FUEL_LEVEL = 0x552      // Poziom paliwa
        const val CAN_ID_LIGHTS = 0x650          // Stan świateł LED
    }

    /**
     * Sprawdza czy dany CAN ID jest obsługiwany
     */
    fun isKnownCanId(canId: Int): Boolean {
        return canId in listOf(
            CAN_ID_THROTTLE_MODE,
            CAN_ID_GEAR_CLUTCH,
            CAN_ID_THROTTLE_STATE,
            CAN_ID_WHEEL_SPEED,
            CAN_ID_BRAKES,
            CAN_ID_TC_BUTTON,
            CAN_ID_SENSOR,
            CAN_ID_KILL_SWITCH,
            CAN_ID_FUEL_LEVEL,
            CAN_ID_LIGHTS
        )
    }

    /**
     * Zwraca nazwę CAN ID
     */
    fun getCanIdName(canId: Int): String {
        return when (canId) {
            CAN_ID_THROTTLE_MODE -> "Throttle/Mode"
            CAN_ID_GEAR_CLUTCH -> "Gear/Clutch"
            CAN_ID_THROTTLE_STATE -> "Throttle State"
            CAN_ID_WHEEL_SPEED -> "Wheel/Lean"
            CAN_ID_BRAKES -> "Brakes"
            CAN_ID_TC_BUTTON -> "TC Button"
            CAN_ID_SENSOR -> "Sensor"
            CAN_ID_KILL_SWITCH -> "Kill Switch"
            CAN_ID_FUEL_LEVEL -> "Fuel Level"
            CAN_ID_LIGHTS -> "Lights/LED Status"
            else -> "Unknown (0x${canId.toString(16).uppercase()})"
        }
    }

    /**
     * Dekoduje wiadomość CAN do mapy klucz-wartość
     */
    fun decode(canId: Int, data: ByteArray): Map<String, Any> {
        require(data.size == 8) { "Wiadomość CAN musi zawierać dokładnie 8 bajtów, otrzymano: ${data.size}" }

        return when (canId) {
            CAN_ID_THROTTLE_MODE -> decodeThrottleMode(data)
            CAN_ID_GEAR_CLUTCH -> decodeGearClutch(data)
            CAN_ID_THROTTLE_STATE -> decodeThrottleState(data)
            CAN_ID_WHEEL_SPEED -> decodeWheelSpeed(data)
            CAN_ID_BRAKES -> decodeBrakes(data)
            CAN_ID_TC_BUTTON -> decodeTractionControl(data)
            CAN_ID_SENSOR -> decodeSensor(data)
            CAN_ID_KILL_SWITCH -> decodeKillSwitch(data)
            CAN_ID_FUEL_LEVEL -> decodeFuelLevel(data)
            CAN_ID_LIGHTS -> decodeLights(data)
            else -> mapOf("unmapped" to data.joinToString(" ") { "%02X".format(it) })
        }
    }

    /**
     * Dekoduje wiadomość CAN do czytelnego stringa
     */
    fun decodeToString(canId: Int, data: ByteArray): String {
        return when (canId) {
            CAN_ID_THROTTLE_MODE -> decodeThrottleModeToString(data)
            CAN_ID_GEAR_CLUTCH -> decodeGearClutchToString(data)
            CAN_ID_THROTTLE_STATE -> decodeThrottleStateToString(data)
            CAN_ID_WHEEL_SPEED -> decodeWheelSpeedToString(data)
            CAN_ID_BRAKES -> decodeBrakesToString(data)
            CAN_ID_TC_BUTTON -> decodeTractionControlToString(data)
            CAN_ID_SENSOR -> decodeSensorToString(data)
            CAN_ID_KILL_SWITCH -> decodeKillSwitchToString(data)
            CAN_ID_FUEL_LEVEL -> decodeFuelLevelToString(data)
            CAN_ID_LIGHTS -> decodeLightsToString(data)
            else -> getCanIdName(canId)
        }
    }

    // ========== Metody delegujące do podrzędnych dekoderów ==========

    private fun decodeThrottleMode(data: ByteArray): Map<String, Any> {
        val engineData = throttleModeDecoder.decode(data) ?: return emptyMap()
        return mapOf(
            "rpm" to engineData.rpm,
            "throttle" to engineData.throttle,
            "kill_switch" to engineData.killSwitch,
            "throttle_map" to engineData.throttleMap
        )
    }

    private fun decodeGearClutch(data: ByteArray): Map<String, Any> {
        val gearData = gearClutchDecoder.decode(data) ?: return emptyMap()
        return mapOf(
            "gear" to gearData.gear,
            "clutch_in" to gearData.clutchIn
        )
    }

    private fun decodeThrottleState(data: ByteArray): Map<String, Any> {
        val throttleData = throttleStateDecoder.decode(data) ?: return emptyMap()
        return mapOf(
            "throttle_open" to throttleData.throttleOpen,
            "requested_throttle_map" to throttleData.requestedThrottleMap,
            "ride_mode" to throttleData.rideMode
        )
    }

    private fun decodeWheelSpeed(data: ByteArray): Map<String, Any> {
        val wheelData = wheelSpeedDecoder.decode(data) ?: return emptyMap()
        return mapOf(
            "front_wheel" to wheelData.frontWheel,
            "rear_wheel" to wheelData.rearWheel,
            "tilt" to wheelData.tilt,
            "lean" to wheelData.lean
        )
    }

    private fun decodeBrakes(data: ByteArray): Map<String, Any> {
        val brakeData = brakeDecoder.decode(data) ?: return emptyMap()
        return mapOf(
            "front_brake" to brakeData.frontBrake,
            "rear_brake" to brakeData.rearBrake
        )
    }

    private fun decodeTractionControl(data: ByteArray): Map<String, Any> {
        val tractionData = tractionControlDecoder.decode(data) ?: return emptyMap()
        return mapOf(
            "traction_control_button" to tractionData.tractionControlButton
        )
    }

    private fun decodeSensor(data: ByteArray): Map<String, Any> {
        val sensorData = sensorDecoder.decode(data) ?: return emptyMap()
        return mapOf(
            "rpm" to sensorData.rpm,
            "gear" to sensorData.gear,
            "kickstand_up" to sensorData.kickstandUp,
            "kickstand_err" to sensorData.kickstandErr,
            "coolant_temp" to sensorData.coolantTemp
        )
    }

    private fun decodeKillSwitch(data: ByteArray): Map<String, Any> {
        val isRunning = killSwitchDecoder.decodeKillSwitch(data) ?: return emptyMap()
        return mapOf(
            "kill_switch_running" to isRunning
        )
    }

    private fun decodeFuelLevel(data: ByteArray): Map<String, Any> {
        val fuelLevel = fuelLevelDecoder.decodeFuelLevel(data) ?: return emptyMap()
        return mapOf(
            "fuel_level_percent" to fuelLevel
        )
    }

    private fun decodeLights(data: ByteArray): Map<String, Any> {
        val lightsData = lightsDecoder.decode(data) ?: return emptyMap()
        return mapOf(
            "drl_active" to lightsData.drlActive,
            "high_beam_active" to lightsData.highBeamActive,
            "low_beam_active" to lightsData.lowBeamActive,
            "high_beam_broken" to lightsData.highBeamBroken,
            "low_beam_broken" to lightsData.lowBeamBroken
        )
    }

    // ========== Metody toString delegujące ==========

    private fun decodeThrottleModeToString(data: ByteArray): String {
        val throttle = throttleModeDecoder.decodeThrottlePosition(data)
        val rpm = throttleModeDecoder.decodeRpm(data)

        return buildString {
            rpm?.let {
                if (it > 0) append("🔥 ${it} RPM ") else append("⏸️ IGN ON ")
            } ?: append("RPM:? ")

            throttle?.let {
                when {
                    it >= 95 -> append("🏎️ FULL GAS!")
                    it > 0 -> append("⛽ ${it}%")
                    else -> append("⛽ Closed")
                }
            } ?: append("Throttle:?")
        }
    }

    private fun decodeGearClutchToString(data: ByteArray): String {
        val gear = gearClutchDecoder.decodeGear(data)
        val clutch = gearClutchDecoder.decodeClutch(data)

        return buildString {
            gear?.let {
                when (it) {
                    0 -> append("⚫ N")
                    in 1..6 -> append("${it}️⃣")
                    else -> append("?")
                }
            } ?: append("?")

            if (clutch == true) append(" 🔘CLUTCH")
        }
    }

    private fun decodeThrottleStateToString(data: ByteArray): String {
        val throttleData = throttleStateDecoder.decode(data)
        return throttleData?.let {
            "🎛️ Throttle: ${if (it.throttleOpen) "OPEN" else "CLOSED"} Map:${it.requestedThrottleMap} Mode:${it.rideMode}"
        } ?: "Throttle State: ?"
    }

    private fun decodeWheelSpeedToString(data: ByteArray): String {
        val front = wheelSpeedDecoder.decodeFrontWheelSpeed(data)
        val rear = wheelSpeedDecoder.decodeRearWheelSpeed(data)
        val lean = wheelSpeedDecoder.decodeLean(data)
        val tilt = wheelSpeedDecoder.decodeTilt(data)

        return buildString {
            append("🏍️ ")
            if ((front ?: 0) > 0 || (rear ?: 0) > 0) {
                append("F:${front ?: "?"} R:${rear ?: "?"} ")
            }
            lean?.let { if (it != 0) append("Lean:${it}° ") }
            tilt?.let { if (it != 0) append("Tilt:${it}°") }
        }.ifEmpty { "Wheel: stopped" }
    }

    private fun decodeBrakesToString(data: ByteArray): String {
        val front = brakeDecoder.decodeFrontBrake(data)
        val rear = brakeDecoder.decodeRearBrake(data)

        return buildString {
            if ((front ?: 0) > 0) append("🛑FRONT ")
            if ((rear ?: 0) > 0) append("🛑REAR ")
            if ((front ?: 0) == 0 && (rear ?: 0) == 0) append("Released")
        }.ifEmpty { "Brakes: ?" }
    }

    private fun decodeTractionControlToString(data: ByteArray): String {
        val button = tractionControlDecoder.decodeTractionControlButton(data)
        return "🎛️ TC Button: ${if (button == 1) "PRESSED" else "RELEASED"}"
    }

    private fun decodeSensorToString(data: ByteArray): String {
        val sensorData = sensorDecoder.decode(data)
        return sensorData?.let {
            "🌡️ RPM:${it.rpm} Gear:${it.gear} Temp:${it.coolantTemp}°C Kickstand:${if (it.kickstandUp) "UP" else "DOWN"}"
        } ?: "Sensor: ?"
    }

    private fun decodeKillSwitchToString(data: ByteArray): String {
        val isRunning = killSwitchDecoder.decodeKillSwitch(data)
        return "🔴 Kill: ${if (isRunning == true) "RUN" else "STOP"}"
    }

    private fun decodeFuelLevelToString(data: ByteArray): String {
        val fuel = fuelLevelDecoder.decodeFuelLevel(data)
        return fuel?.let { "⛽ ${it}% full" } ?: "⛽ ?"
    }

    private fun decodeLightsToString(data: ByteArray): String {
        val lights = lightsDecoder.decode(data)
        return lights?.let {
            buildString {
                append("💡")
                if (it.drlActive) append(" DRL")
                if (it.lowBeamActive) append(" LOW BEAM")
                if (it.highBeamActive) append(" HIGH BEAM")
                if (it.highBeamBroken) append(" HIGH BEAM ❌")
                if (it.lowBeamBroken) append(" LOW BEAM ❌")
                if (!it.drlActive && !it.lowBeamActive && !it.highBeamActive &&
                    !it.highBeamBroken && !it.lowBeamBroken) {
                    append(" OFF")
                }
            }
        } ?: "💡 ?"
    }

    // ========== Metody kompatybilności z oryginalnym API ==========

    fun decodeEngineData(canId: Int, data: ByteArray): EngineData? {
        if (canId != CAN_ID_THROTTLE_MODE) return null
        return throttleModeDecoder.decode(data)
    }

    fun decodeGearData(canId: Int, data: ByteArray): GearData? {
        if (canId != CAN_ID_GEAR_CLUTCH) return null
        return gearClutchDecoder.decode(data)
    }

    fun decodeThrottleData(canId: Int, data: ByteArray): ThrottleData? {
        if (canId != CAN_ID_THROTTLE_STATE) return null
        return throttleStateDecoder.decode(data)
    }

    fun decodeWheelSpeedData(canId: Int, data: ByteArray): WheelSpeedData? {
        if (canId != CAN_ID_WHEEL_SPEED) return null
        return wheelSpeedDecoder.decode(data)
    }

    fun decodeBrakeData(canId: Int, data: ByteArray): BrakeData? {
        if (canId != CAN_ID_BRAKES) return null
        return brakeDecoder.decode(data)
    }

    fun decodeTractionData(canId: Int, data: ByteArray): TractionData? {
        if (canId != CAN_ID_TC_BUTTON) return null
        return tractionControlDecoder.decode(data)
    }

    fun decodeSensorData(canId: Int, data: ByteArray): SensorData? {
        if (canId != CAN_ID_SENSOR) return null
        return sensorDecoder.decode(data)
    }

    fun decodeLightsData(canId: Int, data: ByteArray): LightsData? {
        if (canId != CAN_ID_LIGHTS) return null
        return lightsDecoder.decode(data)
    }
}