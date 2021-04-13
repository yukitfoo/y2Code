#include "mbed.h"
#include <stdio.h>
#include <stdlib.h>
#include <sstream>
#include <string>

#define max7219_reg_noop         0x00
#define max7219_reg_digit0       0x01
#define max7219_reg_digit1       0x02
#define max7219_reg_digit2       0x03
#define max7219_reg_digit3       0x04
#define max7219_reg_digit4       0x05
#define max7219_reg_digit5       0x06
#define max7219_reg_digit6       0x07
#define max7219_reg_digit7       0x08
#define max7219_reg_decodeMode   0x09
#define max7219_reg_intensity    0x0a
#define max7219_reg_scanLimit    0x0b
#define max7219_reg_shutdown     0x0c
#define max7219_reg_displayTest  0x0f

#define LOW 0
#define HIGH 1

// connected to sensor (input)
AnalogIn Ain(PTB1);
//spi in order MOSI, MISO, SCLK (connected to breakout display board)
SPI max72_spi(PTD2, NC, PTD1);
// LOAD is Chip Select (connected to breakout display board)
DigitalOut load(PTD0); //will provide the load signal
AnalogOut Aout(PTE30);

const int size = 100;
float dataBuffer[size];
float* pointer = dataBuffer;
float filtering[size];
Ticker tick;
int check = 0;
// define variables required for dsp here
int n_avg = 200;
int in_min = 0;
// the max value, need to be changed
int in_max = 15;
float alpha = 0.37;

int outputs[size];
float scaling[size];
float trend[size];
float filtered[size];
float sum = 0;
float avg = 0;
float n = 20;



// display part
/*
Write to the maxim via SPI
args register and the column data
*/
void write_to_max( int reg, int col)
{
    load = LOW;            // begin
    max72_spi.write(reg);  // specify register
    max72_spi.write(col);  // put data
    load = HIGH;           // make sure data is loaded (on rising edge of LOAD/CS)
}

//writes 8 bytes to the display  
void pattern_to_display(char *testdata){
    int cdata; 
    for(int idx = 0; idx <= 7; idx++) {
        cdata = testdata[idx]; 
        write_to_max(idx+1,cdata);
    }
} 

void setup_dot_matrix ()
{
    // initiation of the max 7219
    // SPI setup: 8 bits, mode 0
    max72_spi.format(8, 0);
    max72_spi.frequency(100000); //down to 100khx easier to scope ;-)
      
    write_to_max(max7219_reg_scanLimit, 0x07);
    write_to_max(max7219_reg_decodeMode, 0x00);  // using an led matrix (not digits)
    write_to_max(max7219_reg_shutdown, 0x01);    // not in shutdown mode
    write_to_max(max7219_reg_displayTest, 0x00); // no display test
    for (int e=1; e<=8; e++) {    // empty registers, turn all LEDs off
        write_to_max(e,0);
    }
   // maxAll(max7219_reg_intensity, 0x0f & 0x0f);    // the first 0x0f is the value you can set
     write_to_max(max7219_reg_intensity,  0x08);     
 
}

void clear(){
     for (int e=1; e<=8; e++) {    // empty registers, turn all LEDs off
        write_to_max(e,0);
    }
}

void shift1bit(char* toDisplay, char newBit){
    //shift from bit 0 to 6
    // 0->1, 1->2,...,6->7
    //set final element of the char array to the newBit
    for (int i = 0; i < 7; i++) {
        toDisplay[i] = toDisplay[i+1];
    }
    toDisplay[7] = newBit;
}


void displayLevels(){
    //sets a new slate
    char toDisplay[8] = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    char newBit;
    int bufferValue = 0;
    
    //int bufferValue = get value from buffer
    for (int i = 0; i < size; i++) {
        bufferValue = outputs[i];
        switch(bufferValue) {
            case 0: 
                newBit = 0x00;
                break;
            case 1: 
                newBit = 0x40;
                break;
            case 2: 
                newBit = 0x60;
                break;
            case 3: 
                newBit = 0x70;
                break;
            case 4: 
                newBit = 0x78;
                break;
            case 5: 
                newBit = 0x7c;
                break;
            case 6: 
                newBit = 0x7e;
                break;
            case 7: 
                newBit = 0x7f;
                break;
            case 8: 
                newBit = 0xff;
                break;
            default:
                newBit = 0x12;
                break;
        }
        shift1bit(toDisplay, newBit);
        pattern_to_display(toDisplay);
        wait(0.1);
    }
}


