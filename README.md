# Full Stack Project: Spring Boot Backend + Android XML Frontend

This project features a **Spring Boot (Maven)** backend API and an **Android (XML-based)** frontend UI. The backend provides RESTful services while the Android app serves as the client interface. Perfect for full stack development and mobile-first applications! 🚀

---

## Project Structure

```
root/
│
├── Backend/           # Spring Boot backend (Maven)
│   └── TriviaBum/src/...
│
└── Frontend/       # Android frontend (XML UI)
    └── AndroidApp/app/...
```

---

## Prerequisites

Make sure you have the following installed:

### Backend
- Java 17+
- Maven 3.6+
- IDE (e.g. IntelliJ IDEA, VS Code)

### Frontend
- Android Studio 
- Android SDK 33+
- Gradle (handled by Android Studio)

---

## How to Run the Backend (Spring Boot with Maven)

1. **Navigate to the backend directory**:
   ```bash
   cd Backend
   ```

2. **Build and run the application**:
   ```bash
   mvn spring-boot:run
   ```

   Alternatively, you can build the JAR:
   ```bash
   mvn clean package
   java -jar target/your-backend-app.jar
   ```

3. **API is now available at**:
   ```
   http://localhost:8080
   ```

   You can test the endpoints using Postman or a browser.

---

## How to Run the Frontend (Android XML UI)

1. **Open the project in Android Studio**:
   - Go to `File > Open` and select the `android-app/` directory.

2. **Let Gradle sync** and download all required dependencies (this may take a few minutes).

3. **Connect an Android device** or start an emulator.

4. **Run the app**:
   - Press the green ▶Run button or use `Shift + F10`.

5. **App will launch on your device/emulator** and connect to the backend if properly configured.

---

## Connecting Backend & Frontend

Make sure the Android app is set to hit the correct backend URL, typically via a `BASE_URL` constant in your code.

For example:

```java
public static final String BASE_URL = "http://10.0.2.2:8080"; // For Android emulator
```

Use `10.0.2.2` instead of `localhost` when accessing your local backend from the Android emulator.

---

## Notes

- Ensure both backend and frontend are running on the same network if testing on a physical device.
- Use tools like [ngrok](https://ngrok.com/) if you want to expose the backend to the public for real device testing.

---

## Contributions

Pull requests are welcome! Feel free to fork this project and submit improvements.

---
