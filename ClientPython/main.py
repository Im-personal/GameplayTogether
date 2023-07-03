import ffmpeg_streaming
from ffmpeg_streaming import Formats, Bitrate, Representation, Size

# Открываем захват экрана как источник видео
video = ffmpeg_streaming.input('desktop', capture=True)

# Создаем представление DASH
dash = video.dash(Formats.h264())

# Настраиваем битрейт и размер видео
_720p  = Representation(Size(1280, 720), Bitrate(2048, 320))
_480p  = Representation(Size(854, 480), Bitrate(1024, 192))
_360p  = Representation(Size(640, 360), Bitrate(768, 128))

dash.representations(_720p, _480p, _360p)

# Стримим видео на сервер
dash.output('http://localhost:8080/live.mpd')
