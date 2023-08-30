// camel-k: language=java
// camel-k: trait=prometheus.enabled=true

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;

public class Client extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      from("timer:java?repeatCount=10&period=2s")
        .routeId("java")
        .setBody(simple("{ \"value\": \"${date:now:yyyy-MM-dd-HHmmssSSS}\"}"))
        .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
        .to("http://api/v1/camelk?httpMethod=PUT")
        .log("response: ${body}");

  }
}