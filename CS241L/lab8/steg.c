/*********************************/
/* Anthony Galczak               */
/* 11-18-16                      */
/* CS-241 Section 001            */
/* Lab 8                         */
/* Steganography hides a message */
/* inside a bitmap image         */
/*********************************/

#include <stdio.h>

void appendBits(unsigned char* fourBytes, char c);

/* appendBits: */
/* Receives a character array with 4 bytes and a character c */
/* Modifies the four bytes in place implanting c in the last 2 bits of the 4 bytes */
void appendBits(unsigned char* fourBytes, char c)
{
  /* I want: first 6 bits of fourBytes[0], first 2 bits of c */
  /* 0xfc - Remove last 2 bits, 0x03 - Remove first 6 bits */
  fourBytes[0] = (fourBytes[0] & 0xfc) | ((c >> 6) & 0x03);
  fourBytes[1] = (fourBytes[1] & 0xfc) | ((c >> 4) & 0x03);
  fourBytes[2] = (fourBytes[2] & 0xfc) | ((c >> 2) & 0x03);
  fourBytes[3] = (fourBytes[3] & 0xfc) | (c & 0x03);
}

/* First argument is input image, second argument is output image, stdin is secret msg */
int main(int argc, char **argv)
{
  int c;
  FILE *input;
  FILE *output;
  unsigned char header[54];
  unsigned char fourBytes[4];
  unsigned char singleChar[1];

  c = 0;

  /* Opening the files per the command line arguments */
  input = fopen(argv[1], "rb");
  output = fopen(argv[2], "wb");

  /* Reading in and then writing the header */
  fread(header, 1, 54, input);
  fwrite(header, 1, sizeof(header), output);

  /* Reading in 4 bytes of input and encoding 1 byte of stdin */
  while((c = getchar()) != EOF)
  {
    fread(fourBytes, 1, 4, input);

    /* Changing the 4 bytes in place */
    appendBits(fourBytes, c);
  
    /* Writing the modified four bytes to the output file */
    fwrite(fourBytes, 1, sizeof(fourBytes), output);
  }

  /* Done reading in stdin */
  /* Read 4 more bytes from input and encode a null character in it */
  fread(fourBytes, 1, 4, input);
  appendBits(fourBytes, '\0');
  fwrite(fourBytes, 1, sizeof(fourBytes), output);  

  /* Finish 1 to 1 read and write of the bmp file */
  while(1)
  { 
    fread(singleChar, 1, 1, input);

    /* When we hit the end of line in the file, break out of the loop */
    if(feof(input)) break;
    fwrite(singleChar, 1, sizeof(singleChar), output);
  }

  /* Closing the files */
  fclose(input);
  fclose(output);

  return 0;
}




