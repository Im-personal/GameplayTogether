import socket
import threading


class Device:
    def __init__(self,role, sock):

        self.role = role
        self.sock = sock


class StreamServer:
    def __init__(self):
        self.devices = []
        self.lock = threading.Lock()

    def handle_client(self, conn, addr):
        data = conn.recv(1024).decode()
        if not data:
            return
        role = data
        with self.lock:
            self.devices.append(Device(role, conn))
        conn.sendall(b'conn')  # Отправляем сообщение об успешном подключении
        while True:
            data = conn.recv(1024).decode()
            print(data)
            if not data:
                break
            if data == 'startStream':
                sender = next((d for d in self.devices if d.role == 'sender'), None)
                receiver = next((d for d in self.devices if d.role == 'receiver'), None)
                if sender and receiver:
                    threading.Thread(target=self.stream, args=(sender.sock, receiver.sock)).start()
                    break

    def stream(self, sender_sock, receiver_sock):
        while True:
            data = sender_sock.recv(1024)
            if not data:
                break
            receiver_sock.sendall(data)

    def run(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.bind(('', 8081))
            s.listen()
            while True:
                conn, addr = s.accept()
                threading.Thread(target=self.handle_client, args=(conn, addr)).start()


if __name__ == '__main__':
    server = StreamServer()
    server.run()
    print("started!")
