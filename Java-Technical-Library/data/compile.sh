#!/bin/bash
# ─────────────────────────────────────────────────────────────
# compile.sh — Compile all Java source files
# Usage:  chmod +x compile.sh && ./compile.sh
# ─────────────────────────────────────────────────────────────

SRC_DIR="src/main/java"
OUT_DIR="out"

echo ""
echo "  ╔══════════════════════════════════╗"
echo "  ║   Technical Library — BUILD      ║"
echo "  ╚══════════════════════════════════╝"
echo ""

# Clean previous build
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# Compile
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$OUT_DIR" @sources.txt

if [ $? -eq 0 ]; then
    echo "  ✔ Compilation successful.  Run ./run.sh to start."
else
    echo "  ✖ Compilation failed. Check errors above."
    exit 1
fi

rm sources.txt