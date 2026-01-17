#!/bin/bash

# Skrypt do budowania projektu z poprawną wersją Java

export JAVA_HOME=/Users/roque/Library/Java/JavaVirtualMachines/corretto-17.0.13/Contents/Home

echo "🔧 Używam Java 17: $JAVA_HOME"
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
