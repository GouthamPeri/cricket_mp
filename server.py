import socket
import sys
import time


def send_server_name_and_receive_client_name(conn, SERVER_NAME):
    
    # Encoding and sending the server name
    server_name = SERVER_NAME.encode()
    conn.send(server_name)

    # Receiving and initializing the clients name

    client_name = conn.recv(1024)
    client_name = client_name.decode()
    return client_name


def start_server():
    SERVER_NAME = ''
    CLIENT_NAME = ''

    s = socket.socket()
    host = socket.gethostname()
    ipaddr = socket.gethostbyname(host)
    print("Server will start on :" + ipaddr)
    port = 8080
    s.bind((host, port))
    s.listen(1)
    conn, addr = s.accept()

    print("Please Enter your name:", end = '')
    SERVER_NAME = input().strip()

    if not SERVER_NAME:
        SERVER_NAME = 'Player 1'

    CLIENT_NAME = send_server_name_and_receive_client_name(conn, SERVER_NAME)

    while True:
        outgoing_message = input(str(">>"))
        outgoing_message = outgoing_message[0]


        # Error Handling
        if (ord(outgoing_message[0]) < ord('0')
            or ord(outgoing_message[0]) > ord('6')):
            outgoing_message = '6'

        # Sending the outgoing_message
        encoded_outgoing_message = outgoing_message.encode()
        conn.send(encoded_outgoing_message)

        # Receiving the incoming_message
        incoming_message = conn.recv(1024)
        incoming_message = incoming_message.decode()
        print(SERVER_NAME + " :" + outgoing_message)
        print(CLIENT_NAME + " :" + incoming_message)

start_server()