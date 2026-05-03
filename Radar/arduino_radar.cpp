#include <Servo.h>

const int trigPin = 10;
const int echoPin = 11;
Servo radarServo;

void setup() {
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  Serial.begin(9600);
  radarServo.attach(9);
}

void loop() {
  // 1- 0 to 180 
  for (int angle = 0; angle <= 180; angle++) {
    radarServo.write(angle);
    delay(40);
    sendData(angle);
  }
  
  // 2 - 180 to 0 
  for (int angle = 180; angle >= 0; angle--) {
    radarServo.write(angle);
    delay(40);
    sendData(angle);
  }
}

void sendData(int angle) {
  long duration;
  int distance;
  
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  
  duration = pulseIn(echoPin, HIGH);
  distance = duration * 0.034 / 2;

  // Format: Angle,Distance
  Serial.print(angle);
  Serial.print(",");
  Serial.print(distance);
  Serial.println(".");
}