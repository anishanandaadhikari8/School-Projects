/**********************/
/* Anthony Galczak    */
/* 08-31-16           */
/* CS-241 Section 001 */
/**********************/

#include <stdio.h>


int main(void)
{
	int newLines, newWords, newChars, c, c2, state, charsPerLine, wordsPerLine;
	int lineMostChars, lineMostWords, howManyCharsMost, howManyWordsMost;
	int lineNum;	
	newLines = newWords = newChars = c = c2 = state = 0;
	charsPerLine = wordsPerLine = lineMostChars = lineMostWords = 0;
	howManyCharsMost = howManyWordsMost = 0;
	lineNum = 1;
	
	while ((c = getchar()) != EOF)
	{
		/* Printing first line number if just starting */
		if (newChars == 0)
		{
			printf("%d.", lineNum++);
		}
		
		if (c == '\n')
		{
			if ((c2 = getchar()) != EOF)
			{
				printf("[%d;%d]\n%d.", wordsPerLine, charsPerLine, lineNum++);
				ungetc(c2, stdin);
			}
			else
			{
				printf("[%d;%d]", wordsPerLine, charsPerLine, lineNum++);
			}
			
			++newLines;
			wordsPerLine = charsPerLine = 0;
		}
		else
		{	
			++charsPerLine, ++newChars;
			printf("%c", c);
		}

		/* Finding out if inside or outside of a word */
		if (c == ' ' || c == '\n' || c == '\t')
		{
			state = 0;
		}

		else if (state == 0)
		{
			state = 1;
			++newWords, ++wordsPerLine;
		}

		/* Getting the line with most words and line with most chars */
		if (wordsPerLine > howManyWordsMost)
		{
			howManyWordsMost = wordsPerLine;
			lineMostWords = lineNum - 1;
		}
		if (charsPerLine > howManyCharsMost)
		{
			howManyCharsMost = charsPerLine;
			lineMostChars = lineNum - 1;
		}

	}

	/* Printing summary to user */
	printf("\n%d lines, %d words, %d characters\n", 
	newLines, newWords, newChars);	
	printf("With %d, line %d has the most characters\n", 
	howManyCharsMost, lineMostChars);
	printf("With %d, line %d has the most words\n",
	howManyWordsMost, lineMostWords);
	return 0;

}

