#include <stdio.h>

void main(void)
{
  unsigned char n = 37;
    unsigned char p = 128;
	  int i;
	    char bits[9];
		  bits[8] = '\0';
		    for (i=0; i<=7; i++)
			  {
			      if (n & p) bits[i] = '1';
				      else bits[i] = '0';
					      p = p >> 1;
						    }
							  printf("%s\n", bits);
							  }
