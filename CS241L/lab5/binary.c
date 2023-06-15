/************************************/
/* Anthony Galczak                  */
/* 10-13-16                         */
/* CS-241 Section 001               */
/* Outputs either binary or decimal */
/* number based on command line     */
/* arguments and options 	          */
/************************************/

#include <stdio.h>

/* For strcmp */
#include <string.h>

/* For strtoul */
#include <stdlib.h>

void decimalToBinary(char* charNum, int charLen);
void binaryToDecimal(char* charNum, int charLen);
void printUsage();
void addCommas(char* nums);
char *ltoa(long N, char *str, int base);

/* ltoa is not part of the ansi-c library and is immensely useful for this program */
/* Verified with David Ringo on 10-18-16 that this is okay as I am doing the vast */
/* majority of formatting and work within decimalToBinary */
/* ENTIRE CODE COPIED AND PASTED FROM http://stjarnhimlen.se/snippets/ltoa.c */
#define BUFSIZE (sizeof(long) * 8 + 1)
char *ltoa(long N, char *str, int base)
{
  register int i = 2;
  long uarg;
  char *tail, *head = str, buf[BUFSIZE];

  if (36 < base || 2 > base)
    base = 10;                    /* can only use 0-9, A-Z        */
  tail = &buf[BUFSIZE - 1];           /* last character position      */
  *tail-- = '\0';

  if (10 == base && N < 0L)
  {
    *head++ = '-';
    uarg    = -N;
  }
  else  uarg = N;

  if (uarg)
  {
    for (i = 1; uarg; ++i)
    {
      register ldiv_t r;

      r       = ldiv(uarg, base);
      *tail-- = (char)(r.rem + ((9L < r.rem) ?
                              ('A' - 10L) : '0'));
      uarg    = r.quot;
    }
  }
  else  *tail-- = '0';

  memcpy(head, ++tail, i);
  return str;
}


int main(int argc, char **argv){

	int length = 0;

	/* Finding out which method to call */
	if(strcmp(argv[1], "-b") == 0 || strcmp(argv[1], "-d") == 0)
	{
		if(strcmp(argv[2], "-8") == 0 || strcmp(argv[2], "-16") == 0 ||
		   strcmp(argv[2], "-32") == 0 || strcmp(argv[2], "-64") == 0)
		{
			
			length = strtol(argv[2], &argv[2], 10);
			
			/* Checking to make sure a number was entered */
			if(argv[3] != NULL)
			{
				if(strcmp(argv[1], "-b") == 0)
				{
					binaryToDecimal(argv[3], length);
				}
				else
				{
					decimalToBinary(argv[3], length);
				}
			}
			else
			{
				printf("ERROR: incorrect number of arguments\n");
				printUsage();
			}
		}
		else
		{
			printf("ERROR: argument 2 must be: -8 | -16 | -32 | -64\n");
			printUsage();
		}
	}
	else if((strcmp(argv[1], "-h") == 0))
	{
		printUsage();
	}
	else
	{
		printf("ERROR: argument 1 must be -b | -d\n");
		printUsage();
	}


	return 0;
}


/* Converting the number from decimal to binary */
void decimalToBinary(char* charNum, int charLen)
{
	long decimalOutput = 0;
	int numLength = 0, i = 0;
	char returnString[65];
	
	/* Error check for in case char array contains non-digits */
	while(charNum[i] != 0)
	{
		/* 48 == '0', 57 == '9', 0 == '\0' */
		if(charNum[i] < 48 || charNum[i] > 57)
		{
			printf("ERROR: argument 3 is not a decimal integer\n");
			printUsage();
			exit(0);
		}
		++i;
	}

	decimalOutput = strtoul(charNum, &charNum, 10);

	/* Converting decimalNum to binary ascii representation */
	ltoa(decimalOutput, returnString, 2); 
	
	/* How many digits are in the number */
	numLength = strlen(returnString);
	
	/* How many characters the user asked for */
	charLen = abs(charLen);

	for(i = 0; i < charLen; ++i)
	{
		/* Adding a space every 4 digits */
		if((i % 4) == 0 && i != 0)
		{
			putchar(' ');
		}
		
		/* Deciding whether to add a leading 0 or output the real num */
		if(charLen > i + numLength)
		{
			putchar('0');
		}
		else
		{
			putchar(returnString[i-(charLen - numLength)]);
		}
	}
	printf("\n");
} 


