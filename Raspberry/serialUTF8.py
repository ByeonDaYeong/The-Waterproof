from socketIO_client import SocketIO, LoggingNamespace
import serial

ser = serial.Serial('/dev/ttyACM0', 115200, timeout=1)
socketIO = SocketIO('192.168.0.217', 8801, LoggingNamespace)

try:
    while 1:
        response = ser.readline()
        print(response)
except KeyboardInterrupt:
    ser.close()