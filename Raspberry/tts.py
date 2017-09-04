# -*- coding:utf-8 -*-
import os
import sys
import urllib.request

#client_id = "5aswYI9qgxt45cEqJ1WP"
#client_secret = "BkUPYoTP4W"
client_id = "xf924K4IYWlnu5iFAjsm"
client_secret = "eNuA0yqy0X"
encText = urllib.parse.quote("현재 침수가 진행중이고 위험한 상황입니다. 2차 피해를 방지하기 위해서 전기와 가스를 차단하였습니다. 119및 보호자에게 연락을 보냈습니다. 이동시 반드시 핸드폰을 지참히시기 바랍니다")
data = "speaker=mijin&speed=0&text=" + encText;
url = "https://openapi.naver.com/v1/voice/tts.bin"
request = urllib.request.Request(url)
request.add_header("X-Naver-Client-Id", client_id)
request.add_header("X-Naver-Client-Secret", client_secret)
response = urllib.request.urlopen(request, data=data.encode('utf-8'))
rescode = response.getcode()
if (rescode == 200):
    print("TTS mp3 저장")
    response_body = response.read()
    with open('Level4.mp3', 'wb') as f:
        f.write(response_body)
else:
    print("Error Code:" + rescode)