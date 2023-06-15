/********************************/
/* Anthony Galczak              */
/* 10-27-16                     */
/* CS-241 Section 001           */
/* Lab 6                        */
/* Linear Congruential Generator*/
/* that utilizes a cipher algo  */
/* to encrypt and decrypt text  */
/********************************/

#include <stdio.h>
/* For memset */
#include <string.h>
#include <stdlib.h>
/* For LONG_MAX */
#include <limits.h>
#include "lcg.h"


/* TODO: Read in string a better way so that I can handle lines over 500 chars */

void encrypt(int outputChar);

void encrypt(int outputChar)
{
  
  /* if outputChar is:
    < 32 - Replace with '*' and '@' + outputChar
    = 127 - Replace with '*' and '&'
    = '*' - Replace with '*' and '*' */
  if(outputChar < 32)
  {
    putchar('*');
    putchar('@' + outputChar);
  }
  else if(outputChar == 127)
  {
    putchar('*');
    putchar('&');
  }
  else if(outputChar == '*')
  {
    putchar('*');
    putchar('*');
  }
  else
  {
    putchar(outputChar);
  }

}

int main(void){
  
  int c, i, lineNum, charNum, commasNum, isEncrypt; 
  char * ptr;
  unsigned long long_lcg_m, long_lcg_c, randValue;
  char outputChar;
  char *lcg_m2;
  struct LinearCongruentialGenerator lcg;
  
  /* Char arrays to hold data from lines */
  char lcg_m[50];
  char lcg_c[50];
  char data[400];
  char line[500];

  c = lineNum = i = 0;
  long_lcg_m = long_lcg_c = 0;
  
  
   
  while((c = getchar()) != EOF)
  {
    /* Printing line number in output */
    printf("%d) ", ++lineNum);
    
    /* Clearing the line character array for use in next gechar sequence */
    memset(&line[0], 0, sizeof(line));
    
    /* Capturing the first digit into the line character array */
    line[0] = c;
    
    /* Initializing variables to correct values to process another line */
    charNum = 1;
    commasNum = 0;
    isEncrypt = 0;
    
    /* Saving the whole line to be processed */
    while ((c = getchar()) != '\n')
    {
      /* Counting commmas to validate that the line contains at least 2 commas */
      if(c == ',')
      {
        commasNum++;
      }
      line[charNum] = c;
      charNum++;
    }
    
    /* Null terminating the line */
    line[charNum] = '\0';
    
    /* Verifying first two digits are valid input */
    if((line[0] == 'e' || line[0] == 'd') && line[1] != ',' && commasNum >= 2)
    {
      /* Saving strings for use in algorithm */
      sscanf(line, "%[^,],%50[^,],%[^\n]", lcg_m, lcg_c, data);
      
      /* Finding out whether or not we're encrypting or decrypting */
      if(line[0] == 'e')
      {
        isEncrypt = 1;
      }
      
      long_lcg_c = atol(lcg_c);
      
      /* Removing the first character of lcg_m string to remove the e or d */
      lcg_m2 = lcg_m;
      lcg_m2++; 
      
      /* Converting lcg_m string to a long */
      long_lcg_m = strtoul(lcg_m2, &ptr, 10);
      
      /* Error conditions */
      if(long_lcg_c < 1 || long_lcg_m < 1 || strlen(lcg_m2) > 20)
      {
        printf("Error");
      }
      else
      {
        
        if(isEncrypt)
        {




          /* Making the LCG struct */
          lcg = makeLCG(long_lcg_m, long_lcg_c);

          if(lcg.a > lcg.m)
          {
            printf("Error");
          }
          else
          {
            i = 0;
            while(data[i] != '\0')
            {
              /* Getting my next random value, "x" */
              randValue = getNextRandomValue(&lcg);      
      
              /* Generating an output character and then outputting it to screen */
              outputChar = data[i] ^ (randValue % 128);
              encrypt(outputChar);
              ++i;
            }

          }
          

        }
        else
        {
          printf("Decrypt line");
        }
        
      } 
      
    }
    /* Line didn't start with e or d or there was a comma after e/d or there wasn't 2 commas */
    else
    {
      printf("Error");
      
    }
    
    /* Putting back the newline into stdout */
    putchar('\n');
  }

	return 0;
}
