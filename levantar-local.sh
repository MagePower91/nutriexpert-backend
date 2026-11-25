#!/bin/bash
# Levanta los tres servicios en tu computador (Ctrl+C los detiene a todos).
set -e
RAIZ=$(cd "$(dirname "$0")" && pwd)
trap 'kill 0' EXIT
for s in ms-catalogo ms-usuarios ms-registros; do
  (cd "$RAIZ/$s" && ./mvnw -q spring-boot:run) &
done
wait
