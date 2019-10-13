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
	//���ԣ�������·+����վ�㣩
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
	
	//subway�������
	public List<subway> map;
	
	public List<subway> getMap() {
		return map;
	}
	public void setMap(List<subway> map) {
		this.map = map;
	}
	
	//վ�����
	public static  int sta_num=0;
	//Ϊվ�㸳id
	public static HashMap<String,Integer> sta_id=new HashMap<String,Integer>();
	public static HashMap<Integer,String> sta_id2=new HashMap<Integer,String>();
	//��¼�ڵ�֮��ľ���
	public static int[][] distance=new int[500][500];
	//Ϊ�����߸�id
	public static HashMap<String,Integer> sub_id=new HashMap<String,Integer>();
	public static HashMap<Integer,String> sub_id2=new HashMap<Integer,String>();
	//��¼վ�����ڵ�����·
	public static int[][] belong=new int[1000][1000];
	
	//��ȡ����
	public List<subway> ReadData(String name){
		 //��ʼ�������ڵ��Ƿ����ӵ��ڽӾ���
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
	             BufferedReader br = new BufferedReader(reader) // ����һ�����������ļ�����ת�ɼ�����ܶ���������
	        ) {
	            String cont;
	            int line_count=0;//����
	            subway m=new subway();
	            int index=1;
	            // һ�ζ���һ������
	            while ((cont = br.readLine()) != null) {
	            	if(line_count%2==0) {
	            		m.setSub_name(cont);
	            		
	            		//Ϊ�����߸�id
	            		sub_id.put(cont, index);
	            		sub_id2.put(index, cont);
	            		
	            	}	
	            	else {
	            		boolean first=true;
	            		String pre_sta=null;
	            		List<String> sta_name=new ArrayList<String>();//����������վ������
	            		
	            		String [] arr = cont.split("\\s+");
	                    for(String ss : arr){
	                    	sta_name.add(ss); 
	                    	
	                    	if(sta_id.get(ss)==null) {
	                    		sta_id.put(ss, sta_num);//��id��
	                    		sta_id2.put(sta_num, ss);
	                    		
	                    	}                    	
	                    	belong[sta_id.get(ss)][index]=1;     			
	                    	//System.out.print(sta_id.get(ss));
	                    	if(first) {
	                    		pre_sta=ss;//����ǰһ��վ��
	                    		first=false;
	                    	}
	                    	else {
	                    		distance[sta_id.get(pre_sta).intValue()][sta_id.get(ss).intValue()]=1;
	                    		distance[sta_id.get(ss).intValue()][sta_id.get(pre_sta).intValue()]=1;//�����ڽڵ������Ϊ1
	                    		pre_sta=ss;
	                    	}
	                    	
	                    	sta_num++;
	                    }
	                    index++;
	                    //�Ի���·�ߵ���βվ����
	                    if(m.getSub_name().equals("2����")) {
	                    	int firstid=sta_id.get("��ˮ̶");
	                    	int endid=sta_id.get("��ֱ��");
	                    	distance[firstid][endid]=1;
	                    	distance[endid][firstid]=1;
	                    }
	                    if(m.getSub_name().equals("10����")) {
	                    	int firstid=sta_id.get("�͹�");
	                    	int endid=sta_id.get("����Ӫ");
	                    	distance[firstid][endid]=1;
	                    	distance[endid][firstid]=1;
	                    }
	                    
	                    	
	                    m.setStation_name(sta_name);
	            	}  
	            	line_count++;
	            	if(line_count%2==0) {
	            		map.add(m);
	            		m=new subway();//�����ṹ	
	            	}	
	            }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		return map;
	}
	
	//����1�����ָ������������վ��
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
				//��·��д��station.txt�ļ���
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
			System.out.println("������·��������"); 
		
		
	}
	
	//����2��������·��
	public ArrayList<Object> floyd(String start,String end,String place)
    {
		ArrayList<Object> arrayList=new ArrayList<Object>();
		
		//��վ���Ӧ��id
		if(sta_id.get(start)==null) {
			System.out.println("��ʼվ��������");
			return null;
		}
			
		if(sta_id.get(end)==null) {
			System.out.println("�յ�վ��������");
			return null;
		}
			
		int startid=sta_id.get(start);
		int endid=sta_id.get(end);
		
        //��ʼ��path����
		int[][] path=new int[sta_num][sta_num];
        for(int row=0;row<sta_num;row++)
            for(int col=0;col<sta_num;col++)
                path[row][col]=row;
        
        //�����·��
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
                if(i==startid&&j==endid) //���·��
                {
                    Stack<Integer>pathrout=new Stack<>();//ѹջ
                    pathrout.push(endid);//�����յ�id
                    int k=j;
                    do{
                        k=path[i][k];
                        Integer temp_k=new Integer(k);
                        pathrout.push(temp_k.intValue());
                    }while(k!=i);
                    
                    
					//��ջ
                    int pre=pathrout.peek();//ǰһ��վ��
                    line+=sta_id2.get(pathrout.peek())+"\r\n" ;
                    pathrout.pop();

                    int length=pathrout.size();
                    //�ж��Ƿ��л���
                    for(int t=0;t<length;t++) {
                    	int mid=pathrout.peek();//�м�վ��
                        line +=  sta_id2.get(pathrout.peek())+"\r\n";
                        pathrout.pop();
                        if(pathrout.size()!=0) {
                        	int fin=pathrout.peek();//��һ��վ��
                        	for(int h=1;h<=26;h++)
                        	if(belong[fin][h]==1&&belong[mid][h]==1) {
                        		if(h==sub_id.get("��ͨ��")&&pre==sta_id.get("�Ļ�")) {
                        			line +=  "��ͨ��"+"\r\n";
                        			break;
                        		}
                        		
                        		if(belong[pre][h]!=1&&h!=sub_id.get("��ͨ��")) {
                        			line +=  sub_id2.get(h)+"\r\n";
                        		}
                        	}		
                        pre=mid;
                        }
                         
                    }
                    break;
                }
            }
        
      //�����·��д��routine.txt�ļ���
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
		//����ģ��
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
