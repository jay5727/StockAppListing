# 📈 StockListing App

A modern Android application built with **Jetpack Compose**, **MVVM**, and **Clean Architecture** that displays a user's stock holdings. This app fetches data from a remote source, caches it in a local Room database, and supports **offline access**.  

✅ **Fully tested** — Unit tests, UI tests, and database tests are all covered.

# 📷 Screenshots
![Image](https://github.com/user-attachments/assets/0828b4d3-5e0a-45cc-a37b-bb6a806eac34)

![Image](https://github.com/user-attachments/assets/01edc7ed-cb0c-4b1c-bc42-7ec1983fcc3d)

![Image](https://github.com/user-attachments/assets/94fc62e8-8441-4bbf-93e4-2eb5aac5bf8d)
---

## 🚀 Features

- 📊 Holdings list with LTP and P&L display
- 📉 Expandable Profit & Loss bottom sheet
- 📴 Full offline support via Room database
- 💡 Clean Architecture with UseCase & Repository layers
- 🧪 Full test coverage: ViewModel, Repository, UseCases, DAO & UI tests

## 🏛️ Clean Architecture

The app follows Clean Architecture principles to ensure separation of concerns, maintainability, and testability.

### Layers

1. **Presentation Layer**: Contains UI components built with Jetpack Compose, and ViewModels for managing UI state.  
2. **Domain Layer**: Contains business logic, including use cases and domain models.  
3. **Data Layer**: Manages data sources (API and local database) and handles data operations.


## 🛠️ Tech Stack

| Layer         | Libraries / Tools Used                     |
|--------------|---------------------------------------------|
| UI           | Jetpack Compose, Material3, StateFlow       |
| Architecture | MVVM, Clean Architecture, Hilt DI           |
| Persistence  | Room DB                                     |
| Networking   | Retrofit                                    |
| DI           | Hilt                                        |
| Testing      | JUnit, Mockito, Compose UI Testing          |

---

## 🧱 App Architecture

```text
com.jay.stocklistingapp
├── data
│   ├── model
│   ├── remote
│   ├── local (Room DB)
│   └── repository
├── domain
│   ├── model
│   ├── repository
│   └── usecase
├── presentation
│   ├── holdings
│   │   ├── ui (Composables)
│   │   ├── state
│   │   └── viewmodel
│   └── components (shared composables)
└── di (Hilt modules)
