import socket
import threading

import asyncio
import logging
from pyrtmp import StreamClosedException, RTMPProtocol
from pyrtmp.messages import SessionManager
from pyrtmp.messages.audio import AudioMessage
from pyrtmp.messages.command import NCConnect, NCCreateStream, NSPublish, NCResult

import select
from pyrtmp.messages.video import VideoMessage

clients = []

connections = {}

async def handle_stream(stream):
    try:
        while True:
            message = await stream.read_message()
            if isinstance(message, AudioMessage):
                # Отправьте аудио-сообщение другому пользователю
                pass
            elif isinstance(message, VideoMessage):
                # Отправьте видео-сообщение другому пользователю
                pass
    except StreamClosedException:
        # Обработка закрытия потока
        pass

async def handle_session(session):
    # Обработка сессии RTMP
    async for message in session:
        if isinstance(message, NCConnect):
            # Отправка сообщения NCConnectResult в ответ на соединение
            await session.send(NCConnectResult())
        elif isinstance(message, NCCreateStream):
            # Создание потока и запуск обработки потока
            stream = await session.create_stream()
            asyncio.ensure_future(handle_stream(stream))
        elif isinstance(message, NSPublish):
            # Отправка сообщения NSPublish в ответ на команду NSPublish
            await session.send(NSPublish())

async def main():
    # Создание RTMP-сервера и обработка входящих сессий
    async with RTMPProtocol.create_server(('0.0.0.0', 1935)) as server:
        async for session in server:
            asyncio.ensure_future(handle_session(session))

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
                    continue
                elif "receiver" in strdata:
                    connections["receiver"] = client_socket
                    client_socket.sendall(code("Connected!"))
                    print("receiver is connected")
                    continue
                else:
                    if connections["sender"] == client_socket:
                        process_sender(data)
                    elif connections["receiver"] == client_socket:
                        process_receiver(data)

                #if not data:
                #    break
            else:
                # print("super nuthin")
                pass

        except RuntimeError as e:
            print(e)
            break

    clients.remove(client_socket)
    client_socket.close()
    print(f"Соединение с {client_address} закрыто")


def process_sender(data):
    print("process_receiver")
    pass


def process_receiver(data):
    print("process_receiver")
    connections["sender"].sendall(data)
    pass


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
