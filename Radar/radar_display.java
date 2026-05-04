// Save this file with a .pde extension and run it in Processing
// ADJUST BAUD RATE in LINE 50 +-

import processing.serial.*; 

Serial myPort; 
String data = "";
float virtualAngle = 0; 

//SPEED
float sweepSpeed = 0.5; 
int direction = 1; 

float[] blipX = new float[181]; 
float[] blipY = new float[181];
float[] blipOpacity = new float[181];

// HUD Vars
int lastDetectedDist = 0;
int lastDetectedAngle = 0;
float displayDist = 0;

void setup() {
  size(1200, 700); 
  pixelDensity(displayDensity()); 
  smooth();
  
  for (int i = 0; i < 181; i++) {
    blipOpacity[i] = 0;
  }

  String[] ports = Serial.list();
  println("Available Serial Ports:");
  printArray(ports);
  
  String arduinoPort = "";
  
  // OS Redundancy (macOS != WindowsOS) Ports
  if (ports.length > 0) {
    for (String p : ports) {
      if (p.contains("usbmodem") || p.contains("usbserial") || p.contains("COM")) {
        arduinoPort = p;
      }
    }
    
    if (arduinoPort.equals("")) {
      arduinoPort = ports[ports.length - 1];
    }
// ADJUST BAUD RATE HERE
    try {
      myPort = new Serial(this, arduinoPort, 9600);
      myPort.bufferUntil('.'); 
      println("Connected to: " + arduinoPort);
    } catch (Exception e) {
      println("Error.");
    }
  } else {
    println("NO PORTS FOUND");
  }
}

void draw() {
  fill(0, 15); 
  noStroke();
  rect(0, 0, width, height);
  
  virtualAngle += (sweepSpeed * direction);
  if (virtualAngle >= 180 || virtualAngle <= 0) {
    direction *= -1; 
  }
  
  drawRadarGrid();          
  drawSweepLine(virtualAngle); 
  drawBlips();     
  drawHUD();
}

void serialEvent (Serial myPort) { 
  try {
    data = myPort.readStringUntil('.');
    if (data != null) {
      data = data.substring(0, data.length()-1); 
      String[] list = split(data, ',');
      
      if (list.length == 2) {
        int dist = int(list[1]);
        
        if(dist <= 40 && dist > 2) {
          lastDetectedDist = dist;
          lastDetectedAngle = int(virtualAngle);
          float rad = radians(virtualAngle + 180);
          
          // 40cm max distance * 15 scale = 600 pixels
          float pixsDistance = dist * 15; 
          int slot = int(virtualAngle);
          
          if (slot >= 0 && slot <= 180) {
            blipX[slot] = pixsDistance * cos(rad);
            blipY[slot] = pixsDistance * sin(rad);
            blipOpacity[slot] = 255; 
          }
        }
      }
    }
  } catch (Exception e) {
  }
}

void drawBlips() {
  pushMatrix();
  translate(width/2, height - 50); 
  
  for (int i = 0; i <= 180; i++) {
    if (blipOpacity[i] > 0) {
      strokeWeight(15);
      stroke(255, 0, 0, blipOpacity[i] * 0.3); 
      point(blipX[i], blipY[i]);
      
      strokeWeight(6);
      stroke(255, 50, 50, blipOpacity[i]); 
      point(blipX[i], blipY[i]);
      
      // dot detection fading velocity increase the value to disapear faster
      // would be better if the detection only disapears when the sweeping goes over the smae angle and there is no longer any obstacle there.
      blipOpacity[i] -= 2.5; 
    }
  }
  popMatrix();
}

void drawRadarGrid() {
  pushMatrix();
  translate(width/2, height - 50); 
  noFill();
  strokeWeight(2);
  stroke(0, 150, 0, 80); 
  
  //SIZE 
  for (int r=200; r<=1200; r+=200) {
    arc(0, 0, r, r, PI, TWO_PI);
  }

  strokeWeight(1);
  stroke(0, 150, 0, 60);
  for (int angle = 30; angle < 180; angle += 30) {
    float rad = radians(angle + 180);
    line(0, 0, 600 * cos(rad), 600 * sin(rad));
  }
  popMatrix();
}

void drawSweepLine(float angle) {
  pushMatrix();
  translate(width/2, height - 50); 
  float rad = radians(angle + 180);
  strokeWeight(25);
  stroke(0, 255, 0, 15);
  line(0, 0, 600 * cos(rad), 600 * sin(rad));
  strokeWeight(3);
  stroke(0, 255, 0, 200); 
  line(0, 0, 600 * cos(rad), 600 * sin(rad)); 
  popMatrix();
}

void drawHUD() {
  displayDist = lerp(displayDist, lastDetectedDist, 0.08);
  
  fill(0, 255, 0);
  textSize(20);
  textAlign(LEFT);
  text("RADAR SYSTEM ONLINE", 30, 40);
  textSize(16);
  fill(0, 200, 0);
  text("SWEEP ANGLE: " + int(virtualAngle) + "°", 30, 70);
  text("TARGET RANGE: " + (lastDetectedDist > 0 ? int(displayDist) + " cm" : "SCANNING..."), 30, 95);
  text("TARGET BEARING: " + (lastDetectedDist > 0 ? lastDetectedAngle + "°" : "---"), 30, 120);
}
