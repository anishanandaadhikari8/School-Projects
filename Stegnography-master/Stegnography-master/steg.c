/**********************************************/
/*Anish Adhikari                              */
/*Nov 18, 2018                                */
/*CS 241L    Section #002                     */

#include <stdlib.h> 
#include <stdio.h>


int getIntFromByteArray(unsigned char bytes[])
{
  int n = 
    bytes[0] |
    bytes[1] << 8 | 
    bytes[2] << 16 |
    bytes[3] << 24; 
  return n;
}

/* @param character, type char
 *  the character that needs to be encoded
 * @param inputFile, type FILE* inputFile
 *  is the file that contains the image data
 * @param outputFile, type FILE* outputFile
 *  is the file that contains the encoded message
 * This function encodes a character from the image
 *  by masking 2 bits of the character to the last 2 bits of the color
 *  in each pixel */
void encodeCharacter(char character, FILE* inputFile, FILE* outputFile)
{
  unsigned char tempChar;
  unsigned char newChar;
  int i = 3; 
  while (i >= 0)
  {
    fread(&newChar, 1, 1, inputFile);
    /* brings the 2 bits that need to be encoded in the first */
    tempChar = character << i * 2;
    /* brings those two bits to the end */
    tempChar = tempChar >> 6;
    /* zeros out the last two bits */
    newChar = 252 & newChar;
    /* places tempChar in the last two bits of newChar */
    newChar = newChar | tempChar;

    fwrite(&newChar, 1, 1, outputFile);
    i--;
  }
}


int main(int argc, char** argv)
{
  char* inputFileName = argv[1];
  char* outputFileName = argv[2];
  char tempChar;
  unsigned char header[54];
  int isEnd = 0;

  FILE* inputFile = fopen(inputFileName, "rb");
  FILE* outputFile = fopen(outputFileName, "wb");

  int pixelDataSize;
  int i;

  /* read header into array */
  fread(header, 1, 54, inputFile);

  pixelDataSize = getIntFromByteArray(&header[34]);

  /* is this really a bitmap? */
  if (header[0] != 'B' || header[1] != 'M')
  {
    printf("Input file is not a bitmap\n");
  }

  /* Is the header size what we expect? */
  if (getIntFromByteArray(&header[10]) != 54)
  {
    printf("Unexpected header size\n");
  }

  /* How many bits per pixel? Expecting 24 */
  if (!(header[28] == 24 && header[29] == 0))
  {
    printf("Unexpected number of bits/pixel\n");  
  }
  
  /* writes the header to the output file */
  fwrite(header, 1, sizeof(header), outputFile);

  /* loops through each color in the pixel and writes the encoded pixel to the output file */
  for (i = 0; i < pixelDataSize; i++)
  {
    /* encodes the data until the data is finished */
    while (isEnd == 0)
    {
      tempChar = getchar();
      if (tempChar != EOF) encodeCharacter(tempChar, inputFile, outputFile);
      /* puts '\0' as the last character in the message */
      else
      {
         tempChar = '\0';
         encodeCharacter(tempChar, inputFile