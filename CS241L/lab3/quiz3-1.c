#include <stdio.h>

int foo(int n)
{
	n = 2*n;
	printf("foo: n=%d ", n);
	return n;
}

void main(void)
{
	int n=5;
	printf("main: foo(n)=%d, n=%d\n", foo(n),n);
}




