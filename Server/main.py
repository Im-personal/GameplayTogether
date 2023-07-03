# Импортируем необходимые модули
from http.server import BaseHTTPRequestHandler, HTTPServer
import logging

# Определяем класс обработчика HTTP-запросов
class MyServer(BaseHTTPRequestHandler):
    # Метод для обработки HTTP POST-запросов
    def do_POST(self):
        # Получаем длину содержимого из заголовков запроса
        content_length = int(self.headers['Content-Length'])
        # Читаем данные из тела запроса
        data = self.rfile.read(content_length)

        # Выводим сообщение в лог о полученных данных
        print(f'Received data: {len(data)} bytes')
        # Отправляем ответ 200 OK
        self.send_response(200)
        self.end_headers()

# Функция для запуска сервера
def run(server_class=HTTPServer, handler_class=MyServer, port=8080):
    # Настраиваем логирование
    logging.basicConfig(level=logging.INFO)
    # Создаем экземпляр сервера
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    # Выводим сообщение в лог о запуске сервера
    print(f'Starting server on port {port}')
    try:
        # Запускаем сервер
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    # Останавливаем сервер
    httpd.server_close()
    # Выводим сообщение в лог об остановке сервера
    print('Stopping server')

# Точка входа в программу
if __name__ == '__main__':
    # Запускаем сервер
    run()
