package cn.pso.cmdpsofs2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Test_CMDPSOFS2 {

	public static void main(String[] args) throws IOException {
		Process_CMDPSOFS2 pso = new Process_CMDPSOFS2();
		List<Particle_CMDPSOFS2> list = new ArrayList<Particle_CMDPSOFS2>();
		SortBest_CMDPSOFS2 sb = new SortBest_CMDPSOFS2();
		for(int i=0;i<30;i++){
			pso.init(30,"german");
			pso.run(500,i);
			pso.showresult(list);
			System.out.println("CMDPSOFS2:"+Process_CMDPSOFS2.name+"第"+(i+1)+"次测试完成");
		}
		sb.getCMDResult(Process_CMDPSOFS2.name,list,"All");
	}

}
