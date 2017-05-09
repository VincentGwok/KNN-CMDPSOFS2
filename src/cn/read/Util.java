package cn.read;

import java.util.Random;

public class Util {
    /** 
     * 返回low—uper之间的数 (double)
     * @param low 下限 
     * @param uper 上限 
     * @return 返回low—uper之间的数 
     */  
    public static double rand(double low, double uper) {
    	Random rnd = new Random();
        return rnd.nextDouble() * (uper - low) + low;  
    }
    
    /**
     * 返回min和max之间的n个随机数
     * @param min
     * @param max
     * @param n
     * @return
     */
    public static int[] randomNum(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {  
               return null;  
           }  
        int[] result = new int[n];  
        int count = 0;  
        while(count < n) {  
            int num = (int) (Math.random() * (max - min)) + min;  
            boolean flag = true;  
            for (int j = 0; j < n; j++) {  
                if(num == result[j]){  
                    flag = false;
                    break;
                }
            }  
            if(flag){ 
            	result[count] = num;
                count++;  
            }  
        }  
        return result;  
    }  
}
