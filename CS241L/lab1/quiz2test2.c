#include <stdio.h>

int main (void){
	int i, n;
	int flag;
	for (n=10; n>1; n--){
	flag = 0;
	for (i=2; i<n; i++){
	if(n % i == 0) flag = 1;
	}
	if (flag == 0) printf("%d ", n);
	}
	printf("\n");
}


