# KTM CAN Decoder

Projekt do dekodowania wiadomości CAN bus z motocykli KTM 790 Duke. Umożliwia ekstrakcję danych telemetrycznych takich jak obroty silnika, prędkości kół, temperatura, światła i inne parametry pojazdu.

## Obsługiwane CAN ID

Projekt obsługuje następujące identyfikatory CAN:

- **0x120**: Throttle/Mode + RPM (obroty silnika, tryb pracy)
- **0x129**: Gear/Clutch (bieg, sprzęgło)
- **0x12A**: Throttle State (stan przepustnicy)
- **0x12B**: Wheel Speed/Lean (prędkości kół, przechylenie)
- **0x290**: Brakes (hamulce)
- **0x450**: Traction Control (kontrola trakcji)
- **0x540**: Sensors (czujniki: temperatura, napięcie, itp.)
- **0x550**: Kill Switch (wyłącznik bezpieczeństwa)
- **0x552**: Fuel Level (poziom paliwa)
- **0x650**: Lights/LED Status (światła, LED)

## Struktura projektu

```
src/main/kotlin/pl/roque/ktm/can/
├── Ktm790DukeDecoder.kt          # Główny dekoder delegujący zadania
├── decoders/                     # Specjalizowane dekodery dla CAN ID
│   ├── BrakeDecoder.kt
│   ├── FuelLevelDecoder.kt
│   ├── GearClutchDecoder.kt
│   ├── KillSwitchDecoder.kt
│   ├── LightsDecoder.kt
│   ├── SensorDecoder.kt
│   ├── ThrottleModeDecoder.kt
│   ├── ThrottleStateDecoder.kt
│   ├── TractionControlDecoder.kt
│   └── WheelSpeedDecoder.kt
├── models/                       # Klasy danych
│   ├── BrakeData.kt
│   ├── EngineData.kt
│   ├── GearData.kt
│   ├── LightsData.kt
│   ├── SensorData.kt
│   ├── ThrottleData.kt
│   ├── TractionData.kt
│   └── WheelSpeedData.kt
└── utils/                        # Narzędzia pomocnicze
    └── BitOperations.kt
```

## Użycie

### Podstawowe dekodowanie

```kotlin
import pl.roque.ktm.can.Ktm790DukeDecoder

val decoder = Ktm790DukeDecoder()

// Dekodowanie wiadomości CAN
val canId = 0x120
val data = byteArrayOf(0x12, 0x34, 0x56, 0x78, 0x9A, 0xBC, 0xDE, 0xF0)
val decodedData = decoder.decode(canId, data)

// Formatowanie do stringa
val formatted = decoder.decodeToString(canId, data)
println(formatted)
```

### Dekodowanie specyficznych danych

```kotlin
// RPM i tryb pracy
val engineData = decoder.decodeEngineData(data)

// Światła
val lightsData = decoder.decodeLightsData(data)

// Prędkości kół
val wheelData = decoder.decodeWheelSpeedData(data)
```

## Budowanie

Projekt używa Gradle. Do zbudowania:

```bash
./gradlew build
```

Do uruchomienia testów:

```bash
./gradlew test
```

## Zależności

- Kotlin 1.9+
- Gradle 8.0+

## Kompatybilność

Projekt jest kompatybilny z:
- Motocyklami KTM 790 Duke
- Standardem CAN bus
- JVM 11+

## Przykład wyjścia

```
CAN ID 0x120: RPM=3250, Mode=SPORT, Throttle=45%
CAN ID 0x129: Gear=3, Clutch=OFF
CAN ID 0x650: Low Beam=ON, High Beam=OFF, DRL=ON
```

## Autor

Damian Podoba - https://github.com/roq3

## Licencja

MIT