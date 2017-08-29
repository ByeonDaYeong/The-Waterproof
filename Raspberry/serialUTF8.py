import serial

ser = serial.Serial('/dev/ttyACM0', 115200, timeout=1)

try:
    while 1:
        response = ser.readline()
        print(response)
except KeyboardInterrupt:
    ser.close()