
# RoboLift

RoboLift este un robot creat pentru a ridica bomboane, sau diferite lucruri.

## Authors

- [@AkabJack](https://github.com/AkabJack)


## Screenshots

![App Screenshot](https://github.com/AkabJack/RoboLift/blob/master/app.jpg?raw=true)  
![Robot Screenshot](https://github.com/AkabJack/RoboLift/blob/master/robot.jpg?raw=false)  
![Robot Blender File](https://github.com/AkabJack/RoboLift/blob/master/modelRobot.png?raw=false)  
## Piese utilizate

- 2 Mini Breadboard
- 2 Motoare cu reductor si roata
- Arduino Nano
- Întrerupător limitativ
- Modul BLE 4.0 JDY-08
- LED + resistor 220Ω
- Modul Driver de Motoare Dual în Miniatură
- Modul DC-DC Boost XL6009
- Micro Servomotor SG90 180°
- Diferite piese printate 3D (Blender file inclus)


## Documentation

Robotul este compus din 2 parti: aplicatia pentru Android si partea hardware a robotului.  
Modul de functionare: Aplicatia va cauta dispozitive de tip BLE pentru a se conecta, odata ales robotul din lista, aplicatia va incepe sa caute serviciile modulului BLE.
Cand sa ales serviciul necesar, aplicatia va indexa caracteristicele serviciului.
Dupa ce sa ales caracteristica de scriere, aplicatia se va intoarce la ecranul principa, unde va trebui sa folosim butonul de "Connect".
Cand sa conectat aplicatia la robot, ledul de notificare de pe modul va ramane aprins pe durata utilizari. Aplicatia suporta functia de autoconnect pana la iesirea din aplicatie.
Codul pentru arduino este inclus in proiect.
### License

    MIT License

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

