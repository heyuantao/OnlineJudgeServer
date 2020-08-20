#include<stdio.h>
int main(){
    int n,i,s[100],t;
    while (scanf("%d",&n)!=EOF&&n!=0){
        for(i=1;i<5;i++){
           s[i]=i;
        }
        if(n>=5){
           for(i=5;i<=n;i++){   //第四年后
              s[i]=s[i-3]+s[i-1];
           }
        }
        printf("%d\n",s[n]);
    }
    return 0;
}