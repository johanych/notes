from scapy.all import *
import ipaddress

def discover_network(base_ip):
    alive_hosts = []
    for ip in ipaddress.IPv4Network(base_ip, strict=False):
        if ip.is_reserved:
            continue  # Skip reserved IP addresses
        pkt = IP(dst=str(ip))/ICMP()
        resp = sr1(pkt, timeout=1, verbose=0)
        if resp is not None and ICMP in resp:
            alive_hosts.append(str(ip))
    return alive_hosts

def main():
    for j in range(1, 255):
        base_network = f"172.{j}.0.0/16"
        print(f"-------------------------\nBUSCANDO REDES EN {base_network}\n-------------------------\n")
        for i in range(0, 255):
            subnet = f"172.{j}.{i}.0/24"
            print(f"::Escaneando {subnet}::")
            alive_hosts = discover_network(subnet)
            if alive_hosts:
                print(f">>{subnet}")
                with open("redes.txt", "a") as file:
                    file.write(f"{subnet}\n")
                    for host in alive_hosts:
                        file.write(f"Host Vivo: {host}\n")

if __name__ == "__main__":
    main()
