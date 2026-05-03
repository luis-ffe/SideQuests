// this file extension should be .pde to use in the processing app
// 

import processing.serial.*; 

Serial myPort; 
String data = "";
float virtualAngle = 0; 
float sweepSpeed = 1.2;
int direction = 1; 

float[] blipX = new float[181]; 
float[] blipY = new float[181];
float[] blipOpacity = new float[181];

void setup() {
  size(1200, 700); 
  pixelDensity(displayDensity()); 
  smooth();
  
  for (int i = 0; i < 181; i++) {
    blipOpacity[i] = 0;
  }

  String[] ports = Serial.list();
  String arduinoPort = "";
  for (String p : ports) {
    if (p.contains("usbmodem") || p.contains("usbserial")) {
      arduinoPort = p;
    }
  }

  if (!arduinoPort.equals("")) {
    myPort = new Serial(this, arduinoPort, 9600);
    myPort.bufferUntil('.'); 
  }
}

void draw() {
  // fade
  fill(0, 30); 
  noStroke();
  rect(0, 0, width, height);
  
  virtualAngle += (sweepSpeed * direction);
  if (virtualAngle >= 180 || virtualAngle <= 0) {
    direction *= -1; 
  }
  
  drawRadarGrid();          
  drawBlips();     
  drawSweepLine(virtualAngle); 
}

void serialEvent (Serial myPort) { 
  try {
    data = myPort.readStringUntil('.');
    if (data != null) {
      data = data.substring(0, data.length()-1);
      String[] list = split(data, ',');
      if (list.length == 2) {
        int dist = int(list[1]);
        
        // DETECTION TRIGGER
        if(dist < 40 && dist > 2) {

          float rad = radians(virtualAngle + 180);
          float pixsDistance = dist * 15; 
          
          int slot = int(virtualAngle);
          blipX[slot] = pixsDistance * cos(rad);
          blipY[slot] = pixsDistance * sin(rad);
          blipOpacity[slot] = 255;
        }
      }
    }
  } catch (Exception e) {}
}

void drawBlips() {
  pushMatrix();
  translate(width/2, height - 50); 
  for (int i = 0; i <= 180; i++) {
    if (blipOpacity[i] > 0) {
      strokeWeight(10);
      stroke(255, 0, 0, blipOpacity[i]);
      point(blipX[i], blipY[i]);
      
      blipOpacity[i] -= 0.5; 
    }
  }
  popMatrix();
}

void drawRadarGrid() {
  pushMatrix();
  translate(width/2, height - 50); 
  noFill();
  strokeWeight(2);
  stroke(0, 200, 0, 60); 
  for (int r=200; r<=800; r+=200) {
    arc(0, 0, r, r, PI, TWO_PI);
  }
  popMatrix();
}

void drawSweepLine(float angle) {
  pushMatrix();
  translate(width/2, height - 50); 
  
  float rad = radians(angle + 180);

  strokeWeight(20);
  stroke(0, 255, 0, 30);
  line(0, 0, (width*0.45)*cos(rad), (width*0.45)*sin(rad));
  
  strokeWeight(4);
  stroke(0, 255, 0); 
  line(0, 0, (width*0.45)*cos(rad), (width*0.45)*sin(rad)); 
  
  popMatrix();
}