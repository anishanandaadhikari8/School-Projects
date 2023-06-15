#include <stdio.h>
#include <limits.h>

int main(void)
{

	printf("Char: %d Bytes\n", sizeof(char));

	printf("Int:%d Bytes\n", sizeof(int));
	
	printf("Long:%d Bytes\n", sizeof(long));
	
	unsigned long int i = ULONG_MAX;
	printf("Long: maxvalue: %u\n", i);

	return 0;

}



