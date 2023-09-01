// camel-k: language=java
// camel-k: dependency=mvn:io.quarkus:quarkus-jdbc-postgresql
// camel-k: dependency=camel:jdbc
// camel-k: dependency=camel:jslt
// camel-k: trait=route.enabled=true trait=logging.json=false trait=prometheus.enabled=true
// camel-k: build-property=quarkus.datasource.camel.db-kind=postgresql
// camel-k: resource=configmap:jslt-spec/spec.json@/tmp/spec.json
// camel-k: open-api=configmap:camel-k-api
// camel-k: property=file:db.properties

import org.apache.camel.builder.RouteBuilder;

public class API extends RouteBuilder {
  @Override
  public void configure() throws Exception {

        from("direct:getAll")
        .log("Listing all messages")
        .setBody(constant("select * from messages"))
        .to("jdbc:camel")
        .convertBodyTo(String.class);

        from("direct:putMessage")
        .convertBodyTo(String.class)
        .log("saving a new message: ${body}")
        .to("jslt:file:/tmp/spec.json?allowContextMapAll=true")
        .setBody(simple("insert into messages (message) values ('${body}')"))
        .to("jdbc:camel")
        .setBody(constant("message saved"));
  }
}