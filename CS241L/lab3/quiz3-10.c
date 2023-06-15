#include <stdio.h>
void main(void)
{
  unsigned char a=37;
    unsigned char n;
	  int i;

	    for (i=7; i>=0; i--)
		  {
		       n = 1 << i;
			        if (!(a & n)) printf("%d, ", n);
					  }
					    printf("\n");
						}





