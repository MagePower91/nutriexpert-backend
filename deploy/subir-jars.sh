#!/bin/bash
# Se ejecuta en tu computador (Mac/Linux) desde la carpeta nutriexpert-backend.
# Compila los tres servicios, copia los .jar a EC2 y los reinicia.
#   ./deploy/subir-jars.sh <elastic-ip> <ruta-a-labsuser.pem>
set -e
IP=${1:?Uso: ./deploy/subir-jars.sh <elastic-ip> <ruta-a-labsuser.pem>}
LLAVE=${2:?Falta la ruta de la llave .pem}
RAIZ=$(cd "$(dirname "$0")/.." && pwd)

for s in ms-catalogo ms-registros ms-usuarios; do
  echo "== Compilando $s"
  (cd "$RAIZ/$s" && ./mvnw -q clean package)
  scp -i "$LLAVE" "$RAIZ/$s/target/$s.jar" ec2-user@"$IP":/opt/nutriexpert/$s/$s.jar
done

ssh -i "$LLAVE" ec2-user@"$IP" 'sudo systemctl restart ms-catalogo ms-usuarios && sleep 5 && sudo systemctl restart ms-registros && systemctl --no-pager status ms-catalogo ms-registros ms-usuarios | grep -E "●|Active"'
echo "Prueba: curl http://$IP:8081/api/cultivos"
