/*********************************/
/* Anthony Galczak               */
/* 11-18-16                      */
/* CS-241 Section 001            */
/* Lab 8                         */
/* Desteg reveals a message      */
/* inside a bitmap image         */
/*********************************/

#include <stdio.h>

int main(int argc, char **argv)
{
  FILE *input;
  int c;
  unsigned char header[54];
  unsigned char fourBytes[4];

  /* Reading the file */
  input = fopen(argv[1], "rb");

  /* Throwing away the header */
  fread(header, 1, 54, input);

  /* After the header, read the last 2 bits of each byte to construct characters */
  while(1)
  {
    c = 0;

    /* Reading the four bytes in to gather a character from each if it's last two bits */
    fread(fourBytes, 1, 4, input);
  
    /* Grabbing the last two bits of each element */  
    c |= ((fourBytes[0] & 0x03) << 6);
    c |= ((fourBytes[1] & 0x03) << 4);
    c |= ((fourBytes[2] & 0x03) << 2);
    c |= (fourBytes[3] & 0x03);

    /* When the character becomes a null char, we're done */
    if(c == '\0') break;
    
    /* Outputting characters to stdout */
    printf("%c", c);

  }

  fclose(input);

  return 0;
}

