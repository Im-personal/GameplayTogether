import asyncio
import websockets

senders = set()
receivers = set()


async def send_video(websocket, path):
    global receivers
    global senders

    # Check if the sender is already connected
    if websocket in senders:
        return

    # Check if the receiver is already connected
    if websocket in receivers:
        return

    # Receive the device type
    device_type = await websocket.recv()

    if device_type == "sender":
        senders.add(websocket)
        print("Sender connected")

        # Continue processing the video stream
        while True:
            try:

                # Receive the video frame
                frame = await websocket.recv()

                # Forward the frame to the receiver(s)
                for receiver in receivers:
                    await receiver.send(frame)

            except websockets.exceptions.ConnectionClosed:
                break

        senders.remove(websocket)
        print("Sender disconnected")

    elif device_type == "receiver":
        receivers.add(websocket)
        print("Receiver connected")

        # Wait for the connection to close
        await websocket.wait_closed()

        receivers.remove(websocket)
        print("Receiver disconnected")


async def main():
    async with websockets.serve(send_video, 'localhost', 8000):
        await asyncio.Future()  # run forever


asyncio.run(main())
