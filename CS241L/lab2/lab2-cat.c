#include <stdio.h>


void doTheThing(int c);

int main(void)
{
	int c;
	while(EOF != (c = getchar()))
	{
		doTheThing(c);
	}
}

void doTheThing(int c)
{
	putchar(c);
}