char* getValues(char value) {
    char* byte[3];
    switch(value) {
        case 0:
            *byte[0] = (char) 0x7c;
            *byte[1] = (char) 0x44;
            *byte[2] = (char) 0x7c;
            break;
        case 1:
            *byte[0] = (char) 0x40;
            *byte[1] = (char) 0x7c;
            *byte[2] = (char) 0x00;
            break;
        case 2:
            *byte[0] = (char) 0x5c;
            *byte[1] = (char) 0x54;
            *byte[2] = (char) 0x74;
            break;
        case 3:
            *byte[0] = (char) 0x54;
            *byte[1] = (char) 0x54;
            *byte[2] = (char) 0x7c;
            break;
        case 4:
            *byte[0] = (char) 0x70;
            *byte[1] = (char) 0x10;
            *byte[2] = (char) 0x7c;
            break;
        case 5:
            *byte[0] = (char) 0x74;
            *byte[1] = (char) 0x54;
            *byte[2] = (char) 0x5c;
            break;
        case 6:
            *byte[0] = (char) 0x7c;
            *byte[1] = (char) 0x54;
            *byte[2] = (char) 0x5c;
            break;
        case 7:
            *byte[0] = (char) 0x40;
            *byte[1] = (char) 0x40;
            *byte[2] = (char) 0x7c;
            break;
        case 8:
            *byte[0] = (char) 0x7c;
            *byte[1] = (char) 0x54;
            *byte[2] = (char) 0x7c;
            break;
        case 9:
            *byte[0] = (char) 0x74;
            *byte[1] = (char) 0x54;
            *byte[2] = (char) 0x7c;
            break;
    }
    return *byte;
}

void displayHexLED(char * inVal, int control) {
    char toDisplay[8];
    char holder[3];
    if (control == 2) {
        toDisplay[0] = 0x00;
        toDisplay[1] = 0x00;
        for (int i = 0; i < 2;  i++) {
            holder[0] = getValues(*inVal)[0];
            holder[1] = getValues(*inVal)[1];
            holder[2] = getValues(*inVal)[2];
            for (int j = 0; j < 3; j++) {
                toDisplay[2+j+(3*i)] = holder[j];
            }
            inVal++;
        }
    } else {
        toDisplay[0] = 0x40;
        toDisplay[1] = 0x7c;
        for (int i = 0; i < 2;  i++) {
            holder[0] = getValues(*inVal)[0];
            holder[1] = getValues(*inVal)[1];
            holder[2] = getValues(*inVal)[2];
            for (int j = 0; j < 3; j++) {
                toDisplay[2+j+(3*i)] = holder[j];
            }
            inVal++;
        }
    }
    pattern_to_display(toDisplay);
}

void displayBPM(int bpm){
    //TODO
    int control;
    
    if (bpm < 100) {
        control = 2;
    } else {
        control = 3;
    }
    std::stringstream ss;
    ss << bpm;
    char bpmString[control];
    ss >> bpmString;
    
    char *intVal = &bpmString[0];
    displayHexLED(intVal, control);
}

// sampling part
void sample() {
    if (pointer == &dataBuffer[size]) {
        // put all of dataBuffer into filtering
        for (int i = 0; i < size; i++) {
            filtering[i] = dataBuffer[i];
        }
        // point back to start of dataBuffer
        pointer = dataBuffer;
        check = 1;
        
    } else {
        // read from PTD5 (AnalogIn), then increment pointer
        *pointer =  Ain.read();
        Aout.write(*pointer);
        pointer++;
    }
}

// define dsp
// insteadr of returning the array of value of outputs just access it using global variable call (normal call)
void dsp(void) {
    sum = 0;
    filtered[0] = filtering[0];
    for (int j = 1; j < size; j++) {
        filtered[j] = alpha * filtering[j] + (1-alpha) * (filtered[j-1]);
    }
    // trend data
    
    for (int p = 0; p < size; p++) {
        for (int q  = 0; q < n; q++) {
            sum = filtered[q]++;
        }
        avg = sum/n;
        trend[p] = avg;
    }
    
    for (int i = 0; i < size-n; i++) {
        scaling[i] = filtered[i] - trend[i];    
    }
    for (int i = 0; i <size; i++) {
        outputs[i] = (int) ((scaling[i]-in_min)/(in_max - in_min))*7;
    }
}

int getBPM() {
//    uses the peak(7) to calculate the period
//    interval is the sun of all intervals of 7s for the number of clockTicks
    int interval = 0;
//    countTicks is the number of times a whole period is detected
    int countTicks = 0;
//    betweenTcisks is the value of the number of ticks between 
    int betweenTicks = 0;
//    boolean check for the first tick
    bool first = true;
    
    for (int i = 0; i < size; i++) {
        if (outputs[i] == 7) {
            if (!first) {
                // sampled once every 0.01 s
                // this will add the number of times a 7 occurs in the int intervals in secods
                interval += betweenTicks * 0.01;
                countTicks++;
//                resets number of ticks in between
                betweenTicks = 0;
            } else {
//                statements for first 7 detected, dont store any interval values into intervals
                first = false;    
            }
        }
        betweenTicks++;
    }    
    float avgPeriod = interval/countTicks;
    float avgFreq = 1/avgPeriod;
    int BPM = (int) avgFreq*60;
    return BPM;
}

int main() {
    tick.attach(&sample, 0.1);

    while(1) {
        if (check == 1){
           dsp();
           displayLevels();
        }    
    }
}
