import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.upload.aliyun.util.FileDoUtil;

public class Demo {
  public static void main(String[] args) throws Exception {
    Map<String,String> m = new HashMap<String,String>();
    String url =  "http://localhost:8080/song1/api/book/v1/savebook";
    String code = "UTF-8";
    m.put("name", "nihao hello");
    m.put("url", "http://www.baidu.com");
    String rus = doPost(url, m, code);
    FileDoUtil.outLog(rus.toString());
  }

  public static String doPost(String reqUrl, Map<String,String> parameters, String recvEncoding) {
    HttpURLConnection conn = null;
    String responseContent = null;
    try {
      StringBuffer params = new StringBuffer();
      for (Map.Entry<String , String> element : parameters.entrySet()) {
        params.append(element.getKey().toString());
        params.append("=");
        params.append(URLEncoder.encode(element.getValue().toString(), recvEncoding));
        params.append("&");
      }

      if (params.length() > 0) {
        params = params.deleteCharAt(params.length() - 1);
      }
      URL url = new URL(reqUrl);
      HttpURLConnection url_con = (HttpURLConnection) url.openConnection();
      url_con.setRequestMethod("POST");
      url_con.setConnectTimeout(10000);//（单位：毫秒）jdk
      // 1.5换成这个,连接超时
      url_con.setReadTimeout(10000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
      url_con.setDoOutput(true);
      byte[] b = params.toString().getBytes();
      url_con.getOutputStream().write(b, 0, b.length);
      url_con.getOutputStream().flush();
      url_con.getOutputStream().close();

      InputStream in = url_con.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
      String tempLine = rd.readLine();
      StringBuffer tempStr = new StringBuffer();
      String crlf = System.getProperty("line.separator");
      while (tempLine != null) {
        tempStr.append(tempLine);
        tempStr.append(crlf);
        tempLine = rd.readLine();
      }
      responseContent = tempStr.toString();
      rd.close();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return responseContent;
  }

}