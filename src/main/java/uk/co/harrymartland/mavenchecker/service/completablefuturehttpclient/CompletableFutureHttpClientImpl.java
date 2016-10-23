package uk.co.harrymartland.mavenchecker.service.completablefuturehttpclient;

import java.util.concurrent.CompletableFuture;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompletableFutureHttpClientImpl implements CompletableFutureHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(CompletableFutureHttpClientImpl.class);

    @Autowired
    private CloseableHttpAsyncClient httpAsyncClient;

    @Override
    public CompletableFuture<HttpResponse> request(HttpUriRequest httpRequest) {
        LOG.info("downloading {}", httpRequest.getURI());
        CompletableFuture<HttpResponse> future = new CompletableFuture<>();
        httpAsyncClient.execute(httpRequest, new HttpResponseFutureCallback(future, httpRequest));
        return future;
    }

    private static class HttpResponseFutureCallback implements FutureCallback<HttpResponse> {
        private final CompletableFuture<HttpResponse> future;
        private final HttpUriRequest httpRequest;

        HttpResponseFutureCallback(CompletableFuture<HttpResponse> future, HttpUriRequest httpRequest) {
            this.future = future;
            this.httpRequest = httpRequest;
        }

        @Override
        public void completed(HttpResponse response) {
            LOG.info("download complete: {}, {}", httpRequest.getURI(), response.getStatusLine().getReasonPhrase());
            future.complete(response);
        }

        @Override
        public void failed(Exception e) {
            future.completeExceptionally(e);
        }

        @Override
        public void cancelled() {
            future.completeExceptionally(new Exception("Http request canceled"));
        }
    }
}
