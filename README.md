# Okta Spring Boot App

This example shows you how to use the [Okta Spring Boot Starter][] to sign users in with the [Authorization Code Flow][]. The user is redirected to the Okta-hosted login page and returned to this application after authenticating, where a server-side session is created.

## Prerequisites

- [Java 17+](https://sdkman.io/jdks)
- An Okta Integrator Free Plan account — sign up at [developer.okta.com](https://developer.okta.com/login)

## Create an Okta App Integration

In the Okta Admin Console:

1. Go to **Applications > Applications**, click **Create App Integration**
2. Select **OIDC - OpenID Connect** and **Web Application**, then click **Next**
3. Enter an app name
4. Set the **Sign-in redirect URI** to `http://localhost:8080/login/oauth2/code/okta`
5. Set the **Sign-out redirect URI** to `http://localhost:8080`
6. Click **Save**

## Configure

Copy `.env.example` to `.env` and fill in your values:

```bash
cp .env.example .env
```

```text
ISSUER=https://your-okta-domain/oauth2/default
CLIENT_ID=your-client-id
CLIENT_SECRET=your-client-secret
```

Find these values on your app's **General** tab in the Admin Console:
- **Issuer:** **Security > API**, then the authorization server's Issuer URI
- **Client ID / Secret:** **Client Credentials** section

## Enable Refresh Token

In the Admin Console, open your app's **General Settings** and check the **Refresh Token** grant type checkbox, then **Save**.

## Run

Load `.env` into your shell, then start the app:

```bash
set -a && source .env && set +a
./mvnw spring-boot:run
```

Open `http://localhost:8080` and click **Login**.

> This sample does not use a `.env`-loading library — `.env` only takes effect when you `source` it into the shell before running. If you see `Could not resolve placeholder 'ISSUER'` on startup, this step was skipped.

## How It Works

- `/` — Public home page. Shows a login button when unauthenticated.
- `/profile` — Protected page. Displays the claims from the user's ID token. Requires the `profile` scope.
- `/logout` — Ends the local session and performs OIDC logout at Okta.

`SecurityConfiguration` uses only `oauth2Login()` — no resource server configuration. The session is managed server-side; the browser receives a session cookie, **not** a token. The access token stays in the session on the server and is never exposed to the browser.

## Related Samples

- [okta-spring-boot-api](https://github.com/okta-samples/okta-spring-boot-api) — a standalone, token-protected API sample. It runs independently of this app and isn't called by it.
## Learn More

- [Okta Spring Boot Starter](https://github.com/okta/okta-spring-boot)
- [Authorization Code Flow](https://developer.okta.com/docs/guides/implement-grant-type/authcode/main/)
- [Spring Security OAuth2 Login](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/index.html)

[Okta Spring Boot Starter]: https://github.com/okta/okta-spring-boot
[Authorization Code Flow]: https://developer.okta.com/docs/guides/implement-grant-type/authcode/main/
