import socket
from ipaddress import ip_network
import requests

def scan_network(network, ports):
    """Escanea los hosts en la red dada en múltiples puertos."""
    alive_hosts = []
    for ip in ip_network(network).hosts():
        for port in ports:
            try:
                # Verificación rápida de la disponibilidad del host
                socket.setdefaulttimeout(1)
                sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                result = sock.connect_ex((str(ip), port))
                if result == 0:
                    print(f"Host activo encontrado en {ip}:{port}")
                    alive_hosts.append((str(ip), port))
                sock.close()
            except socket.error:
                pass
    return alive_hosts

def check_web_services(hosts_with_ports):
    """Revisa la disponibilidad de servicios web en los hosts."""
    for host, port in hosts_with_ports:
        try:
            if port == 80:  # Ejemplo para HTTP, adaptar según sea necesario
                response = requests.get(f"http://{host}", timeout=2)
                if response.status_code == 200:
                    print(f"Servicio web disponible en: http://{host}")
        except requests.ConnectionError:
            pass

# Ejemplo de uso
network = "192.168.1.0/24"
ports = [21, 22, 25, 80]  # Lista de puertos a escanear
alive_hosts_with_ports = scan_network(network, ports)
check_web_services(alive_hosts_with_ports)
