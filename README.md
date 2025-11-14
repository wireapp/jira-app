jira-app
============

**Description**

A lightweight Kotlin service that integrates with Wire's SDK. Combines Javalin (HTTP server), Koin (DI), and Wire SDK to handle incoming messages and expose REST endpoints.

**Tech Stack**

- Kotlin 2.2.0 / JDK 21
- Javalin 6.7.0 (web framework)
- Koin 4.1.1 (dependency injection)
- Wire Apps JVM SDK 0.0.18

**Architecture: Lightweight Clean Hex**

Organized in three layers:
- **Core** (`com.wire.apps.jira.core`): Domain logic, use cases, interfaces
- **API** (`com.wire.apps.jira.api`): HTTP controllers and message handling
- **Infrastructure** (`com.wire.apps.jira.infra`): External clients (HTTP, Wire SDK), config, implementations

Wire SDK's `WireApplicationManager` is initialized in the DI layer and starts listening for incoming messages at startup.

**Getting Started**

**Requirements**
- JDK 21
- Gradle (included: `./gradlew`)

**Environment Variables**

Set these before running the app:

Jira related
- JIRA_TOKEN
- JIRA_URL
- JIRA_USERNAME

SDK related 
- WIRE_SDK_EMAIL
- WIRE_SDK_ENVIRONMENT
- WIRE_SDK_PASSWORD

**Build & Run**

```bash
# Build
./gradlew build

# Run (listens on port 8888 by default)
java -jar build/libs/javalin-app-1.0-SNAPSHOT.jar
```

Alternatively, set env vars and start:
```bash
export WIRE_API_TOKEN=your-token
export WIRE_CRYPTO_PASSWORD=your-32-byte-password
java -jar build/libs/javalin-app-1.0-SNAPSHOT.jar
```

**Notes**
- Port 8888 is hardcoded in `Main.kt`; update if needed.
- See `ModulesConfig.kt` for Wire SDK initialization and DI setup.
- Message handling is mapped in `CommandHandler` → routed to use cases in `core/`.
