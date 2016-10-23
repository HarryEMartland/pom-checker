package uk.co.harrymartland.mavenchecker.service.completablefuturehttpclient;

import java.util.concurrent.CompletableFuture;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public interface CompletableFutureHttpClient {

    CompletableFuture<HttpResponse> request(HttpUriRequest httpRequest);

}
