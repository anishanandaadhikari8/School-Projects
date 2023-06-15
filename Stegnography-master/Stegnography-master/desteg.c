/**********************************************/
/*Anish Adhikari                              */
/*Nov 18, 2018                                */
/*CS 241L    Section #002                     */
/**********************************************/

#include <stdlib.h>
#include <stdio.h>

/* @param inputFile, type FILE* inputFile
 *  is the file that contains the image data
 * This function returns an encoded character from
 *  the image*/

char getEncodedChar(FILE* inputFile)
{
  char tempChar;
  int i = 0;
  char newChar = '\0';
  
  /* loops through 4 values and gets the last two bits 
   * which constructs a character from encoded pixels */
  for (i = 0; i <= 3; i++)
  {
    fread(&tempChar, 1, 1, inputFile);
    /* gets the last two bits */
    tempChar = tempChar & 3;
    /* construct a new character by placing the bits from
     * last to bottom */
    newChar = newChar | (tempChar << i * 2);
  }
  
  return newChar;
}

int getIntFromByteArray(unsigned char bytes[])
{
  int n = 
    bytes[0] |
    bytes[1] << 8 | 
    bytes[2] << 16 |
    bytes[3] << 24; 
  return n;
}

int main(int argc, char** argv)
{
  char* inputFileName = argv[1];
  FILE* inputFile = fopen(inputFileName, "rb");
  char encodedChar;
  unsigned char header[54];
  int i;
 
  /* read header into array */
  fread(header, 1, 54, inputFile);

  /* is this really a bitmap? */
  if (header[0] != 'B' || header[1] != 'M')
  {
    printf("Input file is not a bitmap\n");
    exit(1);
  }

  /* Is the header size what we expect? */
  if (getIntFromByteArray(&header[10]) != 54)
  {
    printf("Unexpected header size\n");
    exit(1);
  }

  /* How many bits per pixel? Expecting 24*/
  if (!(header[28] == 24 && header[29] == 0))
  {
    printf("Unexpected number of bits/pixel\n");  
    exit(1);
  }
  
  fread(header, 1, 54, inputFile);
  encodedChar = getEncodedChar(inputFile);
  
  /* prints the decoded message in a loop until the last 
   * character is reached */
  i = 0;
  while (encodedChar != '\0')
  {
    printf("%c", encodedChar);
    encodedChar = getEncodedChar(inputFile);
    i++;
  }
  
  return 0;
}