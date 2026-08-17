# 🛡️ AESGuard v1.0.0

**Military-grade file encryption for Android** — Protect your sensitive files with AES-256 encryption powered by native C++ performance through JNI bridge.

## ✨ Features

- 🔐 **AES-256 Encryption** — Military-grade encryption algorithm
- 🚀 **Native C++ Performance** — Core encryption logic written in C++ for maximum speed
- 🌉 **JNI Bridge** — Seamless integration between Kotlin and C++
- 📱 **Modern Android UI** — Built with Jetpack Compose & Material Design 3
- 📂 **File Picker** — Encrypt any file type (images, PDFs, documents, videos)
- 🔑 **Password Protection** — Secure password-based encryption
- 🎨 **Beautiful Design** — Purple shield branding

## 🛠️ Tech Stack

- **Language:** Kotlin (Android) + C++ (Native)
- **UI Framework:** Jetpack Compose
- **Architecture:** Clean separation of concerns
- **Build System:** Gradle + CMake
- **Crypto:** Custom AES-256 implementation in C++
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 37 (Android 14)

## 📦 Project Structure

```
AESGuard/
├── app/src/main/
│   ├── cpp/
│   │   └── aes_encryptor.cpp    # AES-256 + file encryption
│   ├── java/com/aesguard/app/
│   │   └── MainActivity.kt      # UI + JNI bridge
│   └── res/
│       └── values/
│           └── theme/           # Purple Material Design theme
```

## 🚀 How to Use

1. **Pick a file** — Click "📁 Pick File" and select any file
2. **Enter password** — Type a secure password (min 4 characters)
3. **Click encrypt** — Your file is now encrypted with AES-256!
4. **Decrypt** — Switch to decrypt mode, pick the encrypted file, enter same password

## 🔐 Security

- **Algorithm:** AES-256 (Advanced Encryption Standard)
- **Key Size:** 256 bits
- **Password-based encryption** — Your password is never stored
- **Native C++** — Encryption runs in compiled native code for security

## 🏗️ Architecture

```
User Interface (Jetpack Compose)
         ↓
   Kotlin Layer (MainActivity)
         ↓ JNI
   C++ Native Library (aes_encryptor.cpp)
         ↓
   AES-256 Encryption Engine
```

## 📱 Screenshots

*Coming soon*

## 📄 License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Abir**
- GitHub: [@Abir0090](https://github.com/Abir0090)

## 🎉 Version History

- **v1.0.0** - Production release! 🎉
  - Real file encryption working
  - Beautiful Material Design UI
  - File picker integration
  - Clean architecture

## ⭐ Star this repo if you find it useful!
