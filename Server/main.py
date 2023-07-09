import socket
import threading

import select

clients = []

connections = {}


def code(text):
    return text.encode('utf-8')


def handle_client(client_socket, client_address):
    print(f"Подключение установлено с {client_address}")
    clients.append(client_socket)

    while True:
        try:

            ready_to_read, _, _ = select.select([client_socket], [], [], 0)
            if ready_to_read:
                data = client_socket.recv(1024)
                print(data)
                strdata = data.decode('utf-8')

                if "sender" in strdata:
                    connections["sender"] = client_socket
                    client_socket.sendall(code("Connected!"))
                    print("sender is connected")
                    break
                elif "receiver" in strdata:
                    connections["receiver"] = client_socket
                    client_socket.sendall(code("Connected!"))
                    print("receiver is connected")
                    break
                else:
                    print("nuthin")

                if not data:
                    break
            else:
                #print("super nuthin")
                pass

        except RuntimeError as e:
            print(e)
            break

    clients.remove(client_socket)
    client_socket.close()
    print(f"Соединение с {client_address} закрыто")


def start_server():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    serv = input("хост: ")
    server_socket.bind((serv, 8080))
    server_socket.listen(1)
    print("Сервер запущен и ожидает подключения")

    while True:
        client_socket, client_address = server_socket.accept()
        thread = threading.Thread(target=handle_client, args=(client_socket, client_address))
        thread.start()


if __name__ == '__main__':
    start_server()