/* Adding commas to a variable length character array */
void addCommas(char* nums)
{
	char commasArray[40];
	int i, numsArrayLen, commasPlaced, initialCommaValue;

	commasPlaced = 0;
	numsArrayLen = strlen(nums);
	
	/* If the mod is 0 then you do not want a leading comma */
	if(numsArrayLen % 3 == 0)
	{
		initialCommaValue = 3;
	}
	else
	{
		initialCommaValue = (numsArrayLen % 3);
	}
	
	/* Adding commas to the new array */
	for(i = 0; i < numsArrayLen; ++i)
	{
		if((i % 3) == initialCommaValue)
		{
			commasArray[i + commasPlaced] = ',';
			commasPlaced++;
		}
		
		commasArray[i + commasPlaced] = nums[i];
	}
	
	/* Null terminating the character array */
	commasArray[i + commasPlaced] = '\0'; 
	
	/* Replacing the old array with the new comma-added array */
	for(i = 0; i < strlen(commasArray); ++i)
	{
		nums[i] = commasArray[i];
	}
	nums[i] = '\0';

}


/* Converting the number from binary to decimal */
void binaryToDecimal(char* charNum, int charLen)
{
	unsigned long decimalNum = 0;
	int i = 0, lengthOfDiff = 0;
	char returnString[30];

	/* Error check for in case number contains digits other than 0 and 1 */
	while(charNum[i] != 0)
	{
		/* 48 == '0', 49 == '1', 0 == '\0' */
		if(charNum[i] != 48 && charNum[i] != 49)
		{
			printf("ERROR: argument 3 is not a binary integer\n");
			printUsage();
			exit(0);
		}
		++i;
	}
	
	/* If the string of numbers is longer than what the user specified for digits
	then those extra digits need to be chopped off */
	charLen = abs(charLen);
	lengthOfDiff = strlen(charNum) - charLen;
	
	if(lengthOfDiff > 0)
	{
		for(i = 0; i < charLen; ++i)
		{
			charNum[i] = charNum[i + lengthOfDiff];
		}
	
		charNum[charLen] = '\0';
	}
	
	/* Base for strtoul is 2 and returns a decimal unsigned long */
	decimalNum = strtoul(charNum, &charNum, 2);
	
	/* Storing the unsigned long decimalNum into a char array */
	sprintf(returnString, "%lu", decimalNum);
	
	/* If there is more than 3 digits, add some commas */
	if(strlen(returnString) > 3)
	{
		addCommas(returnString);
	}


	/* Print width of 3 on 8-bit numbers */
	if(charLen == 8)
	{
		printf("%*s\n", 3, returnString);
	}
	/* Print width of 6 on 16-bit numbers */
	else if(charLen == 16)
	{
		printf("%*s\n", 6, returnString);
	}
	else if(charLen == 32)
	{
		printf("%*s\n", 13, returnString);
	}
	else if(charLen == 64)
	{
		printf("%s\n", returnString);
	}
	
}

/* Printing usage of program to user, if they selected -h or
   made an error while selecting command line options */
void printUsage()
{
	printf("usage:\n");
	printf("./binary OPTION SIZE NUMBER\n");
	printf("  OPTION:\n");
	printf("    -b    NUMBER is binary and output will be in decimal.\n");
	printf("    -d    NUMBER is decimal and output will be in binary.\n");
	printf("\n");
	printf("  SIZE:\n");
	printf("    -8    input is an unsigned 8-bit integer.\n");
	printf("    -16   input is an unsigned 16-bit integer.\n");
	printf("    -32   input is an unsigned 32-bit integer.\n");
	printf("    -64   input is an unsigned 64-bit integer.\n");
	printf("\n");
	printf("  NUMBER:\n");
	printf("    number to be converted.\n");
	printf("\n");
}
