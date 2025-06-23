# 🎰 Slot Machine App

A modern Android slot machine game built with Jetpack Compose featuring animated spinning reels, configurable speeds, a points system, and currency conversion. Complete with coroutine-based animations and interactive gameplay mechanics.

## 🎯 What I Built
- **Interactive Slot Machine**: Three-reel slot machine with random symbol generation
- **Animated Spinning**: Smooth symbol animations using Kotlin Coroutines
- **Points System**: Earn points based on matching symbols with different reward tiers
- **Speed Control**: Adjustable spinning speed with Fast, Normal, and Slow options
- **Currency Conversion**: Convert earned chips to virtual dollars
- **Progressive Stopping**: Stop reels individually for strategic gameplay
- **Reset Functionality**: Clear game state and return to starting position

## ✨ Key Features
- 🎲 **Six Unique Symbols**: Bell, cherry, clover, diamond, lemon, and seven graphics
- ⚡ **Coroutine Animation**: Smooth, non-blocking spinning animations using Kotlin Coroutines
- 🏆 **Reward System**: 100 points for a jackpot (3 matches), 50 points for partial matches
- 🎚️ **Speed Adjustment**: Radio button controls for customizable spinning speeds
- 🛑 **Progressive Stops**: Stop each reel individually with strategic timing
- 💰 **Points Conversion**: Transform chips into virtual currency at 10:1 ratio
- 🔄 **Game Reset**: Return to the initial state with a single button press

## 🏗️ App Architecture
```
Android App Components:
├── MainActivity.kt        # Main activity and app entry point
├── Navigation System      # Type-safe routing between game and points screens
├── SlotMachineScreen.kt   # Main game interface with spinning mechanics
├── PointsScreen.kt        # Points display and currency conversion
├── Coroutine Management   # Background spinning animation handling
├── Symbol Resources       # Drawable assets for slot machine symbols
└── State Management       # Game state persistence and point tracking
```

## 🎮 Gameplay Mechanics
- **Spin**: Start all three reels spinning simultaneously
- **Stop Strategy**: Click STOP to halt reels one by one for strategic play
- **Speed Control**: Choose spinning speed before starting
- **Points Tracking**: Automatic point calculation based on symbol matches
- **Currency Exchange**: Convert accumulated chips to virtual dollars
- **Game Reset**: Clear all symbols and restart fresh

## 🔧 Technical Implementation
- **Kotlin Coroutines**: Non-blocking animations with proper lifecycle management
- **State Management**: Real-time tracking of spinning states and symbol positions
- **Random Generation**: Dynamic symbol selection using Kotlin's random functions
- **Compose Animation**: Smooth UI updates during spinning sequences
- **Job Management**: Proper coroutine cancellation and cleanup
- **Radio Button Controls**: User-configurable game speed settings
- **Navigation Compose**: Seamless transitions between game and points screens

## 📖 Learning Outcomes
This project demonstrates advanced Android development concepts:
- **Coroutine Programming**: Managing concurrent animations and background tasks
- **Game Development Patterns**: Implementing game loops, state machines, and reward systems
- **Animation Techniques**: Creating smooth, responsive UI animations in Jetpack Compose
- **Resource Management**: Proper handling of drawable assets and memory usage
- **User Interaction Design**: Implementing intuitive game controls and feedback systems
- **State Synchronization**: Coordinating multiple spinning elements and timing controls
- **Lifecycle Management**: Proper cleanup of coroutines and preventing memory leaks

## 🎰 Game Features Highlights
- **Visual Feedback**: Clear result messages with emoji indicators for wins and losses
- **Strategic Gameplay**: Individual reel stopping allows for skill-based play
- **Reward Progression**: Multiple win conditions with different point values
- **Professional UI**: Clean game interface with intuitive button placement and visual hierarchy

---
**Part of my Android development portfolio** | [Github Profile](https://github.com/AGButt04) | [LinkedIn](https://www.linkedin.com/in/abdul-ghani-butt-290056338/)
