# SiWANGAN

SiWANGAN is a comprehensive Android application designed to streamline tourism and local business (UMKM) management. It facilitates ticket booking, service reservations, and product purchases, simplifying the experience for both visitors and local administrators.

## 📱 Overview

SiWANGAN acts as a digital bridge between tourists and the local ecosystem. The application allows users to book tickets, order local products (UMKM), and reserve services effortlessly. It also includes an administration panel for managing orders and verifying payments.

## 🚀 Key Features

### User Features
*   **Secure Authentication**: robust login and registration system using Firebase Auth.
*   **Ticket Booking**: Browse and book entrance tickets seamlessly.
*   **UMKM & Services**: Discover and book local services or purchase products from MSMEs.
*   **Order History**: Track past and active bookings in real-time.
*   **Digital Tickets**: Generate QR codes for verified tickets for easy entry.
*   **Payment Proof**: Upload transfer receipts directly within the app for verification.

### Admin Features
*   **Order Verification**: Review and approve user payments and bookings.
*   **Management Dashboard**: Oversee ticket sales and UMKM orders.

## 🛠 Technology Stack

This project is built using modern Android development practices:

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **Minimum SDK**: API 26 (Android 8.0)
*   **UI Architecture**: XML Layouts with **ViewBinding** & **Jetpack Compose** integration.
*   **Backend Services**: **Firebase** (Authentication, Realtime Database, Firestore, Storage).

### Key Libraries
*   **Glide**: For efficient image loading and caching.
*   **ZXing**: For QR code scanning and generation.
*   **Chip Navigation Bar**: for a fluid bottom navigation experience.
*   **PhotoView**: To support zoomable images.

## 💻 Getting Started

To set up this project locally, follow these steps:

1.  **Clone the repository**
    ```bash
    git clone <repository-url>
    ```
2.  **Open in Android Studio**
    Open the project folder in the latest version of Android Studio.
3.  **Firebase Setup**
    Ensure you place your `google-services.json` file in the `app/` directory to connect to your Firebase project.
4.  **Sync & Build**
    Let Gradle sync dependencies, then build and run the app on your emulator or device.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

[License Information]
