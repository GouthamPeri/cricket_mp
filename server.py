import socket
import sys
import time

s = socket.socket()
host = socket.gethostname()
ipaddr = socket.gethostbyname(host)
print("Server will start on :" + ipaddr)
port = 8080

s.bind((host, port))

s.listen(1)

conn, addr = s.accept()

while True:
    message = input(str(">>"))
    message = message.encode()

    conn.send(message)

    incoming_message = conn.recv(1024)
    incoming_message = incoming_message.decode()
    print("Client :" + incoming_message)