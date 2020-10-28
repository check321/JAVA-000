import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
* @title make a http call via OkHttp3.
* @description
* @author fyang
* @date 2020/10/28 6:59 下午
*/
@Slf4j
public class HttpDemo {

    @Test
    public void httpGet() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:25030/benchmark/foo/fyang")
                .build();

        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();

        // console print: response-body: [hi, fyang]
        log.info("response-body: [{}]",response.body().string());
    }

}
