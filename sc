import socket
from ipaddress import ip_network

def check_routing(host):
    """Intenta verificar el enrutamiento hacia una red realizando una solicitud a un puerto específico."""
    ports = [25, 21, 22, 80]  # SMTP, FTP, SSH
    for port in ports:
        try:
            socket.setdefaulttimeout(1)
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            result = sock.connect_ex((host, port))
            sock.close()
            if result == 0:
                print(f"Conectividad confirmada hacia {host} en el puerto {port}")
                return True
        except socket.error:
            pass
    return False

def scan_network(network_base, second_octet_start=1, second_octet_end=254):
    """Escanea las redes rotando el segundo octeto y verifica los hosts activos en varios puertos."""
    for second_octet in range(second_octet_start, second_octet_end + 1):
        network = f"{network_base}.{second_octet}.0/24"
        gateway_ip = f"{network_base}.{second_octet}.1"
        print(f"Verificando enrutamiento hacia: {network}")
        
        if check_routing(gateway_ip):
            print(f"Enrutamiento confirmado. Escaneando {network}...")
            alive_hosts = []
            for ip in ip_network(network).hosts():
                for port in [25, 21, 22, 80]:  # Itera sobre los puertos de interés
                    try:
                        socket.setdefaulttimeout(1)
                        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                        result = sock.connect_ex((str(ip), port))
                        if result == 0:
                            print(f"Host activo encontrado en {ip}:{port}")
                            alive_hosts.append(f"{ip}:{port}")
                        sock.close()
                    except socket.error:
                        pass
            if alive_hosts:
                print(f"Hosts activos encontrados: {alive_hosts}")
            else:
                print(f"No se encontraron hosts activos en {network}.")
        else:
            print(f"No se confirmó enrutamiento hacia {network}. Continuando con la siguiente red.")

#ajustar '10.1' por el primer y segundo octeto base de la red a escanear
scan_network("10.1")
