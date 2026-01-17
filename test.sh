#!/bin/bash

# Skrypt do uruchamiania testów z poprawną wersją Java

export JAVA_HOME=/Users/roque/Library/Java/JavaVirtualMachines/corretto-17.0.13/Contents/Home

echo "🔧 Używam Java 17: $JAVA_HOME"
echo ""

./gradlew clean test --console=plain

EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    echo "✅ Wszystkie testy przeszły!"
    echo ""
    echo "📊 Raport testów:"
    echo "   build/reports/tests/test/index.html"
    echo ""
    echo "Otwórz raport: open build/reports/tests/test/index.html"
else
    echo ""
    echo "❌ Testy nie przeszły (kod: $EXIT_CODE)"
fi

exit $EXIT_CODE
