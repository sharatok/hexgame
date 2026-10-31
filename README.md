# Hex Strategy Game

A turn-based 4X strategy game on a hexagonal grid, built in Java with Swing.

## Project Structure

```
hexgame/
├── pom.xml                              # Maven configuration
├── config.properties                    # Window size and camera keys
├── .github/workflows/build.yml          # CI: native installers + GitHub Release
├── src/main/java/com/hexgame/
│   ├── Main.java                        # Entry point (opens the main menu)
│   ├── MainMenu.java                    # Start menu: game modes, settings, exit
│   ├── HexGrid.java                     # Game state, rules, input and rendering
│   ├── AIEngine.java                    # AI players: economy, military, naval invasions
│   ├── Tile.java                        # Hex cell: terrain, yields, visibility, radiation
│   ├── Unit.java                        # Unit base class and all unit types
│   ├── Building.java                    # Building base class and all building types
│   └── DummyTarget.java                 # Stationary target used in Dev Mode
└── README.md
```

## Game Modes

Chosen from the main menu:

- **Normal Game**: you against AI opponents, 4 bases on a 50x32 map.
- **Dev Mode (Test Units)**: 2 bases, 9999 Wood/Iron/Gold/Uranium for testing. The enemy base is surrounded by Dummy Targets (1000 HP) instead of real units.
- **AI vs AI (Spectate)**: two AI players fight on their own, one turn per second.
- **Settings / Controls**: shows the control reference.

## Controls

| Input          | Action                                                      |
|----------------|-------------------------------------------------------------|
| Arrows / WASD  | Scroll the map (configurable)                               |
| Left click     | Select unit / move / attack                                 |
| Right click    | Open the build / production menu                            |
| Double-click   | Toggle building power / detonate nuke / unload Sky Ship     |
| `B`            | Toggle build mode for the selected Builder                  |
| `Enter`        | End turn                                                    |
| `Esc`          | Exit to main menu                                           |
| `Space`        | Pause / resume (AI vs AI)                                   |
| `Tab`          | Switch which AI player's view is shown (AI vs AI)           |

## Units

| Unit         | Moves | Vision | HP  | Strength | Range | Built at        |
|--------------|-------|--------|-----|----------|-------|-----------------|
| Scout        | 3     | 4      | 100 | 60       | 1     | Barracks        |
| Builder      | 2     | 2      | 100 | civilian | –     | Barracks        |
| Legion       | 2     | 2      | 150 | 85       | 1     | Barracks        |
| Mortar Group | 2     | 3      | 80  | 75       | 2     | Barracks        |
| Nuke Carrier | 2     | 2      | 20  | –        | –     | Barracks        |
| Tank         | 3     | 3      | 300 | 120      | 1     | Vehicle Factory |
| Sky Ship     | 6     | 4      | 200 | –        | –     | Vehicle Factory |
| Headquarters | 0     | 5      | 500 | 40       | 1     | (start)         |

- Each player starts with an HQ, a Scout and a Builder.
- **Sky Ship** carries up to 6 units and can cross ocean; double-click to unload.
- **Nuke Carrier**: double-click to detonate. It destroys every unit and building on its hex and the 6 around it, and leaves a radioactive dead zone for 30 turns where nothing can be built or produced.

## Buildings

| Building        | Effect                                                |
|-----------------|-------------------------------------------------------|
| Logger Station  | Produces Wood (uses 5 Energy)                         |
| Mining Station  | Produces Coal, Iron, Gold or Uranium (uses 15 Energy) |
| Burning Station | Burns Coal (5 → 25 Energy) or Wood (15 → 15 Energy)   |
| Solar Plant     | Produces 10 Energy                                    |
| Nuclear Plant   | Burns 10 Uranium → 100 Energy                         |
| Barracks        | Trains infantry units (uses 10 Energy)                |
| Vehicle Factory | Builds Tanks and Sky Ships (uses 20 Energy)           |

Buildings that use Energy can be switched off with a double-click. Build Burning Stations early — production stalls without power.

## Terrain & Resources

| Terrain  | Yields                              | Building efficiency |
|----------|-------------------------------------|---------------------|
| Plains   | None                                | 100%                |
| Forest   | Wood (3.0–5.5)                      | 75%                 |
| Mountain | Coal (1.0–2.5), Iron (0.7–1.0)      | 50%                 |
| Desert   | Solar Energy                        | 70%                 |
| Tundra   | Gold (9.0–12.0), Uranium (0.15–0.50)| 60%                 |
| Ocean    | None (no buildings)                 | 0%                  |
| Base     | None                                | 100%                |

Other features: fog of war, A* pathfinding with move preview, and a per-player economy of Coal, Gold, Iron, Wood, Uranium and Energy.

## Build & Run

### Prerequisites
- Java 11+
- Maven 3.6+

### Run from source
```bash
mvn clean compile exec:java
```

### Build and run the JAR
```bash
mvn clean package
java -jar target/hex-strategy-game.jar
```

## Configuration

`config.properties` is read from the working directory at startup. If it's missing, the defaults are used.

| Key              | Default   | Meaning                                         |
|------------------|-----------|-------------------------------------------------|
| `screen.width`   | `980`     | Game view width in pixels (max 1916, the map)   |
| `screen.height`  | `750`     | Game view height in pixels (max 1111, the map)  |
| `camera.up`      | `W UP`    | Keys that scroll up                             |
| `camera.down`    | `S DOWN`  | Keys that scroll down                           |
| `camera.left`    | `A LEFT`  | Keys that scroll left                           |
| `camera.right`   | `D RIGHT` | Keys that scroll right                          |

Key names are Java `KeyEvent.VK_*` names without the `VK_` prefix, separated by spaces.

## Releases

The GitHub Actions workflow builds native installers (`.exe`, `.dmg`, `.deb`) with `jpackage` and publishes them as a GitHub Release:

- **Tag push**: push a tag like `v1.2.0` to release version `1.2.0`.
- **Manual run**: start "Build Native Installers" from the Actions tab and enter a version. The workflow creates the `v<version>` tag on the built commit.

Each version can be released only once.
