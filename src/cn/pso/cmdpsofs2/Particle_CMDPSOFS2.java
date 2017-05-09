package cn.pso.cmdpsofs2;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.knn.TestKNN;
import cn.read.ReadFile;
import cn.read.Util;

public class Particle_CMDPSOFS2 {

    public double[] pos;//粒子的位置，求解问题多少维，则此数组为多少维  
    public double[] v;//粒子的速度，维数同位置  
    public double Mr; //变异率
    public double dit;
    
    
    public double[] fitness;//粒子的适应度  0为位置 1为速度
    public double[] pbest;//粒子的历史最好位置  0为位置 1为速度
    public double[] gbest;//所有粒子找到的最好位置  0为位置 1为速度
    
    
    
    public double randnum1;
    public double randnum2;
    
    public static int dims;  //维度
    public double w;  
    public double c1;  
    public double c2;  
    
    public double[] gbest_fitness;//历史最优解  
    public double[] pbest_fitness;//历史最优解  

    /** 
     * 初始化粒子 
     * @param dim 表示粒子的维数 
     * @throws IOException 
     */  
    public void initial(int dim) throws IOException {  
        pos = new double[dim]; 
        v = new double[dim];
        pbest = new double[dim];  
        dims = dim;
        for(int i=0;i<pos.length;i++){
        	pos[i]=Util.rand(0, 1);
        	pbest[i] = pos[i];										//初始化粒子的个体最优//初始化粒子的速度
        	v[i] = Util.rand(-0.6, 0.6); 
        }	                        
        fitness = new double[2];					//适应值，这里有2个，第一个是特征数，第二个是错误率
        pbest_fitness = new double[2];
        pbest_fitness[0] = ReadFile.getFeatureNum(Process_CMDPSOFS2.name)+1;
        pbest_fitness[1] = 1;
        gbest_fitness = new double[2];
        gbest = new double[dim];
        
        Mr = 1/pos.length;
        randnum1 = Util.rand(0,1);
        randnum2 = Util.rand(0,1);
        c1 = Util.rand(1.5,2.0);
        c2 = Util.rand(1.5,2.0);
        w = Util.rand(0.1,0.5);
        dit = 0;
    }  
    /** 
     * 评估函数值,同时记录历史最优位置 
     * @throws IOException 
     */  
    public void evaluate() throws IOException {

    	List<String> choose = new ArrayList<String>();
    	int j=1;
    	for(int i=0;i<pos.length;i++){
    		if(pos[i]>0.6){
    			choose.add(String.valueOf(j));
    		}
    		j++;
    	}
    	
		ReadFile rf = new ReadFile();
		rf.getFile(choose, "dataset\\Alltra--"+Process_CMDPSOFS2.name, "tra");
		rf.getFile(choose, "dataset\\Alltest--"+Process_CMDPSOFS2.name, "test");
		
		Double accuracy = new TestKNN().runKnn("tra", "test");
		Double errorRate = 1-accuracy;
    	
		fitness[0] = choose.size();
        fitness[1] = errorRate;
		if(choose.size()==0){
			fitness[1] = 1;
		}
        //更新个体最优解
        if (fitness[0] < pbest_fitness[0]&&fitness[1] < pbest_fitness[1]) {
        	pbest_fitness[0] = fitness[0];
        	pbest_fitness[1] = fitness[1];
        	System.arraycopy(pos, 0, pbest, 0, pos.length);
        }else if(fitness[0] == pbest_fitness[0]&&fitness[1] < pbest_fitness[1]){
        	pbest_fitness[0] = fitness[0];
        	pbest_fitness[1] = fitness[1];
        	System.arraycopy(pos, 0, pbest, 0, pos.length);
        }else if(fitness[0] < pbest_fitness[0]&&fitness[1] == pbest_fitness[1]){
        	pbest_fitness[0] = fitness[0];
        	pbest_fitness[1] = fitness[1];
        	System.arraycopy(pos, 0, pbest, 0, pos.length);
        }
    }  
    /** 
     * 更新速度和位置 
     * @throws IOException 
     */  
    public void updatev() throws IOException {
        for (int i = 0; i < pos.length; i++) {
            v[i] = w * v[i] + c1 * randnum1 * (pbest[i] - pos[i])  
                    + c2 * randnum2 * (gbest[i] - pos[i]);  
            if (v[i] > 0.6) {  
                v[i] = Util.rand(-0.6, 0.6);  
            }  
            if (v[i] < -0.6) {  
                v[i] = Util.rand(-0.6, 0.6); 
            }  
            pos[i] = (pos[i] + v[i]);  
            if (pos[i] > 1) {
                pos[i] = Util.rand(0, 1);
            }
            if (pos[i] < 0) {  
                pos[i] = Util.rand(0, 1);
            }
        }
    }
    
    public void nonUniMutation(int t,int T,double Mr,Particle_CMDPSOFS2[] swarm){
    	int b=3;
    	for(int i=0;i<pos.length;i++){
    		double r = Util.rand(0, 1);
    		double a = Util.rand(0, 1);
    		if (a<=Mr) {
    	    	double posi = Util.rand(0, 1);
    	    	double y ;
    	    	int e;
    	    	if (posi>0.5){
    	    		y=1-pos[i];
    	    		e=0;
    	    	}else{
    	    		y=pos[i]-0;
    	    		e=1;
    	    	}
    	    	double k = Math.pow(1-(t/T), b);
    	    	double del =  y*(1-(Math.pow(r, k)));
    	    	
    	    	if (e==0){
    	    		pos[i]=pos[i]+del;
    	    	}else{
    	    		pos[i]=pos[i]-del;
    	    	}
    	    	
    		}
    	}
    }
    
    
    public void mutation(double Mr,int min,int max,Particle_CMDPSOFS2[] swarm) throws IOException{
    	double a = Util.rand(0, 1);
    	if (a<=Mr) {
    		double F=0.5;
        	Particle_CMDPSOFS2 mu = new Particle_CMDPSOFS2();
        	mu.initial(dims);
        	for(int i=0;i<pos.length;i++){
            	int x1 = Util.randomNum(min,max,1)[0]; 
            	int x2 = Util.randomNum(min,max,1)[0]; 
            	int x3 = Util.randomNum(min,max,1)[0]; 
            	while(x1 == i){
            		x1 = Util.randomNum(min,max,1)[0]; 
            	}
            	while(x1 == x2 || x2 == i){
            		x2 = Util.randomNum(min,max,1)[0]; 
            	}
            	while(x1 == x3 || x3 == i){
            		x3 = Util.randomNum(min,max,1)[0]; 
            	}

            	double v = swarm[x3].pos[i]+(F*(swarm[x1].pos[i]-swarm[x2].pos[i]));
            	double a1 = Util.rand(0, 1);
            	if(v > 1||v < 0){
            		if(a1<0.5){
            			v = pos[i];					//越界处理
            		}else{
            			v = 0.5;
            		}
            		
            	}
            	mu.pos[i]=v;
        	}
        	mu.evaluate();
        	if(mu.fitness[0]<=fitness[0]&&mu.fitness[1]<=fitness[1]){
    			pos=mu.pos;
        	}
		}
    }
}
