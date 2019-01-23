package tech.xiangcheng.orangebus.order;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import freemarker.template.Configuration;
import freemarker.template.Template;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;
import tech.xiangcheng.orangebus.order.service.PassengerBusOrderService;
import tech.xiangcheng.orangebus.order.service.TicketChangeAsynchronousService;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TicketChangeServiceTest {
	@Autowired
	TicketChangeAsynchronousService ticketChangeService;
	@Autowired
	PassengerBusOrderService passengerBusOrderSevcie;
//	@Test
//	public void test() throws InterruptedException {
//		JsonObj newOrderParams = JsonObj.instance();
//		String newCode = "OD1502203990";
//		String oldCode = "12345";
//		String openid = "o5-Phw1A7s8hzCjl5chqGnEUOpCc";
//		newOrderParams.putProperty("code", newCode)
//			.putProperty("total", "70.00")
//			.putProperty("startStationName", "测试始发站")
//			.putProperty("endStationName", "测试终点站")
//			.putProperty("startTime", new Date())
//			.putProperty("startCityName", "测试始发城市")
//			.putProperty("endCityName", "测试到达城市")
//			;
//		JsonObj oldOrderParams = JsonObj.instance();
//		oldOrderParams.putProperty("code", oldCode)
//				.putProperty("total", "0.01")
//				.putProperty("startStationName", "测试始发站")
//				.putProperty("endStationName", "测试终点站")
//				.putProperty("startTime", new Date())
//				.putProperty("startCityName", "测试始发城市")
//				.putProperty("endCityName", "测试到达城市")
//				;
//		ticketChangeService.submitTicketChange(newOrderParams, oldOrderParams);
//		Thread.sleep(2000);
//		assertTrue(ticketChangeService.isChangeOrder(newCode));
//		ticketChangeService.submitTicketPaid(newCode, openid);
//		Thread.sleep(3000);
//	}
	@Test
	public void testGetOrder() {
		PassengerBusOrder order = passengerBusOrderSevcie.getOrderById("0021b8fb-f9a0-45fc-9156-647a4a98cfec");
		System.out.println("getBuyer");
		System.out.println(order.getBuyer().getId()+"");
		System.out.println("getCouponSn");
		System.out.println(order.getCouponSn()+"");
		System.out.println("getPersonalAccount");
		System.out.println(order.getCouponSn().getPersonalAccount()+"");
		System.out.println("getId");
		System.out.println(order.getCouponSn().getPersonalAccount().getId()+"");
		
	}
	
	@Test  
	   public void exportSimpleWord() throws Exception{  
	       // 要填充的数据, 注意map的key要和word中${xxx}的xxx一致  
	      Map<String,String> dataMap = new HashMap<String,String>();  
	      dataMap.put("username", "张三");  
	      dataMap.put("sex", "男");  
	            
	    //Configuration用于读取ftl文件  
	      Configuration configuration = new Configuration();  
	      configuration.setDefaultEncoding("utf-8");  
	        
	      /*以下是两种指定ftl文件所在目录路径的方式, 注意这两种方式都是 
	       * 指定ftl文件所在目录的路径,而不是ftl文件的路径 
	       */  
	      //指定路径的第一种方式(根据某个类的相对路径指定)  
	      //configuration.setClassForTemplateLoading(this.getClass(),"");  
	        
	      //指定路径的第二种方式,我的路径是C:/a.ftl  
	      configuration.setDirectoryForTemplateLoading(new File("C:/"));  
	        
	        
	      // 输出文档路径及名称  
	     File outFile = new File("D:/test.doc");  
	       
	     //以utf-8的编码读取ftl文件  
	     Template t =  configuration.getTemplate("a.ftl","utf-8");  
	     Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"),10240);  
	        t.process(dataMap, out);  
	        out.close();  
	   }  
}
