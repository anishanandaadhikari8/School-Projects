/**********************************************/
/* Anthony Galczak                            */
/* 12-9-16                                    */
/* CS-241 Section 001                         */
/* Lab 9                                      */
/* Encoding or decoding a file with a         */
/* compression algorithm using huffman codes. */
/* Methods are called via additional files    */
/* called huffencode.c and huffdecode.c       */
/**********************************************/

#include <stdio.h>
#include "huffman.h"

int main(int argc, char** argv)
{
  char* infile;
  char* outfile;
  FILE* in;
  FILE* out;

  if(argc != 3) 
  {
    printf("wrong number of args\n");
    return 1;
  }

  infile = argv[1];
  outfile = argv[2];

  in = fopen(infile, "rb");
  if(in == NULL)
  {
    printf("couldn't open %s for reading\n", infile);
    return 2;
  }

  out = fopen(outfile, "wb");
  if(out == NULL)
  {
    printf("couldn't open %s for writing\n", outfile);
    return 3;
  }

  decodeFile(in, out);

  fclose(in);
  fclose(out);

  return 0;
}
