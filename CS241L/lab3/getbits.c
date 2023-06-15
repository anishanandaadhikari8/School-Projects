/*******************************/
/* Anthony Galczak             */
/* 09-08-16                    */
/* CS-241 Section 001          */
/* Reads bits from a given int */
/*******************************/

#include <stdio.h>
#include <stdlib.h>

unsigned getbits(unsigned x, int p, int n)
{
	return (x >> (p+1-n)) & ~(~0 << n);
}

int processInt()
{
	char buf[11];
	int c, intLen, j;
	unsigned int output;
	c = intLen = output = 0;

	/* Clearing the buffer for next use */
	for(j = 0; j < 11; ++j)
	{
		buf[j] = '\0';
	}

	/* Getting the next char and building the char array */
	while((c = getchar()) != ',' && c != '\n')
	{
		buf[intLen] = c;
		++intLen;
	}
	
	/* Adding null terminator to make this buffer a string */
	buf[intLen + 1] = '\0';

	/* Building integer from the c string */
	output = atoi(buf);

	/* If the character is larger than 10 characters (max character length of
	   an int, then return sentinel value */
	if(intLen > 10)
	{
		output = 0;
	}

	/* If int length is equal to the max length of an int then find out if this
	   number is overflowing */
	else if (intLen == 10)
	{
		/* If the first digit is greater than 4, it will overflow, but wouldn't
		   be caught by checking if the parsed int is less than 10 digits */
		if ((buf[0] - '0') > 4)
		{
			output = 0;
		}
		/* If the string is 10 chars, but somehow the parsed number is less
		   than 10 digits, then it must've overflowed */
		else if (output < 1000000000)
		{
			output = 0;
		}

	}

	return output;

}

int main(void){

	int num1, num2, num3, c2, bit;

	/* Parsing stdin characters until hitting EOF */
	while((c2 = getchar()) != EOF)
	{
		/* Putting back the char that getchar took away */
		ungetc(c2, stdin);
		num1 = processInt();
		num2 = processInt();
		num3 = processInt();

		
		/* if the 3rd number is 2 larger than the 2nd number then
		   you are requesting too many bits */
		if (num3 >= (num2 + 2))
		{
			printf("Error: too many bits requested from position\n");			
		}

		/* If trying to push more than 4 bytes worth of distance then throw
		   error */
		else if (num2 > 31)
		{
			printf("Error: position out of range\n");	
		}

		/* If 3rd number is larger than 31 then you are trying to grab too many
		   bits into "undefined" range, throw error */
		else if (num3 > 31)
		{
			printf("Error: number of bits out of range\n");
		}

		/* If processInt has returned a 0 then it has deduced that you have
		   overflowed the int data type and returned a 0, throw error */
		else if (num1 == 0 || num2 == 0 || num3 == 0)
		{
			printf("Error: value out of range\n");
		}

		/* Your numbers are good and will get processed as a getbits line */
		else
		{
			bit = getbits(num1, num2, num3);	
			printf("getbits(x=%u, p=%u, n=%u) = %d\n", num1, num2, num3, bit);
		}

	}

	return 0;
}


