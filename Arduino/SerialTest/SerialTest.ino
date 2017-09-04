const int depthOutPin = A0;
const int depthInPin = A1;
const int ledPin1 = 2;
const int ledPin2 = 3;
int numberIn;
int numberOut;
int sw=30;

void setup() {
  Serial.begin(115200);
  pinMode(ledPin1,OUTPUT);
  pinMode(ledPin2,OUTPUT);
}

void loop() {
  if(Serial.available()){   
    sw=0;
  }
  analogWrite(ledPin1,sw);
  analogWrite(ledPin2,sw);
  numberIn = analogRead(depthInPin);
  numberOut = analogRead(depthOutPin);
  Serial.print("In");
  Serial.print(numberIn);
  Serial.print("\n");
  Serial.print("Out");
  Serial.print(numberOut);
  Serial.print("\n");
  delay(1000);
}
