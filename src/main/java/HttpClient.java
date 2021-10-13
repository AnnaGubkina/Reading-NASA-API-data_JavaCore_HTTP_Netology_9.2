import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;


public class HttpClient {

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();


        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=lH1bCk8RCAuIvQc1GLASYdbNakNqPeXCJNkVxsOl");
        CloseableHttpResponse response = httpClient.execute(request);

        NasaPic urlNasa = mapper.readValue(response.getEntity().getContent(), NasaPic.class);
        System.out.println(urlNasa);

        HttpGet request1 = new HttpGet(urlNasa.getUrl());
        CloseableHttpResponse response1 = httpClient.execute(request1);

        String pic = urlNasa.getUrl();
        System.out.println(pic);
        String[] array = urlNasa.getUrl().split("/");
        String picFile = array[6];
        System.out.println();
        HttpEntity entity = response1.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(picFile);
            entity.writeTo(fos);
            fos.close();
        }
    }
}
