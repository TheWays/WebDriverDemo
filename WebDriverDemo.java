package kz.bee.hello.se.test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class WebDriverDemo {

	private static Logger logger = LoggerFactory.getLogger(WebDriverDemo.class);
	
	private static String url = "http://fund.csrc.gov.cn/web/open_fund_daily_net.daily_report?netDate=2017-04-28";
//	static {
//		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
//		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
//	}
	
	private WebDriver webDriver;
	
	public void initWebDriver(boolean enableJavaScript){
		logger.info("-------------init webDriver---------");
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45,enableJavaScript);
//		driver.setProxy("121.8.98.201", 8080);
		
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES);
		webDriver = driver;
		
	}
	
	public void closeWebDriver(){
		logger.info("------closing webDriver");
		webDriver.close();
	}
	
	public boolean grabPage(){
		initWebDriver(true);
		try{
			webDriver.navigate().to(url);
			List<WebElement> list = webDriver.findElements(By.xpath("//*[@id='tablesorter-instance']/tbody/tr"));
			if(list.isEmpty()){
				logger.info(url+ "##page no data");
				return false;
			} else {
				for(WebElement row:list){
					List<WebElement> columnList = row.findElements(By.tagName("td"));
					String fundUrl = columnList.get(2).findElement(By.tagName("a")).getAttribute("href");
					String fundCode = columnList.get(1).getText();
					System.out.println("fundCode:" + fundCode +"'s url =" + fundUrl);
					return true;
				}
			}
			
		} catch(Exception e){
			logger.info(e.getMessage());
			return false;
		}
		closeWebDriver();
		return true;
	}
	
	public static void main(String args[]) {
		WebDriverDemo demo = new WebDriverDemo();
		System.out.println(demo.grabPage()?"grab result is succeed": "grab operation failed");
	}
}
