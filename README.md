# Chaos Music Player: *"Feel Calm in Chaos"*

**Chaos Music Player** is a offline music player for Android, designed to fetch and play your local music files seamlessly. With a wide array of features like repeat mode, shuffle, search, favorites, playlists, and a sleep timer, Chaos provides a calm and personalized music experience—perfect for enjoying your music library exactly the way you like it.

[![Get the App](https://img.shields.io/badge/Get%20Chaos%20Music%20Player-Download-4CAF50?style=flat&logo=google-drive&logoColor=white)](https://drive.google.com/file/d/1j2WnoppEDhbhTkxawwy8pLrJ7P9kZqlw/view?usp=sharing)

## Features

- **Local Song Fetching**: Automatically scans and loads all music files from the device's storage.
- **Playlists**: Create and manage playlists for personalized music experiences.
- **Favorites**: Mark songs as favorites and access them easily.
- **Repeat & Shuffle**: Control the playback with repeat (one/all) and shuffle options.
- **Search**: Quickly search for songs in your library by title, artist, or album.
- **Sleep Timer**: Automatically stops playback after a set period of time.
- **Background & Foreground Music Playback**: Supports playing music in the background while the user interacts with other apps.
- **Media Controls**: Supports system media controls (play, pause, next, previous) with **MediaSessionCompat**.
- **Notifications**: Displays controls in notifications to manage playback.

## Technologies Used

- **Jetpack Compose**: A modern declarative UI toolkit for building native Android UIs 📱.
- **Hilt DI:** A dependency injection library for Android that reduces boilerplate and simplifies dependency management 💉.
- **Compose Navigation:** A library for navigating between composable screens within your Android app 🗺️.
- **Room Database**: Used to store the user's favorite songs and playlists. The many-to-many relationship model was implemented to handle songs being added to multiple playlists.
- **MVVM Architecture**: The app is structured following the Model-View-ViewModel pattern to separate concerns and maintain code modularity. The implementation is customized to suit the project’s needs.
- **MediaPlayer**: Android’s native `MediaPlayer` API is used for audio playback.
- **MediaSessionCompat**: Integrated to manage media controls and handle interactions with other system media components.
- **Background, Foreground, and Bound Services**: Managed playback using Android's service lifecycle to keep the player active in the background and foreground.
- **WorkManager**: Integrated for managing scheduled tasks like the sleep timer.
- **Broadcast Receivers**: Used for handling system events such as headphone plug/unplug and managing playback accordingly.
- **PendingIntent**: Utilized for controlling actions from notifications, media buttons, and widgets.

## Learning Experience

While developing **Chaos Music Player**, I gained practical knowledge of essential Android components, including:

- **Services**: Background, foreground, and bound services to manage playback.
- **WorkManager**: Used WorkManager in Chaos Music Player to schedule reliable background tasks, manage task chaining, and optimize performance under system constraints.
- **PendingIntent & Notification**: For controlling media playback outside the app (e.g., from lock screen or notifications).
- **BroadcastReceiver**: To handle external events like headphone plug/unplug and managing play/pause state.
- **MediaSessionCompat**: Integrating system-wide media controls across different Android API levels, which posed challenges due to scarce resources.
- **Room DB with Many-to-Many Relationship**: Implemented Room database to store favorite songs and playlists with a many-to-many relationship between songs and playlists.
  
Developing this app provided me with insights into how Android features work differently on various API levels and how to implement backward-compatible solutions.

## Future Enhancements

- **Equalizer**: Adding an in-built equalizer for users to tweak audio settings.
- **Themes & UI Enhancements**: Incorporating more visual themes to provide users with customization options.
- **Cloud Sync**: Enabling cloud sync for playlists and favorites across devices.
- **Widget Support**: Adding a home screen widget for easy access to playback controls.
