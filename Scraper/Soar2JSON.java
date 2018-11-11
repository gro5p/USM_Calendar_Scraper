import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.net.URL;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import javax.net.ssl.HttpsURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.*;

public class Soar2JSON {


    public static void main(String [] args) throws IOException, MalformedURLException, UnsupportedEncodingException, URISyntaxException
    {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        try

        {
            String user_name="w111111111";
            String Semester_ID="4191";
            String password="12345";

            String e_pass=URLEncoder.encode(password, StandardCharsets.UTF_8.name());
            String send_this=("userid="+user_name+"&pwd="+e_pass);
            URL login_URL = new URL("https://soar.usm.edu/psc/saprd90/EMPLOYEE/SA/c/NUI_FRAMEWORK.PT_LANDINGPAGE.GBL?&cmd=login&languageCd=ENG");

            HttpsURLConnection con = (HttpsURLConnection) login_URL.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            con.getOutputStream().write(send_this.getBytes("UTF-8"));
            con.getContent();


            CookieStore cookieStore = cookieManager.getCookieStore();
            Map<String, String> cookieMap=(cookieStore.getCookies()).stream()
            .filter(cookie -> cookie.getName().equals("PS_TOKEN") ||
            cookie.getName().equals("pswssa24-18011-PORTAL-PSJSESSIONID") ||
            cookie.getName().equals("BIGipServersoar")).collect(
            Collectors.toMap(cookie->cookie.getName(),cookie->cookie.getValue()));


            String cookie_header="PS_TOKEN="+cookieMap.get("PS_TOKEN")+"; "
            +cookieMap.get("pswssa24-18011-PORTAL-PSJSESSIONID")+ "; BIGipServersoar="
            +cookieMap.get("BIGipServersoar");
            URL services_URL= new URL("https://soar.usm.edu/psc/saprd90/EMPLOYEE/SA/c/SA_LEARNER_SERVICES.SSR_SSENRL_LIST.GBL?ACAD_CAREER=UGRD&INSTITUTION=USM01&STRM=$SEMESTER&&");

            con.disconnect();
            con = (HttpsURLConnection) services_URL.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Cookie", cookie_header);
            con.getContent();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
              System.out.println(inputLine);
            in.close();


            //-H "Cookie: PS_TOKEN=$PS_TOKEN; $PSWSSA; BIGipServersoar=$BIGIP"


            /*for (HttpCookie cookie : cookieList)
		          {
			             // gets domain set for the cookie
			             System.out.println("Domain: " + cookie.getDomain());

			             // gets max age of the cookie
			             System.out.println("max age: " + cookie.getMaxAge());

			             // gets name cookie
			             System.out.println("name of cookie: " + cookie.getName());

			             // gets path of the server
			             System.out.println("server path: " + cookie.getPath());

			             // gets boolean if cookie is being sent with secure protocol
			             System.out.println("is cookie secure: " + cookie.getSecure());

			             // gets the value of the cookie
			             System.out.println("value of cookie: " + cookie.getValue());

			             // gets the version of the protocol with which the given cookie is related.
			             System.out.println("version of cookie: " + cookie.getVersion());



                   System.out.println();

		            }*/
        }
        catch (MalformedURLException e)
        {
            // Replace this with your exception handling
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
          e.printStackTrace();
        }
        catch (IOException e)
        {
            // Replace this with your exception handling
            e.printStackTrace();
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }

    }
}
