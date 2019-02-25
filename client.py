import socket
import sys
import time

conn = socket.socket()
host = input(str("IP:"))
port = 8080

conn.connect((host, port))

while True:
    message = input(str(">>"))
    message = message.encode()

    conn.send(message)

    incoming_message = conn.recv(1024)
    incoming_message = incoming_message.decode()
    print("Server :" + incoming_message)