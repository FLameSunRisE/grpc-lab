package com.flamesunrises.demo.client;

import com.flamesunrises.demo.service.GreetingServiceGrpc;
import com.flamesunrises.demo.service.Hello.HelloRequest;
import com.flamesunrises.demo.service.Hello.HelloResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

  private final ManagedChannel channel;
  private final GreetingServiceGrpc.GreetingServiceBlockingStub blockingStub;

  public GreetingClient(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build();

    blockingStub = GreetingServiceGrpc.newBlockingStub(channel);
  }

  public void sayHello(String name) {
    HelloRequest request = HelloRequest.newBuilder()
        .setName(name)
        .build();

    HelloResponse response = blockingStub.sayHello(request);

    System.out.println("Server response: " + response.getMessage());
  }

  public void shutdown() {
    channel.shutdown();
  }

  public static void main(String[] args) {
    GreetingClient client = new GreetingClient("localhost", 50051);
    System.out.println("GreetingClient.main");
    // 呼叫 gRPC 服務
    client.sayHello("John Doe");
    System.out.println("client sayHello = " + "John Doe");

    // 關閉客戶端
    client.shutdown();
  }
}