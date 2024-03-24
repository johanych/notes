import socket
from ipaddress import ip_network

def scan_network(network, ports):
    """Escanea los hosts en la red dada y verifica puertos específicos."""
    alive_hosts = {}
    for ip in ip_network(network).hosts():
        ip_str = str(ip)
        for port in ports:
            try:
                socket.setdefaulttimeout(1)
                sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                result = sock.connect_ex((ip_str, port))
                if result == 0:
                    print(f"Host activo encontrado en {ip_str}:{port}")
                    if ip_str in alive_hosts:
                        alive_hosts[ip_str].append(port)
                    else:
                        alive_hosts[ip_str] = [port]
                else:
                    print(f"No se encontró servicio en {ip_str}:{port}")  # Línea agregada
                sock.close()
            except socket.error:
                print(f"Error al conectar con {ip_str}:{port}")  # Mensaje en caso de error de socket
                continue
    return alive_hosts

def check_services(hosts):
    """Revisa la disponibilidad de servicios específicos basándose en el puerto."""
    for host, ports in hosts.items():
        for port in ports:
            service = ""
            if port == 80:
                service = "HTTP"
            elif port == 25:
                service = "SMTP"
            elif port == 21:
                service = "FTP"
            elif port == 22:
                service = "SSH"
            print(f"Servicio {service} disponible en {host}:{port}")

# Ejemplo de uso
network = "192.168.1.0/24"
ports = [25, 21, 22, 80]  # Puertos a escanear
alive_hosts = scan_network(network, ports)
check_services(alive_hosts)

