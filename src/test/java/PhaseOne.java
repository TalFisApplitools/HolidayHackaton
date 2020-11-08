import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.visualgrid.model.DesktopBrowserInfo;
import com.applitools.eyes.visualgrid.model.IosDeviceInfo;
import com.applitools.eyes.visualgrid.model.IosDeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;

public class PhaseOne {
	private String appName = "AppliFashion";
	private String batchName = "Testing Lifecycle";
	private String apiKey = System.getenv("APPLITOOLS_API_KEY");
	private VisualGridRunner runner;
	private Configuration config;
	private Eyes eyes;
	private WebDriver webDriver;
	private RectangleSize viewportSize;
	private String V1URL = "https://demo.applitools.com/tlcHackathonMasterV1.html";
	private String V2URL = "https://demo.applitools.com/tlcHackathonDev.html";
	private String V3URL = "https://demo.applitools.com/tlcHackathonMasterV2.html";
	
	@BeforeSuite
	public void beforeTestSuite() {
		
        viewportSize = new RectangleSize(1200,800);
		
		runner = new VisualGridRunner(10);
		BatchInfo batchInfo = new BatchInfo(batchName);
		eyes = new Eyes(runner);      
		
		config = new Configuration();
		config.addBrowser(new DesktopBrowserInfo(viewportSize, BrowserType.CHROME))
		//uncomment this for phase 3
		.addBrowser(new DesktopBrowserInfo(viewportSize, BrowserType.FIREFOX))
		.addBrowser(new DesktopBrowserInfo(viewportSize, BrowserType.EDGE_CHROMIUM))
		.addBrowser(new DesktopBrowserInfo(viewportSize, BrowserType.SAFARI))
        .addBrowser(new IosDeviceInfo(IosDeviceName.iPhone_X, ScreenOrientation.PORTRAIT))
		.setViewportSize(viewportSize)
		.setApiKey(apiKey)
		.setBatch(batchInfo);
	
	    eyes.setConfiguration(config);

		webDriver = new ChromeDriver();
	}

	@BeforeMethod
	public void beforeEachTest() {
	}

	@Test
	public void testOne() {
		WebDriver driver = eyes.open(webDriver, appName, "Test 1");

		//change this URL according to the phase
		driver.get(V3URL);
		eyes.checkWindow("main page", true);
	}


	@Test
	public void testTwo() {
		WebDriver driver = eyes.open(webDriver, appName, "Test 2");

		//change this URL according to the phase
		driver.get(V3URL);
		driver.findElement(By.id("SPAN__checkmark__107")).click();
		driver.findElement(By.id("filterBtn")).click();
		eyes.checkRegion(By.id("product_grid"), "filter by color", true);
	}
	
	@Test
	public void testThree() {
		WebDriver driver = eyes.open(webDriver, appName, "Test 3");

		//change this URL according to the phase
		driver.get(V3URL);
		driver.findElement(By.id("DIV__colcolmd__210")).click();

		eyes.checkWindow("product details", true);
	}
	
	@AfterMethod
	public void afterEachTest(ITestResult result) {
		eyes.closeAsync();
	}

	@AfterSuite
	public void afterTestSuite(ITestContext testContext) {
		webDriver.quit();
		eyes.abortIfNotClosed();
		
		// Wait until the test results are available and retrieve them
		TestResultsSummary allTestResults = runner.getAllTestResults(false);
		System.out.println(allTestResults);
	}
}
