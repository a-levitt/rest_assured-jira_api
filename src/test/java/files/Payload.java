package files;

public class Payload {
    public static String postAddBugBody() {
        return "{\n" +
                "    \"fields\": {\n" +
                "       \"project\":\n" +
                "       {\n" +
                "          \"key\": \"SCRUM\"\n" +
                "       },\n" +
                "       \"summary\": \"[Test][RestAssured] Create bug using Jira API and Rest Assured\",\n" +
                "       \"issuetype\": {\n" +
                "          \"name\": \"Bug\"\n" +
                "       }\n" +
                "   }\n" +
                "}";
    }
}
