#include <stdio.h>




int main(void){
	char init[81];

	for(int i = 0; i < 81; ++i)
	{
		init[i] = i;
	}

	char twoD[9][9];

	for(int i = 0; i < 9; ++i)
    {
        twoD[i] = {nums[0], nums[1], nums[2], nums[3], nums[4],
            nums[5], nums[6], nums[7], nums[8]};

    }

	for(int i = 0; i < 9; ++i)
	{
		printf("%d, %d, %d, %d, %d, %d, %d, %d, %d,\n", twoD[i][0], twoD[i][1],
			twoD[i][2], twoD[i][3], twoD[i][4], twoD[i][5], twoD[i][6],
			twoD[i][7], twoD[i][8] )
	}

	return 0;


}
