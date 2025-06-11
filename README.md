# 📈 StockListing App 🔥🔥🔥🔥🔥

A modern Android application built with **Jetpack Compose**, **MVVM**, and **Clean Architecture** that displays a user's stock holdings. This app fetches data from a remote source, caches it in a local Room database, and supports **offline access**.  

✅ **Fully tested** — Unit tests, UI tests, and database tests are all covered.

# 📷 Screenshots
| Screenshot 1 | Screenshot 2 |
|--------------|--------------|
| ![Image](https://github.com/user-attachments/assets/ab996f1f-b8d6-493d-98d4-e450082c1a47) | ![Image](https://github.com/user-attachments/assets/f4209dd8-1e7e-4a52-8327-83707344e74c) |

| Screenshot 3 | Screenshot 4 |
|--------------|--------------|
| ![Image](https://github.com/user-attachments/assets/84db1d18-5a8d-400f-a67a-5f21d454c810) | ![Image](https://github.com/user-attachments/assets/299fc562-bd72-462d-8ae6-133b6ea13fd2) |

| Screenshot 5 | Screenshot 6 |
|--------------|--------------|
| ![Image](https://github.com/user-attachments/assets/96169cb7-8019-4aee-bbc0-44acbf3afc48) | ![Image](https://github.com/user-attachments/assets/eeefeaa4-d40c-403e-be94-6b580d82a7da) |
---

## 🚀 Test Case Coverage

✅ Includes complete **JUnit** & **Jetpack Compose UI tests** with full screen validation!  
<img width="560" alt="Image" src="https://github.com/user-attachments/assets/0728ffe2-9558-4c0e-ae07-1794e452b390" />

![Image](https://github.com/user-attachments/assets/e86d50b8-623b-44bb-a8e1-de41e8b1f368)


## 🚀 Features

- 📊 Holdings list with LTP and P&L display
- 📉 Expandable Profit & Loss bottom sheet
- 📴 Full offline support via Room database
- 💡 Clean Architecture with UseCase & Repository layers
- 🧪 Full test coverage: ViewModel, Repository, UseCases, DAO & UI tests
- 🌙 This app supports full dark mode, including adaptive backgrounds and text colors!

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
