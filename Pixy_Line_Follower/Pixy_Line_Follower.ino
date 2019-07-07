#include <SPI.h>
#include <Pixy2.h>
#include <Wire.h>

#define X_CENTER (m_pixy.frameWidth/2)

Pixy2 m_pixy;

String piOutput = "none";

bool isTracking;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  
  m_pixy.init();
  m_pixy.changeProg("line");
  m_pixy.setLamp(1, 1);
  isTracking = false;
}

void loop() {
  // put your main code here, to run repeatedly:
  int8_t res;
  res = m_pixy.line.getMainFeatures();
  
  if (res < 0) {
    Serial.println(res);
  }
  else  {
    trackLines();
  }
}

void trackLines()  {
  if (m_pixy.line.numVectors) {
    int32_t error; 
    error = (int32_t)m_pixy.line.vectors->m_x1 - (int32_t)X_CENTER;
    Serial.write(error);
  }
}
