package cn.zj.easynet.rabbitmq.complex.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;

//import com.chinatelecom.yiliao.common.protocol.TeamNotify;

import cn.zj.easynet.rabbitmq.complex.EventControlConfig;
import cn.zj.easynet.rabbitmq.complex.EventProcesser;
import cn.zj.easynet.rabbitmq.complex.exception.SendRefuseException;
import cn.zj.easynet.rabbitmq.complex.impl.DefaultEventController;

public class TestMain {

	public void testMain() throws SendRefuseException, Exception {
		// TODO Auto-generated method stub
		RbmqUtil rbmqUtil = RbmqUtil.getInstance();
		
		//消费者
/*		rbmqUtil.getMsg(new EventProcesser() {
			@Override
			public void process(Object e) {//消费程序这里只是打印信息
				System.out.println("**********开始****"+ Thread.currentThread().getName() +"*******************");
				if(e instanceof People){
					People people = (People)e;
					System.out.println(people.getSpouse());
					System.out.println(people.getFriends());
				}else{
					System.out.println(e);
				}
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
					System.out.println("**********结束***********************");
					System.out.println();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});*/
		
		//生产者
		testSendMsg();
	}
	
//	@Before
	public void init() throws IOException{
	}
	
//	@Test
	//取数据
	public void testGetMsg() throws IOException{
		RbmqUtil rbmqUtil = RbmqUtil.getInstance();
		
		rbmqUtil.getMsg(new EventProcesser() {
			
			@Override
			public void process(Object e) {//消费程序这里只是打印信息
				if(e instanceof String){
					System.out.println(e);
				}else if(e instanceof People){
					People people = (People)e;
					System.out.println(e);
					System.out.println("\tspouse: " + people.getSpouse());
					System.out.println("\tfried: "+ people.getFriends());
				}else if(e instanceof Map){
					@SuppressWarnings("unchecked")
					Iterator<Map.Entry<Integer, String>> it = ((Map<Integer, String>) e)
							.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Integer, String> entry = it.next();
						System.out.println("key= " + entry.getKey()
								+ " and value= " + entry.getValue());
					}
					  
					
				}
				System.out.println();
			}
		});
	}
	
//	@Test
	//按顺序发送N个字符串
	public void testSendMsgSort() throws SendRefuseException{
		RbmqUtil rbmqUtil = RbmqUtil.getInstance();
		for(int i=0;i<100;i++){
			rbmqUtil.sendMsg("hello" + i);
		}
	}
	
//	@Test
	//发送十个字符串
	public void testSendMsg() throws SendRefuseException, Exception{
		RbmqUtil rbmqUtil = RbmqUtil.getInstance();
//		rbmqUtil.sendMsg();
		for(int i=0; i<10 ; i++){
			new Thread(new SendMsgTask(i, rbmqUtil)).start();
//			TimeUnit.MILLISECONDS.sleep(300);
		}
	}
	
//	@Test
	//发送一个map
	public void testSendMap() throws SendRefuseException{
		RbmqUtil rbmqUtil = RbmqUtil.getInstance();
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "id1");
		map.put(2, "id2");
		map.put(3, "id3");
		rbmqUtil.sendMsg(map);
	}
	
//	@Test
	//发送一个object
	public void testSendObject() throws SendRefuseException{
		RbmqUtil rbmqUtil = RbmqUtil.getInstance();
		rbmqUtil.sendMsg(mockObj());
	}
	
	
	
//	@After
	public void end() throws InterruptedException{
		TimeUnit.SECONDS.sleep(30);
	}
	
	class SendMsgTask implements Runnable {
		private int num;
		private RbmqUtil rbmqUtil;
		
		public SendMsgTask(int num, RbmqUtil rbmqUtil) {
			super();
			this.num = num;
			this.rbmqUtil = rbmqUtil;
		}

		public void run() {
			try {
				System.out.println("发送数据：" + "hello world[" + num + "]");
				rbmqUtil.sendMsg("hello world[" + num + "]");
			} catch (SendRefuseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//模拟一个对象
	private People mockObj(){
		People jack = new People();
		jack.setId(1);
		jack.setName("JACK");
		jack.setMale(true);
		
		People polly = new People();
		polly.setId(2);
		polly.setName("polly");
		polly.setMale(true);
		
		List<People> friends = new ArrayList<People>();
		friends.add(jack);
		friends.add(polly);
		People hanMeiMei = new People();
		hanMeiMei.setId(3);
		hanMeiMei.setName("韩梅梅");
		hanMeiMei.setMale(false);
		hanMeiMei.setFriends(friends);
		
		People liLei = new People();
		liLei.setId(4);
		liLei.setName("李雷");
		liLei.setMale(true);
		liLei.setFriends(friends);
		liLei.setSpouse(hanMeiMei);
		hanMeiMei.setSpouse(liLei);
		return hanMeiMei;
	}
	
	public static void main(String[] args) throws SendRefuseException, Exception {
//		new TestMain().testMain();
//		new TestMain().testSendObject();
		new TestMain().testSendMsg();
//		new TestMain().testGetMsg();
	}

}
