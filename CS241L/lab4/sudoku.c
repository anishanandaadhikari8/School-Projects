/********************************/
/* Anthony Galczak              */
/* 09-18-16                     */
/* CS-241 Section 001           */
/* Solves a given sudoku puzzle */
/********************************/

#include <stdio.h>

/* for memset */
#include <string.h>

int checkGrid(char twoD[9][9], int i, int j);
int checkRowAndCol(char twoD[9][9], int row, int col);
void processArray(char *oneD);
int solve_puzzle(char twoD[9][9], int row, int col);
int numIsGood(char twoD[9][9], int row, int col);


int checkGrid(char twoD[9][9], int i, int j){
	int rowOffset, columnOffset, equalCount, valid, k;
	
	equalCount = rowOffset = columnOffset = k = 0;
	valid = 1;
	rowOffset = (i / 3) * 3;
	columnOffset = (j / 3) * 3;


	for(k = rowOffset; k < 3 + rowOffset; ++k)
	{
		
		/* 3 if statements for checking 3 columns */
		if(twoD[k][columnOffset] == twoD[i][j])
		{
			equalCount++;
		}
		if(twoD[k][columnOffset + 1] == twoD[i][j])
		{
			equalCount++;
		}
		if(twoD[k][columnOffset + 2] == twoD[i][j])
		{
			equalCount++;
		}
			
		
	}

	/* Because I'm checking every cell in 3x3, allowing one equal
	space before throwing an error */
	if(equalCount > 1)
	{
		valid = 0;
	}

	/* 0 is not valid, 1 is valid */
	return valid;

}


int checkRowAndCol(char twoD[9][9], int row, int col){
	int i, equalCountRow, equalCountCol, valid;

	equalCountRow = equalCountCol = 0;
	valid = 1;
	
	/* Checking row */
	for(i = 0; i < 9; ++i)
	{
		if(twoD[row][i] == twoD[row][col])
		{
			equalCountRow++;
		}
	}

	/* Checking col */
	for(i = 0; i < 9; ++i)
	{
		if(twoD[i][col] == twoD[row][col])
		{
			equalCountCol++;
		}
	}

	if(equalCountRow > 1 || equalCountCol > 1)
	{
		valid = 0;
	}

	/* 0 is not valid, 1 is valid */
	return valid;
}


void processArray(char *oneD){
	char twoD[9][9];
	int i, j, k, errored, isValid, tmp;
	errored = tmp = 0;
	isValid = 1;
	
	/* Assigning 1D array to 2D array */	
	for (i = 0; i < 9; ++i)
	{
		for(j = 0; j < 9; ++j)
		{
			twoD[i][j] = oneD[(i*9) + j];
		}
		
	}

	/* Checking for problems with array, nums in same column or row and 3x3 */
	for(i = 0; i < 9; ++i)
	{
		for(j = 0; j < 9; ++j)
		{
			for(k = j + 1; k < 9; ++k)
			{
				/* Row check */
				if(twoD[i][j] != '.' && twoD[i][k] != '.')
				{
					if(twoD[i][j] == twoD[i][k])
					{
						errored = 1;
					}
				}
				
				/* Column check */
				if(twoD[j][i] != '.' && twoD[k][i] != '.')
				{
					if(twoD[j][i] == twoD[k][i])
					{
						errored = 1;
					}
				
				}

			}

			/* Checking 3x3 grid */
			if(twoD[i][j] != '.')
			{
				if(checkGrid(twoD, i, j) == 0)
				{
					errored = 2;
				}
			}
			if(isValid == 0)
			{
				errored = 2;
			}

		}

	}
	

	if(errored == 1)
	{
		printf("Error");
		/*printf("Error - Column or Row");*/
	}
	else if(errored == 2)
	{
		printf("Error");
		/*printf("Error - 3x3");*/
	}

	/* Solving the sudoku puzzle after exceptions have been caught */
	else
	{
		if(solve_puzzle(twoD, 0, 0) == 1)
		{
			
			/* Printing solved puzzle */		
			for(i = 0; i < 9; ++i)
			{
				for(j = 0; j < 9; ++j)
				{
					printf("%c", twoD[i][j]);
				}
			}
		}
		else
		{
			printf("No solution");
		}
		

	}
	
}

int solve_puzzle(char twoD[9][9], int row, int col){
	int i;

	/* If the 9th row has been hit then we solved the puzzle */
	if(row == 9)
	{
		return 1;
	}
	
	if(twoD[row][col]  == '.'){
				
		for(i = '1'; i <= '9'; ++i)
		{
			twoD[row][col] = i;

			if(numIsGood(twoD, row, col) == 1)
			{		
					

				/* If on the 8th column then iterate down rows */
				if (col == 8)
				{
					if (solve_puzzle(twoD, row + 1, 0) == 1)
					{
						return 1;
					}
				}
				else
				{
					if(solve_puzzle(twoD, row, col + 1) == 1)
					{
						return 1;
					}
				}
				
			}

		}

		/* If this loop leaves the recursion then reassign a blank value, i.e. . */
		twoD[row][col] = '.';
	}

	/* Value is not . */
	else{
			
			/* If on the 8th column then iterate down rows */
			if (col == 8)
			{
				if (solve_puzzle(twoD, row + 1, 0) == 1)
				{
					return 1;
				}
			}
			else
			{
				if(solve_puzzle(twoD, row, col + 1) == 1)
				{
					return 1;
				}
			}

		
	}


	/* No solution */
	return 0;


}


int numIsGood( char twoD[9][9], int row, int col){
	int check1, check2;

	check1 = checkGrid(twoD, row, col);
	check2 = checkRowAndCol(twoD, row, col);

	/* If both checks are valid then return true */
	if(check1 && check2)
	{
		return 1;
	}
	else
	{
		return 0;
	}

	/* 0 is not valid, 1 is valid */
}



int main(void){

	/* Character array for holding the 81 elements from the input */
	char initial[81];
	
	int c, errored, charsPerLine, eof;

	c = errored = charsPerLine = eof = 0;

	/* Reading the whole file line by line*/
	while(eof == 0)
	{
		/* Processing characters */
		while((c = getchar()) != '\n' && c != EOF)
		{
			
			/* Printing each char regardless of it throws an error */
			printf("%c", c);

			/* Error if there is more than 81 chars per line */
			if (charsPerLine > 80)
			{
				errored = 1;
			}

			/* If errored is false then process the chars into the array */
			if (errored == 0)
			{
				/* Checking if the chars are between 0 and 9 or a . */
				if((c == '.') || (c >= '0' && c <= '9'))
				{
					/* Filling the array with each character */
					initial[charsPerLine] = c;	
					++charsPerLine;
				}
				else
				{
					errored = 1;
				}
			}


		}

		/* If end of file was hit, then exit program */
		if (c == EOF)
		{
			eof = 1;
			break;
		}

		/* If the line has less than 81 characters then throw error */
		if(charsPerLine <= 80)
		{
			errored = 1;
		}
		
		if(errored == 1)
		{
			/*printf("\nError - Line or Char Type\n\n");*/
			printf("\nError\n\n");
		}
		else
		{
			printf("\n");	
			processArray(initial);
			printf("\n\n");
		}
		
		/* Resetting variables to continue onto the next line */
		charsPerLine = 0;
		errored = 0;
		
		/* Clearing character array */
		memset(&initial[0], 0, sizeof(initial));

	} 

	return 0;
}


