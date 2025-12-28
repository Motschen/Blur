### Blur+ v5.3.2
- **always** check if blur effect is applicable
  - Should resolve all remainng instances of the `Can only blur once per frame` crash

### Blur+ v5.3.1
- Fix crash `java.lang.IllegalStateException: Can only blur once per frame` that occurred in edge cases

## Blur+ v5.3.0
- Switch to Stonecutter build system, which allows us to support multiple versions of Minecraft at the same time
  - Currently supported versions: 1.21.1, 1.21.5, 1.21.8, 1.21.10, 1.21.11 (on Fabric & NeoForge)
- Migrate to the official Mojang mappings, making the codebase future-proof
- Fix config not being saved correctly
  - This was hard to pinpoint, but I finally did it :)  
    Sorry for the long waiting time!