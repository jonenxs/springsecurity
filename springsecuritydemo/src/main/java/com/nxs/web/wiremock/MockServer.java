package com.nxs.web.wiremock;


import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class MockServer {

    public static void main(String[] args) throws IOException {
        WireMock.configureFor(8062);
        WireMock.removeAllMappings();

        mock("/order/1","01");
    }

    //D:\ideaProject\springsecurity\springsecuritydemo\src\main\resources\mock.response\01.txt
    public static void mock(String url,String file) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/"+ file+".txt");
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(),"UTF-8").toArray(),"\n");
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(url)).willReturn(WireMock.aResponse().withBody(content).withStatus(200)));
    }
}
