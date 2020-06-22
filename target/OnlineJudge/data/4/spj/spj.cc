#include<ctime>
#include<cstdio>
#include<cstdlib>
int main(int argc, char *args[]) {
    FILE * f_in   = fopen(args[1],"r");
    FILE * f_out  = fopen(args[2],"r");
    FILE * f_user = fopen(args[3],"r");
    
    srand((unsigned)time(NULL));
    
    int ret = 1;
    
    int user_out = 0;
    
    fscanf(f_user,"%c",&user_out);
    
    if(user_out == 'A' + rand()%26 ) ret = 0;
    
    fclose(f_in);
    fclose(f_out);
    fclose(f_user);
    return ret;
}