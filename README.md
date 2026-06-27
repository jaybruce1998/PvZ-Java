# Java Plants vs Zombies

A compact, original Java/Swing re-creation of the core `Plants vs Zombies` lane-defense loop with a full 50-level campaign.

## Features

- 50-level campaign with `Day`, `Night`, `Pool Day`, `Pool Night`, and `Roof` stages
- Unlockable plants: `Sunflower`, `Peashooter`, `Wallnut`, `Cherry Bomb`, `Snow Pea`, `Puff-shroom`, `Repeater`, `Lily Pad`, `Flower Pot`
- Starting sun fixed at `75` every level
- Seed packet recharge timers and hold-to-collect sun input
- Auto-save after each cleared level and restore progress on launch
- Larger zombie roster with special variants including `Flag`, `Pole Vault`, `Newspaper`, `Screen Door`, `Football`, `Ducky Tube`, `Snorkel`, `Dolphin Rider`, `Ladder`, `Pogo`, and `Gargantuar`
- Added `Jack-in-the-Box` and `Zomboni` specials plus point-based rush spawning
- Early waves always open with `1` threat point, then `2`, before scaling normally
- Pool and roof support tiles
- Select-to-place flow that resets after each plant placement
- Win, loss, and campaign-complete states
- Hand-drawn vector-style visuals with no external assets

## Run

Compile:

```powershell
javac -d out src\pvz\*.java
```

Build runnable jar:

```powershell
.\build-jar.bat
```

Launch:

```powershell
java -cp out pvz.Main
```

Launch with a game-speed multiplier:

```powershell
java -cp out pvz.Main 1.5
```

Prompt for speed and run the jar:

```powershell
.\run-game.bat
```

## Controls

- Left-click a card to select a plant
- Left-click a lawn tile to place the selected plant
- Right-click a tile to remove a plant
- Click or hold near a sun to collect it
- Press `1` through `9` to select the corresponding seed slot
- Click after a win to continue
- Press `R` to retry the current level or restart after the campaign ends
