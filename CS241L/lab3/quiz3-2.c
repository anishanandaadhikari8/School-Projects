

#include <stdio.h>

void foo(int n[], int i)
{
  n[i] = n[i-2] + n[i-1];
    i = 6;
	}

	void main(void)
	{
	  int i, n[11];
	    for (i=0; i<11; i++)
		  {
		       n[i] = i*2;
			     }
				   i=4;
				     foo(n, i);
					   printf("%d\n", n[i]);
					}
