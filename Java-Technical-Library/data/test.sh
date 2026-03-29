#!/bin/bash
# ─────────────────────────────────────────────────────────────
# test.sh — Run manual tests
# Compile first with ./compile.sh
# ─────────────────────────────────────────────────────────────

echo ""
echo "  ── ResourceRepository Tests ──"
java -cp out com.library.ResourceRepositoryTest

echo ""
echo "  ── LibraryService Tests ──"
java -cp out com.library.LibraryServiceTest