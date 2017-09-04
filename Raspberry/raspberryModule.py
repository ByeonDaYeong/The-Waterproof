import serial
from socketIO_client import SocketIO, BaseNamespace, LoggingNamespace

ser = serial.Serial(
    port='/dev/cu.usbmodem1421', \
    baudrate=115200, \
    parity=serial.PARITY_NONE, \
    stopbits=serial.STOPBITS_ONE, \
    bytesize=serial.EIGHTBITS, \
    timeout=1)


def on_connect():
    print('[Connected]')


def on_reconnect():
    print('[Reconnected]')


def on_disconnect():
    print('[Disconnected]')


socketIO = SocketIO('localhost', 8801, LoggingNamespace, verify=False)
socketIO.on('connect', on_connect)
socketIO.on('disconnect', on_disconnect)

try:
    while 1:
        response = ser.readline()
        realResponse = response.decode('utf-8')[:len(response) - 1]
        print(realResponse)
        inWater = 0
        outWater = 0

        if realResponse[0:2] == 'In':
            inWater = realResponse[2:]
        else:
            outWater = realResponse[3:]
        try:
            if int(inWater) >= 700 or int(outWater) >= 700:
                ser.write(b'off');
        except Exception as e:
            pass

        socketIO.emit('rasp', {'inWater': inWater, 'outWater': outWater});
except KeyboardInterrupt:
    ser.close()
