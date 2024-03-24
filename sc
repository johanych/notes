import socket
import requests
from ipaddress import ip_network

def scan_network(network):
    """Escanea los hosts en la red dada."""
    alive_hosts = []
    for ip in ip_network(network).hosts():
        try:
            # Verificación rápida de la disponibilidad del host
            socket.setdefaulttimeout(1)
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            result = sock.connect_ex((str(ip), 80))  # Prueba el puerto 80 como ejemplo
            if result == 0:
                print(f"Host activo encontrado: {ip}")
                alive_hosts.append(str(ip))
            sock.close()
        except socket.error:
            pass
    return alive_hosts

def check_web_services(hosts):
    """Revisa la disponibilidad de servicios web en los hosts."""
    for host in hosts:
        try:
            response = requests.get(f"http://{host}", timeout=2)
            if response.status_code == 200:
                print(f"Servicio web disponible en: http://{host}")
        except requests.ConnectionError:
            pass

# Ejemplo de uso
network = "192.168.1.0/24"
alive_hosts = scan_network(network)
check_web_services(alive_hosts)
