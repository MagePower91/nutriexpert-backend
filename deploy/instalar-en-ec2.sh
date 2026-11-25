#!/bin/bash
# Se ejecuta UNA vez dentro de la instancia EC2 (Amazon Linux 2023), despues de copiar esta carpeta.
#   scp -i labsuser.pem -r deploy ec2-user@<elastic-ip>:~
#   ssh -i labsuser.pem ec2-user@<elastic-ip> 'bash ~/deploy/instalar-en-ec2.sh'
set -e
sudo dnf install -y java-17-amazon-corretto-headless

# 1 GB de swap: con tres JVM en una t3.micro evita que el sistema mate un servicio por memoria.
if [ ! -f /swapfile ]; then
  sudo dd if=/dev/zero of=/swapfile bs=1M count=1024
  sudo chmod 600 /swapfile
  sudo mkswap /swapfile
  sudo swapon /swapfile
  echo '/swapfile swap swap defaults 0 0' | sudo tee -a /etc/fstab
fi

for s in ms-catalogo ms-registros ms-usuarios; do
  sudo mkdir -p /opt/nutriexpert/$s
  sudo cp ~/deploy/$s.service /etc/systemd/system/
done
sudo chown -R ec2-user:ec2-user /opt/nutriexpert
sudo systemctl daemon-reload
sudo systemctl enable ms-catalogo ms-registros ms-usuarios
echo "Listo. Ahora copia los .jar con deploy/subir-jars.sh desde tu computador."
