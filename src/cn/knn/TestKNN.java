package cn.knn;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
/**
 * KNN算法测试�?
 */
public class TestKNN {
	
	/**
	 * 从数据文件中读取数据
	 * @param datas 存储数据的集合对�?
	 * @param path 数据文件的路�?
	 */
	public void read(List<List<Double>> datas, String path){
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String data = br.readLine();
			List<Double> l = null;
			while (data != null) {
				String t[] = data.split(" ");
				l = new ArrayList<Double>();
				for (int i = 1; i < t.length; i++) {
					double temp = Double.parseDouble(t[i].split(":")[1]);
					l.add(temp);
				}
				l.add(Double.parseDouble(t[0]));
				datas.add(l);
				data = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 程序执行入口
	 * @param args
	 */
	public double runKnn(String trafilePath,String testfilePath) {
		TestKNN t = new TestKNN();
		try {
			List<List<Double>> datas = new ArrayList<List<Double>>();
			List<List<Double>> testDatas = new ArrayList<List<Double>>();
			t.read(datas, trafilePath);
			t.read(testDatas, testfilePath);
			KNN knn = new KNN();
			int count = 0;
			for (int i = 0; i < testDatas.size(); i++) {
				List<Double> test = testDatas.get(i);
				double judge = test.get(test.size()-1);
				test.remove(test.size()-1);
				double result = Math.round(Float.parseFloat((knn.knn(datas,test,3))));
				if(judge==result){
					count++;
					
				}
			}
			
			double accuracy = (double)count/testDatas.size();
//			System.out.println(accuracy);
			return accuracy;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
