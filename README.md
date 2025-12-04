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
git clone https://github.com/Echo1121-Z/CW2025.git
cd CW2025
- **Compile and Run Using Maven**:
mvn clean:clean
mvn javafx:run
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
- **Location**: src/main/java/com/comp2042/logic/bricks/GuiController.java
- **Description**: Refactored the game main loop and state management mechanism.
- **Status**: Fully functional
- **Improvement Content**:
Optimized game loop to ensure stable 60FP operation.
Refactored game state management (Start, Running ,Paused, End state transitions).
Improved exception handling and error recovery mechanisms.
#### 3) Collision Detection System Operation
- **Location**: src/main/java/com/comp2042/logic/bricks/GuiController.java
- **Description**: Refactored and optimized collision detection algorithms, improving accuracy and performance.
- **Status**: Fully functional.
- **Technical Improvements**: 
Used precise Bounding Box calculations instead of original simple detection.
Implemented spatial partitioning optimization to reduce unnecessary collision checks.
Fixed boundary detection errors and rotation collision issues in the original code.



