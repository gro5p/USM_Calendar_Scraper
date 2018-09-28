from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from   selenium.common.exceptions import TimeoutException

user=input("please enter username: ")
password=input("please enter a password: ")
driver=webdriver.Chrome()

driver.get("https://soar.usm.edu/psp/saprd90/?cmd=login")
assert "SOAR" in driver.title
elem = driver.find_element_by_id("userid")
elem.send_keys(user)
elem = driver.find_element_by_id("pwd")
elem.send_keys(password)
elem.send_keys(Keys.RETURN)


driver.get("https://soar.usm.edu/psp/saprd90/EMPLOYEE/SA/c/SA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL")
driver.get("https://soar.usm.edu/psc/saprd90/EMPLOYEE/SA/c/PRJCS_MENU.PRJCS_SCHD_STRT.GBL?Page=PRJCS_SCHD_STRT&Action=U&TargetFrameName=None%22")
driver.find_element_by_id("win0divPRJCS_DERIVED_PRJCS_LAUNCH_CS").click()

main_window=driver.current_window_handle

driver.find_element_by_tag_name('body').send_keys(Keys.CONTROL + Keys.TAB)
#driver.switch_to.window(main_window)

wait=WebDriverWait( driver, 10 )

try:
    page_loaded = wait.until(
        lambda driver: driver.current_url == "usm.collegescheduler.com/entry"
        )
except TimeoutException:
    print( "Loading timeout expired" )
    print(driver.current_url)

schedule_source=open("collegescheduler.html", "w")

driver.get("https://usm.collegescheduler.com/courses")
html = driver.page_source
schedule_source.write(html).close()
