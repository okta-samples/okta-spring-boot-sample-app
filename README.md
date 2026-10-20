Spring Boot Web App Quickstart
==================

This is the completed project corresponding to the guide "[Sign users in to your web app](https://developer.okta.com/docs/guides/sign-into-web-app-redirect/spring-boot/main/)".


Prerequisites
-------------

- Java 25 or later
- An Okta org with an **OIDC Web Application** integration:
  - **Sign-in redirect URI:** `http://localhost:8080/login/oauth2/code/okta`
  - **Sign-out redirect URI:** `http://localhost:8080`
  - **Refresh Token** grant type enabled on the app's **General** tab

Getting Started
---------------

Create a `.env` file and set:

- `ISSUER`: the issuer URI of your authorization server, for example `https://{yourOktaDomain}/oauth2/default`.
- `CLIENT_ID`: the client ID from your app's **Client Credentials** section.
- `CLIENT_SECRET`: the client secret from the same section.

```text
ISSUER=https://{yourOktaDomain}/oauth2/default
CLIENT_ID={yourClientId}
CLIENT_SECRET={yourClientSecret}
```

> Don't commit `.env` to source control.

Load the variables and run the app (it starts on port 8080):

```bash
set -a && source .env && set +a
./mvnw spring-boot:run
```

Open `http://localhost:8080` in your browser and click **Login**. After signing in, visit `/profile` to see your ID token claims.

Call the API
------------

This app is also the front-end for [okta-spring-boot-sample-api](https://github.com/okta-samples/okta-spring-boot-sample-api). After signing in, open **Messages** (`/messages`). The app sends your access token to the API as a `Bearer` token and shows the response.

To try it:

1. Set up the API's custom scope and access policy as described in its README, and use the **same** `ISSUER` for both projects.
2. Start the API (port 8081), then start this app (port 8080).
3. Sign in and open `http://localhost:8080/messages`.

The app requests the `messages.read` scope at login. If the API is not on `http://localhost:8081`, set `API_BASE_URL`.
