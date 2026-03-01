# LifeSteal Plugin for Paper 1.21

A comprehensive LifeSteal plugin for Minecraft 1.21 that implements heart-stealing mechanics, elimination system, and heart item crafting.

## Features
- **Heart Theft**: Kill players to steal their hearts (+1 max health to killer, -1 max health to victim)
- **Elimination System**: Players reaching minimum health are eliminated (set to Spectator mode)
- **Heart Items**: Craftable hearts using Nether Star recipe that increase max health when consumed
- **Health Conversion**: Convert current health to Heart items using `/lifesteal withdraw`
- **Persistence**: Player health data saved across restarts
- **Safety Checks**: Ensures only player kills count, prevents health from going below minimum

## Commands
- `/lifesteal withdraw <amount>` - Convert current health to Heart items

## Permissions
- `lifesteal.use` - Use LifeSteal commands (default: true)
- `lifesteal.admin` - Administrative permissions (default: op)

## Installation
1. Build the plugin using Maven: `mvn clean package`
2. Copy the generated JAR from `target/` to your Paper server's `plugins/` directory
3. Restart your server

## Crafting Recipe
- Shape: 3x3 grid
- Center: Nether Star
- Surrounding: Gold Blocks
- Result: Heart item

## Health Limits
- Minimum: 1 heart (2 HP)
- Maximum: 20 hearts (40 HP)