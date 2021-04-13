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

SPI max72_spi(PTD2, NC, PTD1);
DigitalOut load(PTD0); //will provide the load signal




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
    //int bufferValue = get value from buffer
    int bufferValue = 0;
    char newBit;
    switch(bufferValue) {
        case 0: 
            newBit = 0x01;
            break;
        case 1: 
            newBit = 0x03;
            break;
        case 2: 
            newBit = 0x07;
            break;
        case 3: 
            newBit = 0x0f;
            break;
        case 4: 
            newBit = 0x1f;
            break;
        case 5: 
            newBit = 0x3f;
            break;
        case 6: 
            newBit = 0x7f;
            break;
        case 7: 
            newBit = 0xff;
            break;
        default:
            newBit = 0x00;
            break;
                
    }
    shift1bit(toDisplay, newBit);
    pattern_to_display(toDisplay);
    
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
    // int bpm = get value from buffer
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

int main()
{
    setup_dot_matrix ();      /* setup matric */
    char toD0[8] = {0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    char toD1[8] = {0x00, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    char toD2[8] = {0x00, 0x00, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00};
    char toD3[8] = {0x00, 0x00, 0x00, 0xff, 0x00, 0x00, 0x00, 0x00};
    char toD4[8] = {0x00, 0x00, 0x00, 0x00, 0xff, 0x00, 0x00, 0x00};
    char toD5[8] = {0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0x00, 0x00};
    char toD6[8] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0x00};
    char toD7[8] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff};
    
    char toR0[8] = {0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80};
    char toR1[8] = {0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40};
    char toR2[8] = {0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20};
    char toR3[8] = {0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10};
    char toR4[8] = {0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08};
    char toR5[8] = {0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04};
    char toR6[8] = {0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02};
    char toR7[8] = {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
    
    
    
    while(1){
        // change this conditional to fit buffer/switch
        pattern_to_display(toD0);
        wait(0.5);
        pattern_to_display(toD1);
        wait(0.5);
        pattern_to_display(toD2);
        wait(0.5);
        pattern_to_display(toD3);
        wait(0.5);
        pattern_to_display(toD4);
        wait(0.5);
        pattern_to_display(toD5);
        wait(0.5);
        pattern_to_display(toD6);
        wait(0.5);
        pattern_to_display(toD7);
        wait(0.5);
        
        
        pattern_to_display(toR0);
        wait(0.5);
        pattern_to_display(toR1);
        wait(0.5);
        pattern_to_display(toR2);
        wait(0.5);
        pattern_to_display(toR3);
        wait(0.5);
        pattern_to_display(toR4);
        wait(0.5);
        pattern_to_display(toR5);
        wait(0.5);
        pattern_to_display(toR6);
        wait(0.5);
        pattern_to_display(toR7);
        wait(0.5);

    }
}