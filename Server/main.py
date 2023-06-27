import socket

def handle_client(client_socket, client_address):
    print(f"Accepted connection from {client_address}")
    while True:
        data = client_socket.recv(1024)
        if not data:
            break
        # здесь можно добавить код для пересылки данных на другое устройство
        print(data)
    print("process closed")
    client_socket.close()

def start_server():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('0.0.0.0', 8080))
    server_socket.listen(2)
    print("Server is listening on port 8080")
    while True:
        client_socket, client_address = server_socket.accept()
        handle_client(client_socket, client_address)

if __name__ == "__main__":
    print("process started")
    start_server()
