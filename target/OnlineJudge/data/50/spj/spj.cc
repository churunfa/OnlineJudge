#include<stdio.h>
#include<stdlib.h>
#include<time.h>
int main(int argc, char *args[]) {
    FILE * f_in   = fopen(args[1],"r");
    FILE * f_out  = fopen(args[2],"r");
    FILE * f_user = fopen(args[3],"r");
    srand(time(NULL));
    int ret = 0;
    int n;
    fscanf(f_user,"%d",&n);
    int ans = rand()%2;
    if(ans != n) ret = 1;
    fclose(f_in);
    fclose(f_out);
    fclose(f_user);
    return ret;
}