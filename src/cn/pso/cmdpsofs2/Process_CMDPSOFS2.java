package cn.pso.cmdpsofs2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.read.ReadFile;
import cn.read.Util;

public class Process_CMDPSOFS2 {
    /** 
     * ����Ⱥ 
     */  
    Particle_CMDPSOFS2[] swarm;  //����Ⱥ
    Particle_CMDPSOFS2 global_best;//ȫ�����Ž�  
    int pcount;//���ӵ�����  
    
    List<Particle_CMDPSOFS2> swarm1 = new ArrayList<Particle_CMDPSOFS2>();
    List<Particle_CMDPSOFS2> swarm2 = new ArrayList<Particle_CMDPSOFS2>();
    List<Particle_CMDPSOFS2> swarm3 = new ArrayList<Particle_CMDPSOFS2>();
    
    List<Particle_CMDPSOFS2> LeaderSet;
    List<Particle_CMDPSOFS2> Archive;
    
    public static String name;
    
    /**
     * ��Ԫ����
     * @param LeaderSet
     * @return
     */
    public Particle_CMDPSOFS2 getGbest(List<Particle_CMDPSOFS2> LeaderSet){
    	Particle_CMDPSOFS2 g = new Particle_CMDPSOFS2();
    	if(LeaderSet.size()==1){
    		return LeaderSet.get(0);
    	}
    	int a = Util.randomNum(0, LeaderSet.size(), 1)[0];
    	for(int i=0;i<1;i++){
    		int b = Util.randomNum(0, LeaderSet.size(), 1)[0];
    		if(LeaderSet.get(a).dit>=LeaderSet.get(b).dit){
    			a=b;
    		}
    	}
    	g = LeaderSet.get(a);
		return g;
    }
    
    /**
     * ����
     * @param LeaderSet
     * @return
     */
    public List<Particle_CMDPSOFS2> sortSet(List<Particle_CMDPSOFS2> LeaderSet){
    	Particle_CMDPSOFS2 p;
        for (int i = 0; i < LeaderSet.size(); i++){
            for (int j = i+1; j < LeaderSet.size(); j++){
            	if(LeaderSet.get(i).pbest_fitness[0] < LeaderSet.get(j).pbest_fitness[0]){
            		p = LeaderSet.get(i);
            		LeaderSet.set(i, LeaderSet.get(j));
            		LeaderSet.set(j, p);
            	}
            }
        }
        return LeaderSet;
    }
    
    /**
     * �����Ӽ�����ӵ����������
     * @param LeaderSet
     * @return
     * @throws IOException
     */
    public List<Particle_CMDPSOFS2> crowdingSort(List<Particle_CMDPSOFS2> LeaderSet) throws IOException{
        List<Particle_CMDPSOFS2> temp = LeaderSet;
        Particle_CMDPSOFS2 p;
        for (int i = 0; i < LeaderSet.size()-1; i++){
        	double di;
        	if(i==0){
        		di = Double.POSITIVE_INFINITY;		//��һ�������һ������ӵ������Ϊ�����
        		LeaderSet.get(i).dit = di;
        	}else{
        		di = (temp.get(i+1).pbest_fitness[0]-temp.get(i-1).pbest_fitness[0])/ReadFile.getFeatureNum(name);
        		di+=temp.get(i+1).pbest_fitness[1]-temp.get(i-1).pbest_fitness[1];
        		LeaderSet.get(i).dit = di;
        	}
            for (int j = i+1; j < LeaderSet.size();j++){
            	double dj;
            	if(j==LeaderSet.size()-1){
            		dj = Double.POSITIVE_INFINITY;		//��һ�������һ������ӵ������Ϊ�����
            		LeaderSet.get(j).dit = di;
            	}else{
            		dj = (temp.get(j+1).pbest_fitness[0]-temp.get(j-1).pbest_fitness[0])/ReadFile.getFeatureNum(name);
            		dj+=temp.get(j+1).pbest_fitness[1]-temp.get(j-1).pbest_fitness[1];
            	}
            	if(di > dj){
            		p = LeaderSet.get(i);
            		LeaderSet.set(i, LeaderSet.get(j));
            		LeaderSet.set(j, p);
            	}
            }
        }
        return LeaderSet;
    }
    
