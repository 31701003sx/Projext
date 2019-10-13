import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;


public class subway {
	//属性（地铁线路+所含站点）
	String sub_name;
	List<String> station_name;
	public String getSub_name() {
		return sub_name;
	}
	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}
	public List<String> getStation_name() {
		return station_name;
	}
	public void setStation_name(List<String> station_name) {
		this.station_name = station_name;
	}
	
	//subway类的数组
	public List<subway> map;
	
	public List<subway> getMap() {
		return map;
	}
	public void setMap(List<subway> map) {
		this.map = map;
	}
	
	//站点计数
	public static  int sta_num=0;
	//为站点赋id
	public static HashMap<String,Integer> sta_id=new HashMap<String,Integer>();
	public static HashMap<Integer,String> sta_id2=new HashMap<Integer,String>();
	//记录节点之间的距离
	public static int[][] distance=new int[500][500];
	//为地铁线赋id
	public static HashMap<String,Integer> sub_id=new HashMap<String,Integer>();
	public static HashMap<Integer,String> sub_id2=new HashMap<Integer,String>();
	//记录站点所在地铁线路
	public static int[][] belong=new int[1000][1000];
	
	//读取数据
	public List<subway> ReadData(String name){
		 //初始化各个节点是否连接的邻接矩阵
		for(int k=0;k<500;k++)
			for(int h=0;h<500;h++) {
				if(k==h)
					distance[k][h]=0;
				else
					distance[k][h]=10000;
			}
		
		
		List<subway> map=new ArrayList<subway>();
		
		String pathname = "C:\\Users\\Administrator\\Desktop\\"+name;
		try (FileReader reader = new FileReader(pathname);
	             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
	        ) {
	            String cont;
	            int line_count=0;//行数
	            subway m=new subway();
	            int index=1;
	            // 一次读入一行数据
	            while ((cont = br.readLine()) != null) {
	            	if(line_count%2==0) {
	            		m.setSub_name(cont);
	            		
	            		//为地铁线赋id
	            		sub_id.put(cont, index);
	            		sub_id2.put(index, cont);
	            		
	            	}	
	            	else {
	            		boolean first=true;
	            		String pre_sta=null;
	            		List<String> sta_name=new ArrayList<String>();//地铁线所含站点名称
	            		
	            		String [] arr = cont.split("\\s+");
	                    for(String ss : arr){
	                    	sta_name.add(ss); 
	                    	
	                    	if(sta_id.get(ss)==null) {
	                    		sta_id.put(ss, sta_num);//赋id号
	                    		sta_id2.put(sta_num, ss);
	                    		
	                    	}                    	
	                    	belong[sta_id.get(ss)][index]=1;     			
	                    	//System.out.print(sta_id.get(ss));
	                    	if(first) {
	                    		pre_sta=ss;//保存前一个站点
	                    		first=false;
	                    	}
	                    	else {
	                    		distance[sta_id.get(pre_sta).intValue()][sta_id.get(ss).intValue()]=1;
	                    		distance[sta_id.get(ss).intValue()][sta_id.get(pre_sta).intValue()]=1;//将相邻节点距离置为1
	                    		pre_sta=ss;
	                    	}
	                    	
	                    	sta_num++;
	                    }
	                    index++;
	                    //对环形路线的首尾站相连
	                    if(m.getSub_name().equals("2号线")) {
	                    	int firstid=sta_id.get("积水潭");
	                    	int endid=sta_id.get("西直门");
	                    	distance[firstid][endid]=1;
	                    	distance[endid][firstid]=1;
	                    }
	                    if(m.getSub_name().equals("10号线")) {
	                    	int firstid=sta_id.get("巴沟");
	                    	int endid=sta_id.get("火器营");
	                    	distance[firstid][endid]=1;
	                    	distance[endid][firstid]=1;
	                    }
	                    
	                    	
	                    m.setStation_name(sta_name);
	            	}  
	            	line_count++;
	            	if(line_count%2==0) {
	            		map.add(m);
	            		m=new subway();//地铁结构	
	            	}	
	            }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		return map;
	}
	
	//功能1：输出指定地铁的所含站点
	public void Print(String sub,List<subway> map,String place) {
		String way=new String();
		
		int i=0,j=0;
		for(i=0;i<map.size();i++) {
			subway m=new subway();
			m=map.get(i);
			if(sub.equals(m.getSub_name())) {
				for(j=0;j<m.getStation_name().size();j++) {
					way=way+m.getStation_name().get(j)+" ";
				}
				//将路线写入station.txt文件中
				char[] text = way.toCharArray();
				File file = new File(place);
				FileWriter out;
				try {
					out = new FileWriter(file);
					out.write(text);
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}	
		}
		if(j==0)
			System.out.println("地铁线路输入有误！"); 
		
		
	}
	
	//功能2：输出最短路径
	public ArrayList<Object> floyd(String start,String end,String place)
    {
		ArrayList<Object> arrayList=new ArrayList<Object>();
		
		//找站点对应的id
		if(sta_id.get(start)==null) {
			System.out.println("起始站输入有误！");
			return null;
		}
			
		if(sta_id.get(end)==null) {
			System.out.println("终点站输入有误！");
			return null;
		}
			
		int startid=sta_id.get(start);
		int endid=sta_id.get(end);
		
        //初始化path矩阵
		int[][] path=new int[sta_num][sta_num];
        for(int row=0;row<sta_num;row++)
            for(int col=0;col<sta_num;col++)
                path[row][col]=row;
        
        //找最短路径
        for(int k=0;k<sta_num;k++) {
        	for(int i=0;i<sta_num;i++) 
                for(int j=0;j<sta_num;j++)
                    if(distance[i][j]>distance[i][k]+distance[k][j])
                    {
                        distance[i][j]=distance[i][k]+distance[k][j];
                        path[i][j]=path[k][j];     
                        
                    }                
        }
            
        
        int dis=distance[startid][endid]+1;
        int  sumSite=distance[startid][endid];
        String line=dis+"\r\n";

        for(int i=0;i<sta_num;i++)
            for(int j=0;j<sta_num;j++)
            {
                if(i==startid&&j==endid) //输出路径
                {
                    Stack<Integer>pathrout=new Stack<>();//压栈
                    pathrout.push(endid);//加入终点id
                    int k=j;
                    do{
                        k=path[i][k];
                        Integer temp_k=new Integer(k);
                        pathrout.push(temp_k.intValue());
                    }while(k!=i);
                    
                    
					//弹栈
                    int pre=pathrout.peek();//前一个站点
                    line+=sta_id2.get(pathrout.peek())+"\r\n" ;
                    pathrout.pop();

                    int length=pathrout.size();
                    //判断是否有换乘
                    for(int t=0;t<length;t++) {
                    	int mid=pathrout.peek();//中间站点
                        line +=  sta_id2.get(pathrout.peek())+"\r\n";
                        pathrout.pop();
                        if(pathrout.size()!=0) {
                        	int fin=pathrout.peek();//后一个站点
                        	for(int h=1;h<=26;h++)
                        	if(belong[fin][h]==1&&belong[mid][h]==1) {
                        		if(h==sub_id.get("八通线")&&pre==sta_id.get("四惠")) {
                        			line +=  "八通线"+"\r\n";
                        			break;
                        		}
                        		
                        		if(belong[pre][h]!=1&&h!=sub_id.get("八通线")) {
                        			line +=  sub_id2.get(h)+"\r\n";
                        		}
                        	}		
                        pre=mid;
                        }
                         
                    }
                    break;
                }
            }
        
      //将最短路线写入routine.txt文件中
		char[] text = line.toCharArray();
		File file = new File(place);
		FileWriter out;
		try {
			out = new FileWriter(file);
			out.write(text);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
         arrayList.add(line);
            arrayList.add(sumSite);
        return arrayList;

    }
	
	
	
	public static void main(String args[]) {
		subway m=new subway();
		//功能模块
        if("-map".equals(args[0])) {
        	String name=args[1];
        	m.ReadData(name);
        }
        else if("-a".equals(args[0])) {
        	String sub=args[1];
        	String name=args[3];
        	String place=args[5];
        	m.Print(sub, m.ReadData(name),place);
        }
        else if("-b".equals(args[0])) {
        	String start=args[1];
        	String end=args[2];
        	String name=args[4];
        	String place=args[6];
        	m.ReadData(name);
        	m.floyd(start, end,place);
        }
	}
	
}
