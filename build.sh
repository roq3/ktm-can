#!/bin/bash

# Skrypt do budowania projektu z poprawną wersją Java

# Najpierw spróbuj przeczytać z local.properties (plik lokalny, nie commitowany)
if [ -f "local.properties" ]; then
    JAVA_HOME_FROM_FILE=$(grep "^java.home=" local.properties | cut -d'=' -f2-)
    if [ -n "$JAVA_HOME_FROM_FILE" ]; then
        export JAVA_HOME="$JAVA_HOME_FROM_FILE"
        export PATH=$JAVA_HOME/bin:$PATH
        echo "🔧 Używam Java z local.properties: $JAVA_HOME"
    else
        # Fallback na domyślną ścieżkę
        export JAVA_HOME=/Users/roque/Library/Java/JavaVirtualMachines/corretto-17.0.13/Contents/Home
        export PATH=$JAVA_HOME/bin:$PATH
        echo "🔧 Brak java.home w local.properties, używam domyślnej Java: $JAVA_HOME"
    fi
else
    # Brak pliku local.properties, użyj domyślnej ścieżki
    export JAVA_HOME=/Users/roque/Library/Java/JavaVirtualMachines/corretto-17.0.13/Contents/Home
    export PATH=$JAVA_HOME/bin:$PATH
    echo "🔧 Brak local.properties, używam domyślnej Java: $JAVA_HOME"
fi

echo ""

./gradlew clean build --console=plain

EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    echo "✅ Build zakończony sukcesem!"
    echo ""
    echo "📦 Artefakty:"
    echo "   build/libs/ktm-can-1.0.0.jar"
    echo ""
    echo "📊 Raport testów:"
    echo "   build/reports/tests/test/index.html"
else
    echo ""
    echo "❌ Build nie powiódł się (kod: $EXIT_CODE)"
fi

exit $EXIT_CODE