    /** 
     * ����Ⱥ��ʼ�� 
     * @param n ���ӵ����� 
     * @throws IOException 
     */ 
	public void init(int n,String name) throws IOException {
    	Process_CMDPSOFS2.name = name;
    	Particle_CMDPSOFS2.dims = ReadFile.getFeatureNum(name); 
    	pcount = n;
        
    	Archive = new ArrayList<Particle_CMDPSOFS2>();
    	LeaderSet = new ArrayList<Particle_CMDPSOFS2>();
        swarm = new Particle_CMDPSOFS2[pcount];  //����Ⱥ
        global_best = new Particle_CMDPSOFS2();
         
        for (int i = 0; i < pcount; ++i) { 
            swarm[i] = new Particle_CMDPSOFS2();
            swarm[i].initial(Particle_CMDPSOFS2.dims);
            swarm[i].evaluate();
            LeaderSet.add(swarm[i]);
        }
        int sp = swarm.length/3;
        for(int i = 0;i<swarm.length;i++){
        	if(i<sp){
        		swarm1.add(swarm[i]);
        	}else if(i>=sp){
        		if(i<2*sp){
        			swarm2.add(swarm[i]);
        		}else{
        			swarm3.add(swarm[i]);
        		}
        	}
        }
        //��LeaderSet�е����Ӱ���λ������
        LeaderSet = sortSet(LeaderSet);
        //��ӵ������,��ӵ����������
        LeaderSet = crowdingSort(LeaderSet);
    }  
	
    /** 
     * ����Ⱥ������ 
     * @throws IOException 
     */  
    public void run(int runtimes,int times) throws IOException {
        int index;  
        int count = 1;
        List<Particle_CMDPSOFS2> record;
        int T=runtimes;
        SortBest_CMDPSOFS2 sb = new SortBest_CMDPSOFS2();
        while (runtimes > 0) {  
            index = -1; 
            
            //ÿ�����Ӹ���λ�ú���Ӧֵ
	        for (int i = 0; i < swarm.length; i++) {
	        	Particle_CMDPSOFS2 c = getGbest(LeaderSet);
	        	swarm[i].gbest = c.pbest;
	        	swarm[i].gbest_fitness = c.pbest_fitness;
	            swarm[i].updatev(); 
	        }
            
            
	        //����
	        int sp = swarm.length/3;
	        for(int i = 0;i<swarm.length;i++){
	        	if(i>=sp){
	        		if(i<2*sp){
	        			swarm[i].mutation(swarm[i].Mr, 2*sp, swarm.length, swarm);
	        		}else{
	        			swarm[i].nonUniMutation(runtimes, T, swarm[i].Mr, swarm);
	        		}
	        	}
	        }
            
	        //����
	        for (int i = 0; i < swarm.length; ++i) {
	            swarm[i].evaluate();
	        }
	        
	        //update LS
	    	 for (int i = 0; i < pcount;i++){
	        	index = -1; 
	        	 for (int j = 0; j < pcount; j++){
	            	if(swarm[j].fitness[0] <= swarm[i].fitness[0]&&swarm[j].fitness[1] < swarm[i].fitness[1]){
	            		index=1;
	            	}else if(swarm[j].fitness[0] < swarm[i].fitness[0]&&swarm[j].fitness[1] <= swarm[i].fitness[1]){
	            		index=1;
	            	}
	            }
	        	for(int k = 0;k<LeaderSet.size();k++){
	        		if(LeaderSet.get(k).fitness[0] == swarm[i].fitness[0]&&LeaderSet.get(k).fitness[1] == swarm[i].fitness[1]){
	        			index=1;
	        		}
	        	}
	            if(index == -1){
	            	double cf = 0;
	            	int cfn = 0;
	            	for(int k=0;k<LeaderSet.size();k++){
	        			if(cf<LeaderSet.get(k).dit){
	        				cf = LeaderSet.get(k).dit;
	        				cfn = k;
	        			}
	            	}
	            LeaderSet.add(swarm[i]);
	                    
                //�����Ӱ���˳������
                LeaderSet = sortSet(LeaderSet);
                //��ӵ������,��ӵ����������
                LeaderSet = crowdingSort(LeaderSet);
                
                if(cf>LeaderSet.get(swarm.length).dit){
                	LeaderSet.remove(cfn);
                }else{
                	LeaderSet.remove(swarm.length);
                }
        	}
        }
        Archive.addAll(LeaderSet);
        record = Archive;
        if(count%10==0 || count==1){
        	sb.getCMDResult(Process_CMDPSOFS2.name, record, count+"-"+times);
        }
       	count++;
        runtimes--;  
        }
    }
    /** 
     * ��ʾ��������� 
     */  
    public void showresult(List<Particle_CMDPSOFS2> list) {  
    	INNER:for(int i = 0 ; i<Archive.size() ; i++){
    		int k=0;
    		for(Particle_CMDPSOFS2 p:list){
    			if(p.fitness[0]==Archive.get(i).fitness[0]){
    				if(p.fitness[1]<=Archive.get(i).fitness[1]){
    					continue INNER;
    				}else{
    					k=list.indexOf(p);
    				}
    			}
    		}
    		if(k>0){
    			list.remove(k);
    			
    		}
    		list.add(Archive.get(i));
    	}
    }  
}
