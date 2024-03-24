import socket
import concurrent.futures
from ipaddress import ip_address, ip_network

# Define una función para escanear un solo host
def scan_host(ip_str, ports):
    for port in ports:
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
                sock.settimeout(0.5)  # Reduce el tiempo de espera para aumentar la velocidad
                result = sock.connect_ex((ip_str, port))
                if result == 0:
                    return ip_str  # Retorna el host si algún puerto está abierto
        except socket.error:
            pass
    return None

def scan_network(network_prefix, ports, start_third_octet, end_third_octet):
    alive_hosts = []
    network_prefix = network_prefix[:network_prefix.rfind('.')]  # Remueve el último octeto
    with concurrent.futures.ThreadPoolExecutor(max_workers=100) as executor:  # Ajusta el número de workers según tu entorno
        future_to_ip = {executor.submit(scan_host, f"{network_prefix}.{i}.{j}", ports): f"{i}.{j}" for i in range(start_third_octet, end_third_octet + 1) for j in range(0, 256)}
        for future in concurrent.futures.as_completed(future_to_ip):
            ip_suffix = future_to_ip[future]
            try:
                host = future.result()
                if host:
                    print(f"Host activo encontrado: {network_prefix}.{ip_suffix}")
                    alive_hosts.append(f"{network_prefix}.{ip_suffix}")
            except Exception as exc:
                print(f"{network_prefix}.{ip_suffix} generó una excepción: {exc}")
    return alive_hosts

# Ejemplo de uso
network_prefix = "10.222"  # Primeros dos octetos de la red
ports = [80, 25, 21, 22]  # Puertos a escanear
start_third_octet = 0  # Inicio del tercer octeto
end_third_octet = 255  # Fin del tercer octeto

alive_hosts = scan_network(network_prefix, ports, start_third_octet, end_third_octet)


