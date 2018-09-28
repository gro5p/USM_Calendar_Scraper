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
"""
wait = WebDriverWait( driver, 5 )


try:
    page_loaded = wait.until_not(
        lambda browser: browser.current_url == login_page
        )
except TimeoutException:
    self.fail( "Loading timeout expired" )

self.assertEqual(
    browser.current_url,
    correct_page,
    msg = "Successful Login"
    )
"""

driver.get("https://soar.usm.edu/psp/saprd90/EMPLOYEE/SA/c/SA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL")
#driver.get("https://soar.usm.edu/psc/saprd90/EMPLOYEE/SA/c/PRJCS_MENU.PRJCS_SCHD_STRT.GBL?Page=PRJCS_SCHD_STRT&Action=U&TargetFrameName=None%22")

#driver.find_element_by_id("DERIVED_SSS_SCL_SSS_MORE_ACADEMICS").click()
el = driver.find_element_by_id('DERIVED_SSS_SCL_SSS_MORE_ACADEMICS')
for option in el.find_elements_by_tag_name('option'):
    if option.text == 'Class Schedule':
        option.click() # select() in earlier versions of webdriver
        break
#driver.close()
