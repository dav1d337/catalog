package com.dav1337d.catalog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;


public class AppiumTests {

    WebDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("deviceName", "Android Device");
        capabilities.setCapability(CapabilityType.VERSION, "10.0.0");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.dav1337d.catalog");
        capabilities.setCapability("appActivity", "com.dav1337d.catalog.MainActivity");

        driver = new AndroidDriver<>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void test() {
        driver.findElement(MobileBy.className("android.widget.ImageButton")).click();

        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"You clicked!\")"));

        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
    }

    //@After
    //public void End() {
    //    driver.quit();
    //}
}