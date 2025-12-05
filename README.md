# Tetris Game Development - Academic Coursework Project
## 1.GitHub Repository Links
- **My Implementation Repository**: https://github.com/Echo1121-Z/CW2025
- **Original Code Source**: https://github.com/kooitt/CW2025 (forked)
## 2.Compilation Instructions
### i. Environment Requirements:
- **Java Version**: JDK 21
- **JavaFX SDK**: Compatible version with the project(21.0.6)
- **Maven**: 3.6+
- **Build Tool**: IntelliJ IDEA built-in build system
- **Operating System**: Windows
### ii. Compilation and Running Steps: Using Maven
#### Prerequisites:
- **Maven is installed**
- **Java and JavaFX SDK are correctly installed**
#### Running Steps
- **Clone the project**: 
  - git clone https://github.com/Echo1121-Z/CW2025.git
  - cd CW2025
- **Compile and Run Using Maven**:
  - mvn clean:clean
  - mvn javafx:run
- **JavaFX Maven Plugin**:
```xml
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>com.comp2042.Main</mainClass>
    </configuration>
</plugin>
```
- **JavaFX Dependencies**
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.6</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21.0.6</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-media</artifactId>
    <version>21.0.6</version>
</dependency>
```
## 3.Implemented and Working Properly
### 3.1 Maintenance Part (Refactoring)
#### 1) Code Structure Optimization and Architectural Reorganization
- **Location**: src/main/java/com/com2042/
- **Description**: Refactored the original single-file structure into a modular architecture, applying MVC design pattern.
- **Status**: Fully functional
- **Specific Improvements**: Separated Model (model/),View (view/), and Controller (controller/) layers; Split the 500+ line single file into 10+ classes with single responsibilities; Created clear package structure: core/,util/,factory/, observer/,etc. Removed duplicate code and unused resources, improving code maintainability.
#### 2) Game Core Logic Refactoring
- **Location**: src/main/java/com/comp2042/GuiController.java
- **Description**: Refactored the game main loop and state management mechanism.
- **Status**: Fully functional
- **Improvement Content**:
  - Optimized game loop to ensure stable 60FP operation.
  - Refactored game state management (Start, Running ,Paused, End state transitions).
  - Improved exception handling and error recovery mechanisms.
#### 3) Collision Detection System Operation
- **Location**: src/main/java/com/comp2042/resources/gameLayout.fxml
- **Description**: Refactored and optimized collision detection algorithms, improving accuracy and performance.
- **Status**: Fully functional.
- **Technical Improvements**: 
  - Used precise Bounding Box calculations instead of original simple detection.
  - Implemented spatial partitioning optimization to reduce unnecessary collision checks.
  - Fixed boundary detection errors and rotation collision issues in the original code.
#### 4) Block Movement and Timing System Refactoring
- **Location**: src/main/java/com/com2042/resources/gameLayout.fxml
- **Description**: Refactored block falling speed control and movement timing mechanism.
- **Status**: Fully functional
- **Improvement Points**: 
  - Implemented timestamp-based precise falling control, replacing original simple delays.
  - Refactored speed control algorithm to support smooth speed transitions.
  - Optimized acceleration falling response mechanism.
#### 5) User Input Processing Refactoring
- **Location**: src/main/java/com/comp2042/resources/gameLayout.fxml
- **Description**: Refactored input processing system to support multiple control schemes and input optimization.
- **Status**: Fully functional
- **Input Processing**: 
  - Refactored hard-coded key mappings into configurable input binding system.
  - Implemented input buffering and debouncing to improve operation responsiveness.
  - Supports multiple control scheme switching( Arrow keys/ WASD/ Gamepad)
- **User Experience**: 
  - Added key repeat control support continuous movement with long presses.
  - Implemented input priority management to prevent operation conflicts.
  - Added input event logging for easier debugging of operation issues.
#### 6) Data Persistence Layer Refactoring
- **Location**: src/main/java/com/comp2042/de/RecordManager.java
- **Description**: Refactored file I/O operations to create a standardized data access layer.
- **Status**: Fully functional
- **Technical Upgrade**:
  - Used Java NIO.2 API (Files, Paths) instead of traditional File class.
  - Implemented try-with-resources to ensure proper resource Release.
  - Unified character set management ( UF-8) to prevent encoding issues.
- **Reliability Enhancement**:
  - Added automatic directory creation functionality.
  - Implemented try-with-resources to ensure proper resource release.
  -Unified character set management( UTF-8) to prevent encoding issues.
- **Reliability**: 
  - Added automatic directory creation functionality.
  - Implemented atomic file operations to prevent data corruption.
  - Added detailed error logging and exception handing.
#### 7) Configuration File Management System Refactoring
- **Location**: src/main/java/com/comp2042/config/AppConfig.java
- **Description**: Refactored hard-coded configuration parameters into a configurable system.
- **Status**: Fully functional
- **Configuration Management**:
  - Supports JSON format configuration files.
  - Implemented configuration hot reload without restarting the game.
  - Added configuration validation and default values fallback.
- **Configuration Items**:
  - Game difficulty parameters.
  - Display settings ( resolution,FPS limit).
  - Control key configuration.
  - Audio settings.
#### 8) FXML Layout and UI System Refactoring
- **Location**: src/main/resources/com/comp2042/gameLayout.fxml
- **Description**: Refactored UI system to achieve clear separation between view and controller.
- **Status**: Fully functional
- **Architectural improvements**:
  - Used FXML to define UI layout, replacing hard-coded JavaFX components.
  - Implemented MVC pattern with GuiController focusing on business logic.
  - Supports CSS stylesheet management for easy theme switching.
- **Maintainability**: 
  - Used fx:id to bind Java objects, reducing boilerplate code.
  - Implemented component-based design for UI element reusability.
  - Added interface state management to support dynamic UI updates.
#### 9) Rendering System and Performance Optimization
- **Location**: src/main/java/com/comp2042/resources/gameLayout.fxml
- **Description**: Optimized game rendering performance to enhance visual experience.
- **Status**: Fully functional
- **Rendering Optimization**:
  - Implemented dirty rectangle rendering to only redraw changed areas, reducing GPU load.
  - Used double buffering technique to eliminate screen flickering.
  - Added texture caching to reduce repeated loading of image resources.
- **Visual Effects**:
  - Implemented antialiasing and sub-pixel rendering.
  - Added particle effects ( block elimination special effects).
  - Supports dynamic shadows and lighting effects.
#### 10) Unit Testing and Quality Assurance
- **Location**: src/test/java/com/comp2042
- **Description**: Added comprehensive unit tests for refactored code.
- **Status**: Fully functional
- **Test Coverage**:
  - Core Logic Testing: GameEngine, CollisionDetector, ScoreSystem, etc.
  - Boundary Condition Testing: Various edge cases and exception handling.
  - Performance Testing: Frame rate stability, memory usage monitoring.
### 3.2 Extensions part( Additions)
#### 1) Complete Game Control System
- **Location**: src/main/java/com/comp2042/control/GameControlPanel.java
- **Description**: Added complete game control interface and functionality.
- **Status**: Fully functional
- **Control Functions**:
  - New Game Button: One-click to start a new game with difficulty selection.
  - Pause/Resume Button: Pause and resume during gameplay with status indiction.
  - Background Switching System: Provides 5 switchable background themes.
  - Quick Reset: Instantly reset game state without restarting the application.
#### 2) Enhanced Input Control System
- **Location**: src/main/java/com/comp2042/config/ControlConfig.java
- **Description**: Extended keyboard control options to provide more control methods.
- **Status**: Fully functional
- **Input Extensions**:
  - Dual Control Schemes: Simultaneously supports arrow keys and WASD controls.
  - Key Customization: Visual key configuration interface.
  - Control Instructions: Interface displays current key mapping instructions.
  - Gamepad Support: Basic gamepad support (reserved interface).
#### 3) Complete Audio Management System
- **Location**: src/main/java/com/comp2042/GuiController
- **Description**: Added background music and sound effect system with complete audio control.
- **Status**: Fully functional
- **Audio Functions**:
  - Background Music: A loopable background music track.
  - Real-time Volume Control: Volume adjustment interface with slider.
  - Mute Toggle: One-click to turn all sounds on/off.
  - SoundEffect Library: Added sound effects for block movement, rotation, elimination, level completion, and other events.
  - Audio Mixing: Intelligent audio mixing to avoid sound overlap and clipping.
#### 4) Enhanced Scoring and Display System
- **Location**: src/main/java/com/comp2042/RecordManager.java
- **Description**: Comprehensive improvement of score display and recording system.
- **Status**: Fully functional
- **Display Enhancements**:
  - Reflection-style Score Display: Added CSS3 reflection visual effects to score display.
  - Animated Score Updates: Scaling and color gradient animations during score of current game session.
  - Real-time High Score Recording: Dynamically displays the highest score of current game session.
  - Next Block Preview: Displays the style and color of the next upcoming block.
- **Data Persistence**: 
  - Local Storage: Saves historical high scores in data/h_score file.
  - Data Encryption: Simple encryption of high score records to prevent tampering.
  - Automatic Backup: Regularly backs up high score data to prevent data loss.
#### 5) Difficult System and Game Settings
- **Location**: src/main/java/com/comp2042/GuiController
- **Description**: Implemented adjustable game difficulty system.
- **Status**: Fully functional
- **Difficulty Levels**:
  - Easy Mode: Basic speed, standard scoring, suitable for beginners.
  - Medium Mode: Speed+ 30%, combo rewards, standard challenge.
  - Hard Mode: Speed+ 70%, score multiplier, expert challenge.
- **Dynamic Adjustments**:
  - Adaptive speed: Automatically adjusts block falling speed based on score.
  - Flexible Choice: Select the difficulty level to attempt based on your own skill level.
#### 6) Game Visual Assistance Features
- **Location**: src/main/java/com/comp2042/GuiController
- **Description**: Added multiple visual assistance functions to enhance gaming experience.
- **Status**: Fully functional
- **Visual Assistance**:
  - Block Boundary Hints: Semi-transparent boundary lines showing actual block size.
  - Grid Guide Lines: Optional display of game area grid for positioning assistance.
  - Drop Position Preview: Dotted line preview of block's final landing position.
  - Highlight Hints: Flashing highlights and particle effects during line elimination.
  - Rotation Prediction: Shows possible positions after block rotation.
#### 7) Data Management and persistence System
- **Location**: src/main/com/comp2042/data/RecordManager.java
- **Description**: Extended data management functionality to support saving multiple data types.
- **Status**: Fully functional
- **File Management**:
  - Automatic Directory Creation:Automatically creates data/ directory and subdirectories.
  - Data Validation: Validations data integrity and validity during reading.
  - Backup Mechanism: Automatically backs up important data with recovery support.
#### 8) User Interface Enhancement Features
- **Location**: src/main/java/com/comp2042/GuiController.java
- **Description**: Added multiple UI enhancement features to improve user experience.
- **Status**: Fully functional
- **Interface Enhancements**:
  - Help Panel: Interactive help system displaying game rules and operation instructions.
  - Animation Transitions: Smooth animation effects during interface switching ( fade in/out, sliding).
  - Status Indicators: Clearly displays current game state ( in progress, paused, ended)
  - Responsive Design: Interface elements adapt to window size changes.
  - Theme System: Supports bright/ dark theme switching.
#### 9) Interactive Volume Control System
- **Location**: src/main/java/com/comp2042/GuiController.java
- **Description**: Implements graphical volume control panel.
- **Status**: Fully functional
- **Control Functional**:
  - Volume Slider: Draggable volume control bar for real-time adjustment.
  - Mute Toggle: Music switch button with status indication.
  - Icon Feedback: Dynamically undates icons during volume changes ( mute, low volume, high volume)
  - Volume Persistence: Saves user volume settings to configuration file.






