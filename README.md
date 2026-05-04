# SideQuests:

Hobby projects i might feel like trying for fun.

## Project Radar Mapping
To build an autonomous mapping platform capable of generating 3D floor plans using an IMU-stabilized 8x8 ToF array mounted on a dual-axis robotic base.

## Roadmap

### **Phase 1: Proof of Concept**
*   **Sensors:** VL53L0X (1m range).
*   **Actuation:** Single-axis Servo.
*   **Visualization:** Processing App via Serial.
*   **Status:** ✅Functional 2D Radar with CRT-style persistence.

### **Phase 2: Multi-Zone & Portability**
*   **Sensor Upgrade:** VL53L5CX (8x8 multizone array) for 64 points per frame.
*   **Actuation:** Pan-Tilt system for vertical/horizontal scanning.
*   **Display:** Local OLED/LCD for standalone use.
*   **Communication:** ESP-NOW wireless link between two ESP32 units.

### **Phase 3: SLAM & 3D Mapping**
*   **Stabilization:** IMU (MPU6050) to compensate for robot movement.
*   **Integration:** Mobile app control and 3D Point Cloud rendering.
*   **Final Goal:** A mobile robot that autonomously maps a house in 3D.

## Tech Stack
*   **Hardware:** ESP32, Arduino, VL53Lxx ToF Series, Servos, 3D Printer.
*   **Software:** C++ (Firmware), Java/Processing (Visualization).


<img width="1196" height="696" alt="image" src="https://github.com/user-attachments/assets/dc0c6997-dab2-4256-b368-af8ed4afe3da" />

### TO DO:
- Bill of materials to be addes (BOM)
- pictures to be added 
- proper documentation , code and project folder structure
