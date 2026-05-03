#include <Wire.h>
#include <VL53L0X.h>

VL53L0X sensor;

void setup() {
  Serial.begin(9600);
  Wire.begin();

  sensor.setTimeout(500);
  if (!sensor.init()) {
    Serial.println("Failed to detect and initialize sensor!");
    while (1) {}
  }

  // --- OPTIONAL MODES  ---
  
  // 1. Long Range Mode: Increases sensitivity but is more prone to noise
  // sensor.setSignalRateLimit(0.1);
  // sensor.setVcselPulsePeriod(VL53L0X::VcselPeriodPreRange, 18);
  // sensor.setVcselPulsePeriod(VL53L0X::VcselPeriodFinalRange, 14);

  // 2. High Accuracy: Increases measurement timing budget to 200ms
  // sensor.setMeasurementTimingBudget(200000);

  // 3. High Speed: Decreases timing budget to 20ms for fast tracking
  // sensor.setMeasurementTimingBudget(20000);

  sensor.startContinuous();
}

void loop() {
  uint16_t distance = sensor.readRangeContinuousMillimeters();

  if (sensor.timeoutOccurred()) { 
    Serial.print(" TIMEOUT"); 
  }

  Serial.print("Distance: ");
  Serial.print(distance);
  Serial.println("mm");

  delay(50);
}