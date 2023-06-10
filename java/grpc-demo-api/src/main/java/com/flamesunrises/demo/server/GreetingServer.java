package com.flamesunrises.demo.server;

import com.flamesunrises.demo.service.GreetingServiceGrpc;
import com.flamesunrises.demo.service.Hello.HelloRequest;
import com.flamesunrises.demo.service.Hello.HelloResponse;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;

public class GreetingServer {
  private Server server;

  public static void main(String[] args) throws IOException, InterruptedException {
    GreetingServer greetingServer = new GreetingServer();
    greetingServer.start();
    greetingServer.blockUntilShutdown();
  }

  private void start() throws IOException {
    int port = 50051;
    server = ServerBuilder.forPort(port)
        .addService((BindableService) new GreetingServiceImpl())
        .build()
        .start();

    System.out.println("Server started, listening on port " + port);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Shutting down gRPC server");
      GreetingServer.this.stop();
      System.out.println("Server shut down");
    }));
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  static class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
      String name = request.getName();
      System.out.println("GreetingServiceImpl name = " + name);
      String message = "Hello, " + name + "!";

      HelloResponse response = HelloResponse.newBuilder()
          .setMessage(message)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }
}