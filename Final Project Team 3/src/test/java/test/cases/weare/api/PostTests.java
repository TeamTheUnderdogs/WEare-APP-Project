package test.cases.weare.api;

import com.weare.testframework.api.WeAreAPI;
import com.weare.testframework.api.utils.Constants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.Map;

import static com.weare.testframework.Utils.getConfigPropertyByKey;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostTests {
    private final WeAreAPI api = new WeAreAPI();

    public void authenticate() {
        if (!api.hasAuthenticateCookies()) {
            api.authenticateAndFetchCookies();
            assertTrue(api.hasAuthenticateCookies());
        }
    }

    @Test
    @Order(1)
    public void createPostTest() {
        // Requires authentication
        authenticate();

        String content = getConfigPropertyByKey("social.post.content");
        String picture = getConfigPropertyByKey("social.post.picture");
        boolean isPublic = Boolean.parseBoolean(getConfigPropertyByKey("social.post.public"));

        Response response = api.createPost(content, picture, isPublic);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        // Response example:
        // {"postId":70,"content":"Post Content","picture":null,"date":"08/10/2023 18:17:30","likes":[],"comments":[],"rank":70,"public":true,"category":{"id":100,"name":"All"},"liked":false}
        JsonPath bodyJsonPath = response.getBody().jsonPath();
        String postContent = bodyJsonPath.getString("content");
        String postPicture = bodyJsonPath.getString("picture");
        boolean postPublic = bodyJsonPath.getBoolean("public");
        assertEquals(content, postContent);
        assertEquals(picture, postPicture);
        assertEquals(isPublic, postPublic);

        Constants.POST_ID = bodyJsonPath.get("postId");

        System.out.printf("Post with id %d was created%n%n", Constants.POST_ID);
    }

    @Test
    @Order(2)
    public void updatePostTest() {
        // Requires authentication
        authenticate();

        String content = getConfigPropertyByKey("social.post.contentModified");
        String picture = getConfigPropertyByKey("social.post.picture");
        boolean isPublic = Boolean.parseBoolean(getConfigPropertyByKey("social.post.public"));

        Response response = api.updatePost(content, picture, isPublic);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        System.out.printf("Post with id %d was updated%n%n", Constants.POST_ID);
    }

    @Test
    @Order(2)
    public void likePostTest() {
        // Requires authentication
        authenticate();

        Response response = api.likePost();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        // Example response:
        // {"postId":74,"content":"Post Content","picture":"","date":"08/10/2023 19:03:39","likes":[{"userId":74,"username":"denip","expertiseProfile":{"id":74,"skills":[],"category":{"id":100,"name":"All"},"availability":0.0},"enabled":true,"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true}],"comments":[],"rank":75,"public":true,"hibernateLazyInitializer":{},"category":{"id":100,"name":"All"},"liked":true}
        JsonPath bodyJsonPath = response.getBody().jsonPath();
        ArrayList likes = bodyJsonPath.get("likes");
        boolean likedByUser = false;
        for (Object like: likes) {
            Map<String, Object> likeMap = (Map<String, Object>) like;
            String username = (String)likeMap.get("username");
            if (username.equals(getConfigPropertyByKey("social.api.username"))) {
                likedByUser = true;
                break;
            }
        }
        assertTrue(likedByUser);
    }

    @Test
    @Order(2)
    public void getCommentsForPostTest() {
        Response response = api.getCommentsForPost();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
    }

    @Test
    @Order(3)
    public void deleteTestPost() {
        // Requires authentication
        authenticate();

        Response response = api.deletePost();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
    }
}