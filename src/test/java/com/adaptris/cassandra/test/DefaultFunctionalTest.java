package com.adaptris.cassandra.test;

import com.adaptris.testing.DockerComposeFunctionalTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import java.io.File;
import java.net.InetSocketAddress;
import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class DefaultFunctionalTest extends DockerComposeFunctionalTest {

    protected static String INTERLOK_SERVICE_NAME = "interlok-1";
    protected static String CASSANDRA_SERVICE_NAME = "cassandra-1";
    protected static int INTERLOK_PORT = 8080;
    protected static int CASSANDRA_PORT = 9042;
    protected static WaitStrategy defaultWaitStrategy = Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60));



    @Override
    protected ComposeContainer setupContainers() throws Exception {
        return new ComposeContainer(new File("docker-compose.yaml"))
                .withExposedService(INTERLOK_SERVICE_NAME, INTERLOK_PORT, defaultWaitStrategy)
                .withExposedService(CASSANDRA_SERVICE_NAME, CASSANDRA_PORT, defaultWaitStrategy);
    }

    protected String getInterlokEndpoint(String path) {
        InetSocketAddress address = getHostAddressForService(INTERLOK_SERVICE_NAME, INTERLOK_PORT);
        if (!path.startsWith("/")) path = "/" + path;
        return "http://" + address.getHostString() + ":" + address.getPort() + path;
    }

    @Test
    public void test() throws Exception {
        Thread.sleep(20000); // wait for interlok to startup
        given().body("""
{
        "username":"username",
        "firstName":"firstName",
        "lastName":"lastName",
        "password":"password"
                        }
                        """)
        .post(getInterlokEndpoint("/api/cassandra"))
                .then().assertThat()
                .statusCode(200);

        given()
                .get(getInterlokEndpoint("/api/cassandra/username"))
                .then().assertThat()
                .statusCode(200);
        given()
                .delete(getInterlokEndpoint("/api/cassandra/username"))
                .then().assertThat()
                .statusCode(200);
        given()
                .get(getInterlokEndpoint("/api/cassandra/username"))
                .then().assertThat().body(equalTo("[]"))
                .and().statusCode(200);

    }




}
