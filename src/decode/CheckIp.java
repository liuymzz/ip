package decode;

import com.google.gson.Gson;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CheckIp {


    /**
     * 根据传入的ip返回请求获得的json值
     *
     * @param ip ip
     * @return json数据
     * @throws Exception
     */
    //@Test
    public String getJson(String ip) throws Exception {

        //用来存放请求的返回数据
        StringBuffer sb = new StringBuffer();

        //利用ip拼接链接发送请求，简单直接粗暴的使用它的原始链接
        URL url = new URL("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=" +
                ip +
                "&co=&resource_id=6006&t=1494253752260&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110204935657823502546_1494253741148&_=1494253741151");
        URLConnection urlConnection = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "gbk"));
        //将请求返回的字节输入流使用字符输入流包装最后再包装成BufferedReader

        //将流中的数据读出并添加到字符串缓存中
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //因为请求返回的json值的首尾部分有一些其他内容，所以要进行简单的处理
        String s = sb.substring(sb.indexOf("(") + 1, sb.indexOf(")"));

        //最终返回正确的json数据
        return s;

    }


    /**
     * 将访问日志文件加载到内存中
     *
     * @return 返回List集合，包含了每一条访问的ip
     */
    //@Test
    public List<String> loadFile() throws Exception {

        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/ips"))));

        String line;
        while ((line = br.readLine()) != null) {
            //每次循环读取一行，并将每行的ip分割出来存入List
            list.add(line.substring(0, line.indexOf(" ")));
        }

        return list;
    }


    /**
     * 根据json获取地区值
     *
     * @param json 需要解析的json数据
     * @return 直接返回地区值
     */
    //@Test
    public String getArea(String json) {
        Gson gson = new Gson();

        //传入json数据以及自定义的POJO类，会返回一个创建的Json实例
        Json json1 = gson.fromJson(json, Json.class);

        //这里我们直接将实例中的地区属性返回
        return json1.data[0].location;
    }

    /**
     * 主要的执行类
     */
    @Test
    public void control() throws Exception {
        List<String> list = loadFile();

        for (String s : list) {
            String json = getJson(s);
            String area = getArea(json);
            System.out.print(s + ":");
            System.out.print(area);
            System.out.println();
        }


    }

}
