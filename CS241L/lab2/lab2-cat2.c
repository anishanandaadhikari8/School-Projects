#include <stdio.h>

int charCount = 0;
int lineCount = 0;
void doTheThing(int c);
void summarize(void);


int main(void)
{
	int c;
	while(EOF != (c = getchar()))
	{
		doTheThing(c);
	}
	summarize();
}

void doTheThing(int c)
{
	charCount++;
	lineCount += c == '\n'; /* read: if c == '\n', increment linecount */
	putchar(c);
}

void summarize(void)
{
	printf("There were %d characters in the input\n"
			"There were %d lines in the input\n", charCount, lineCount);
}


