import socket
import sys
import time


def send_client_name_and_receive_server_name(conn, CLIENT_NAME):
        
    # Encoding and sending the client name
    client_name = CLIENT_NAME.encode()
    conn.send(client_name)

    # Receiving and initializing the clients name

    server_name = conn.recv(1024)
    server_name = server_name.decode()
    return server_name


def start_client():
    SERVER_NAME = ''
    CLIENT_NAME = ''

    conn = socket.socket()
    host = input(str("IP:"))
    port = 8080

    conn.connect((host, port))

    print("Please Enter your name:", end = '')
    CLIENT_NAME = input().strip()

    if not CLIENT_NAME:
        CLIENT_NAME = 'Player 2'

    SERVER_NAME = send_client_name_and_receive_server_name(conn, CLIENT_NAME)

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
        print(str(SERVER_NAME) + " :" + incoming_message)
        print(str(CLIENT_NAME) + " :" + outgoing_message)


start_client()