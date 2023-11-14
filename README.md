# BurpSuite Bambda Filters

This repository is a amalgamation of Bambda filters that can be considered useful during testing!

The intent is to provide credits to the original authors where possible.

# Need help?

If you would like to request a filter, feel free to open an Issue and I'll try my best to make it for you! Please note, this is a fun project for learning purposes, so the solutions may not be the best!

---

### dateTime

> @sml555\_

This filter allows you to control the datetime aspect of the requests and find requests made before or after a datetime (dependant upon the comparison operator you use):

```java
if (!requestResponse.hasResponse()){
    return false;
}

long epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-01-01 00:00:00").getTime() / 1000;

if (epoch < requestResponse.time().toEpochSecond()){
	return true;
}

return false;
```

### hasResponse

> @sml555\_

This filter allows you to control the datetime aspect of the requests and find requests made before or after a datetime (dependant upon the comparison operator you use):

```java
if (!requestResponse.hasResponse()){
    return false;
}
return true;
```

### Status Code and Cookie Name

> @PortSwigger

This is a [PortSwigger example](https://portswigger.net/burp/documentation/desktop/tools/proxy/http-history/bambdas) to show only requests that meet the following criteria:

- The request must have a response.
- The response must have a `3XX` status code.
- The response must have a cookie set with the name session.

```java
if (!requestResponse.hasResponse()) {
    return false;
}

var response = requestResponse.response();
return response.isStatusCodeClass(StatusCodeClass.CLASS_3XX_REDIRECTION) && response.hasCookie("session");
```

### Cookie Value

> @PortSwigger

This is a [PortSwigger example](https://portswigger.net/blog/introducing-bambdas) to find requests with a specific cookie value:

```java
if (requestResponse.request().hasParameter("foo", HttpParameterType.COOKIE)) {
var cookieValue = requestResponse
.request()
.parameter("foo", HttpParameterType.COOKIE)
.value();

return cookieValue.contains("1337");
}

return false;
```

or an more concise version:

```java
var request = requestResponse.request();
var cookie = request.parameter("foo", HttpParameterType.COOKIE);

return cookie != null && cookie.value().conatins("1337");
```

### JSON responses with wrong Content-Type

> @PortSwigger

This is a [PortSwigger example](https://portswigger.net/blog/introducing-bambdas) to return responses where the content is probably JSON, but the content type is not `application/json`:

```java
var contentType = requestResponse.response().headerValue("Content-Type");

if (contentType != null && !contentType.contains("application/json")) {
    String body = requestResponse.response().bodyToString().trim();

    return body.startsWith( "{" ) || body.startsWith( "[" );
}

return false;
```

### Find role within JWT claims

> @PortSwigger

This is a [PortSwigger example](https://portswigger.net/blog/introducing-bambdas) to find responses with a JWT in an `Authorization: Bearer` header that contains the term `role`:

```java
var body = requestResponse.response().bodyToString().trim();

if (requestResponse.response().hasHeader("authorization")) {
    var authValue = requestResponse.response().headerValue("authorization");

    if (authValue.startsWith("Bearer ey")) {
        var tokens = authValue.split("\\.");

        if (tokens.length == 3) {
            var decodedClaims = utilities().base64Utils().decode(tokens[1], Base64DecodingOptions.URL).toString();

            return decodedClaims.toLowerCase().contains("role");
        }
    }
}

return false;
```

### Pass URLs to another tool

> @yougina

Pass URL's (in this case requests that have a body parameter of value `parameter`) as stdin to another tool.
The ProcessBuilder is used to create operating system processes. The first argument is the command to execute, the second `"-"` is specifying `stdin` input:

```java
if (!requestResponse.request().hasParameter("parameter", HttpParameterType.BODY)) {
	return false
}

ProcessBuilder builder = new ProcessBuilder("path-to-process", "-");
Process process = builder.start();
OutputStream stdin = process.getOutputStream();
BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

writer.write(requestResponse.request().url());
writer.flush();
writer.close();

return true;
```
