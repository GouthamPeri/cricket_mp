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


def send_message(conn):
    outgoing_message = input(str(">>"))
    outgoing_message = outgoing_message[0]


    # Error Handling
    if (ord(outgoing_message[0]) < ord('0')
        or ord(outgoing_message[0]) > ord('6')):
        outgoing_message = '6'

    # Sending the outgoing_message
    encoded_outgoing_message = outgoing_message.encode()
    conn.send(encoded_outgoing_message)

    return outgoing_message


def receive_message(conn):
    incoming_message = conn.recv(1024)
    incoming_message = incoming_message.decode()


def get_status(incoming_message, outgoing_message, details):
    '''
        If the incoming and outgoing messages are same then
        the batsman is out. 
        Status = 0 -> Server Out
        Status = 1 -> Client Out 
        else status = 2
    '''
    SERVER_BAT, bat, SERVER_SCORE, CLIENT_SCORE = details
    if incoming_message == outgoing_message:
        if bat = SERVER_BAT:
            return 0
        else:
            return 1
    if bat == SERVER_BAT:
        SERVER_SCORE += incoming_message
    else:
        CLIENT_SCORE += outgoing_message

    details = []
    details.extend((SERVER_BAT, bat, SERVER_SCORE, CLIENT_SCORE))
    return 2


def start_server():
    SERVER_NAME = ''
    CLIENT_NAME = ''
    SERVER_BAT = 0
    CLIENT_BAT = 1
    CLIENT_SCORE = 0
    SERVER_SCORE = 0

    bat = SERVER_BAT

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
        #Sending the outgoing_message
        outgoing_message = send_message(conn)

        # Receiving the incoming_message
        incoming_message = receive_message(conn)

        # Getting the status
        details = []
        details.extend((SERVER_BAT, bat, SERVER_SCORE, CLIENT_SCORE))
        status = get_staus(incoming_message, outgoing_message, details)

        

        print(SERVER_NAME + " :" + outgoing_message)
        print(CLIENT_NAME + " :" + incoming_message)

start_server()