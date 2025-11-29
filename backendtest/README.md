# Exchange Rate Service – Backend Test

Service ini mengambil data exchange rate (USD terhadap IDR), menghitung nilai setelah spread berdasarkan GitHub username, lalu menyimpannya ke immutable in-memory store.  
Service juga menyediakan health checks custom Spring Actuator health indicator.

---

## 📌 Features

- Fetch currency rates melalui API eksternal
- Convert **IDR → USD → IDR per USD**
- Hitung spread berdasarkan GitHub username
- Immutable in-memory store (`ImmutableFinanceStore`)
- Standard REST API wrapper (`ApiResponse`)
- Endpoint **liveness & readiness**
- Custom Spring Actuator health indicator

---
## ⚙️ How It Works

### **1. Spread Calculation**

Spread dihitung berdasarkan ASCII sum dari GitHub username:
spread = (sum(username chars) % 1000) / 100000
Tipe data return: `BigDecimal` (scale = 6).

Spread kemudian diterapkan pada IDR-per-USD untuk menghasilkan nilai `usdBuySpreadIdr`.

---

## 🚀 How to Run the App

### **1. Clone Repo**
```
git clone <repo-url>
cd <folder-project>
```

### **2. Jalankan dengan Maven**
Spring Boot wrapper (Linux/Mac):
```
./mvnw spring-boot:run
```
Windows:
```
mvnw spring-boot:run
```

### **3. Build JAR**
```
./mvnw clean package
```
Jalankan JAR:
```
java -jar target/backendtest-0.0.1-SNAPSHOT.jar
```

## 🧪 Endpoints
Jalankan app lalu buka:
👉 **[Swagger UI](http://localhost:8181/swagger-ui.html)**


