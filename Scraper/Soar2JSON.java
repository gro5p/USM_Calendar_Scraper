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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.*;


public class Soar2JSON {


    public static void main(String [] args) throws IOException, MalformedURLException, UnsupportedEncodingException, URISyntaxException
    {
        //this will store the output
        List<String> namesList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> statusList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> unitsList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> gradingsList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> gradesList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> classNumbersList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> sectionsList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> componentsList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> timesList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> roomsList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> instructorsList = Stream.of(new String[]{}).collect(Collectors.toList());
        List<String> datesList = Stream.of(new String[]{}).collect(Collectors.toList());

        //Stores the cookies necessary to reach the Soar Calendar
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        java.io.Console console = System.console();
        String user_name = console.readLine("Username: ");
        String password = new String(console.readPassword("Password: "));

        try
        {
            String Semester_ID="4191";

            String e_pass=URLEncoder.encode(password, StandardCharsets.UTF_8.name());
            String send_this=("userid="+user_name+"&pwd="+e_pass);
            URL login_URL = new URL("https://soar.usm.edu/psc/saprd90/EMPLOYEE/SA/c/NUI_FRAMEWORK.PT_LANDINGPAGE.GBL?&cmd=login&languageCd=ENG");

            HttpsURLConnection con = (HttpsURLConnection) login_URL.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            con.getOutputStream().write(send_this.getBytes("UTF-8"));
            con.getContent();


            CookieStore cookieStore = cookieManager.getCookieStore();
            //the cookies need to be passed in a particular Order
            //so I'm using a map to pass them in that order
            Map<String, String> cookieMap=(cookieStore.getCookies()).stream()
            .filter(cookie -> cookie.getName().equals("PS_TOKEN") ||
            cookie.getName().contains("18011") ||
            cookie.getName().equals("BIGipServersoar")).collect(
            Collectors.toMap(cookie->cookie.getName(),cookie->cookie.getValue()));

            String tricky_bastard="";

            //the name of the second cookie often changes...
            //this is here to store it in a string
            for (String key : cookieMap.keySet())
            {

              if (key.contains("18011"))
              {
                tricky_bastard=key;
              }
            }


            String cookie_header="PS_TOKEN="+cookieMap.get("PS_TOKEN")+"; "
            +tricky_bastard+"="+cookieMap.get(tricky_bastard)+ "; BIGipServersoar="
            +cookieMap.get("BIGipServersoar");

            String Calendar_URL="https://soar.usm.edu/psc/saprd90/EMPLOYEE/SA/c/SA_LEARNER_SERVICES.SSR_SSENRL_LIST.GBL?ACAD_CAREER=UGRD&INSTITUTION=USM01&STRM="
            + Semester_ID+"&&";

            //System.out.println(Calendar_URL);
            URL services_URL= new URL(Calendar_URL);

            //System.out.println(cookie_header);


            con.disconnect();
            con = (HttpsURLConnection) services_URL.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Cookie", cookie_header);
            con.getContent();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    con.getInputStream()));

            BufferedWriter writer = new BufferedWriter(new FileWriter("output"));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
              {
                if (inputLine.contains("PAGROUPDIVIDER"))
                  namesList.add(inputLine.replace("</td></tr>","").replaceAll(".*>","").replace("&amp;","and"));
                if (inputLine.contains("STATUS"))
                  statusList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
                if (inputLine.contains("DERIVED_REGFRM1_UNT_TAKEN"))
                  unitsList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
                if (inputLine.contains("GB_DESCR"))
                  gradingsList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
                if (inputLine.contains("DERIVED_REGFRM1_CRSE_GRADE_OFF"))
                  gradesList.add(inputLine.replace("</span>", "").replaceAll(".*>","").replace("&nbsp;","Not Available"));
                if (inputLine.contains("DERIVED_CLS_DTL_CLASS_NBR"))
                  classNumbersList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
                if (inputLine.contains("MTG_SECTION"))
                  sectionsList.add(inputLine.replace("</a></span>","").replaceAll(".*>",""));
                if (inputLine.contains("MTG_COMP"))
                  componentsList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
                if (inputLine.contains("MTG_SCHED"))
                  timesList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
                if (inputLine.contains("MTG_LOC"))
                  roomsList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
                if (inputLine.contains("DERIVED_CLS_DTL_SSR_INSTR_LONG"))
                  instructorsList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
                if (inputLine.contains("MTG_DATES"))
                  datesList.add(inputLine.replace("</span>", "").replaceAll(".*>",""));
              }
            in.close();
            for (int i=0;i<namesList.size();i++)
            {
              System.out.println("Course: "+namesList.get(i));
              System.out.println("Status: "+statusList.get(i));
              System.out.println("Units: "+unitsList.get(i));
              System.out.println("Grading Scale: "+gradingsList.get(i));
              System.out.println("Grade: "+gradesList.get(i));
              System.out.println("Course Code: "+classNumbersList.get(i));
              System.out.println("Section: "+sectionsList.get(i));
              System.out.println("Component: "+componentsList.get(i));
              System.out.println("Times: "+timesList.get(i));
              System.out.println("Location: "+roomsList.get(i));
              System.out.println("Instructor: "+instructorsList.get(i));
              System.out.println("Dates: "+datesList.get(i));
              System.out.println();
            }
            writer.close();





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
