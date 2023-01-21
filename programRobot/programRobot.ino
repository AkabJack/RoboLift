#include <AltSoftSerial.h>
#include <ServoTimer2.h>//conflict https://forum.arduino.cc/t/text-0x0-multiple-definition-of-__vector_11/558060
                        //https://projecthub.arduino.cc/ashraf_minhaj/9bbe4e52-63c2-4b4f-b153-0f160e73e4c5
#include <ezButton.h>

int inMotor1 = 2; 
int inMotor2 = 3; 
int inMotor3 = 4;
int inMotor4 = 5;
int motorLift = 6;
int servo = 10;
int buttonLift = 11;

AltSoftSerial altSerial;//pt bt-dev = 9-rx, 8-tx

//int btDevRx = 9;
//int bt-DevTx = 8
//comenzi robot - 1->5
//spate = 1;
//fata = 2;
//dreapta = 3;
//stanga = 4;
//candy = 5;

ServoTimer2 myservo;
ezButton limitSwitch(buttonLift);
int buttonStateLift = 0;
char c;

void setup() {
  
  pinMode(buttonLift, INPUT);
  pinMode(inMotor1, OUTPUT);
  pinMode(inMotor2, OUTPUT);
  pinMode(inMotor3, OUTPUT);
  pinMode(inMotor4, OUTPUT);
  myservo.attach(servo);
  myservo.write(1000);//Grade - 60 -  pulse width 0* = 750; 90* = 1500
  pinMode(motorLift, OUTPUT);
  Serial.begin(9600);
  altSerial.begin(115200);
  altSerial.println("Hello World");

}

void loop() {
  if (altSerial.available()) {//citim de pe serial-ul dispozitivului BLE
    c = altSerial.read();
    Serial.print(c);
  }
  if(c == '1'){
    inapoi();
  }
  if(c == '2'){
    inainte();
  }
  if(c == '3'){
    dreapta();
  }
  if(c == '4'){
    stanga();
  }
  if(c == '5'){
   myservo.write(1250);//Grade - 60 -  pulse width 0* = 750 - 90* = 1500
      delay(3000);
      myservo.write(1000);//grade = 30
      delay(3000);
      digitalWrite(motorLift, HIGH);// "Pornim" motorul
      
      buttonStateLift = digitalRead(buttonLift);
      while(buttonStateLift == 0){
        buttonStateLift = digitalRead(buttonLift);
        digitalWrite(motorLift, HIGH);// "Pornim" motorul
      }
      c = '69';//ca sa nu se repete codul de 2 ori :))
      altSerial.print(c);//ca sa nu se repete codul de 2 ori

      digitalWrite(motorLift, LOW);
  }
  if (c == '7'){
    stop();
  }
}

void inainte(){
  digitalWrite(inMotor1, HIGH);
  digitalWrite(inMotor2, LOW);

  digitalWrite(inMotor3, HIGH);
  digitalWrite(inMotor4, LOW);
}
void inapoi(){
  digitalWrite(inMotor1, LOW);
  digitalWrite(inMotor2, HIGH);

  digitalWrite(inMotor3, LOW);
  digitalWrite(inMotor4, HIGH);
}
void stanga(){
  digitalWrite(inMotor1, LOW);
  digitalWrite(inMotor2, HIGH);

  digitalWrite(inMotor3, HIGH);
  digitalWrite(inMotor4, LOW);
}
void dreapta(){
  digitalWrite(inMotor1, HIGH);
  digitalWrite(inMotor2, LOW);

  digitalWrite(inMotor3, LOW);
  digitalWrite(inMotor4, HIGH);
}
void stop(){
  digitalWrite(inMotor1, LOW);
  digitalWrite(inMotor2, LOW);

  digitalWrite(inMotor3, LOW);
  digitalWrite(inMotor4, LOW);
}