#include <LiquidCrystal.h>

// LCD Pins
const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

const int soundAOPin = A0;
const int buzzerPin = 9;
const int threshold = 514;

void setup() {
  pinMode(buzzerPin, OUTPUT);
  lcd.begin(16, 2);
  lcd.clear();
  lcd.print("Monitoring...");
}

void loop() {
  int soundValue = analogRead(soundAOPin);

  lcd.setCursor(0, 1);
  lcd.print("Lvl: ");
  lcd.print(soundValue);
  lcd.print("     ");

  if (soundValue > threshold) {
    lcd.setCursor(11, 1);
    lcd.print("ALRM!");
    
    digitalWrite(buzzerPin, HIGH);
    delay(200); 
    digitalWrite(buzzerPin, LOW);
  } else {
    lcd.setCursor(11, 1);
    lcd.print("OK   ");
  }

  delay(1000);
}