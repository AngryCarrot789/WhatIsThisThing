# DO NOT RUN setup.sh... YET!
It downloads the mcp62 environment, but to save some space on your PC in case you
make a few more mods in 1.2.5, we will move the setup file to a better location. 
Don't worry it will only take a few minutes... or hours.

The original file is not mine. Many thanks to NeRdTheNed for creating it: https://github.com/NeRdTheNed/SetupOldMCP-FML

>**TLDR**: Place setup.sh in the same folder that you placed the Witt mod and run it. Assuming extra lazy, 
> reinstall technic launcher to D:/.technic and install tekkit classic. Open project in IntelliJ 
> IDEA and click Run or Debug. Creating production .jar requires following the whole list below unfortunately

For example, say you have a folder called `ModMaking`, place setup.sh and Witt into that folder, such that these files exist:
- `ModMaking/setup.sh`
- `ModMaking/Witt/src/mod_Witt.java`

Now, actually run `setup.sh`. This should create two folder: `downloads` and `mcp62`.

Then, run `ModMaking/mcp62/forge/install.cmd`.

### Errors when running install.cmd
You might encounter some errors while it tries to recompile the server, talking about 'worldMngr'.
To fix this, open `ModMaking/mcp62/src/minecraft_server/net/minecraft/server/MinecraftServer.java` #
and uncomment the code on line 78, such that the line becomes:
- `public WorldServer[] worldMngr;` 
  
Then, run `ModMaking/mcp62/updatemd5.bat` and you should be all good to go.

### Opening the Witt project
This project requires IntelliJ IDEA. I created this using 2021.1.3, however, it works on newer version too.
Just make sure you have java 8 installed. This project targets the level 6 language level, so that you don't
accidentally use lambdas (apparently the reobfuscator doesn't support it, or maybe it does, it didn't work when I tried it)

### Mod Dependencies
This project also uses other mods as optional dependencies, but are required for compilation. 
Press `CTRL+SHIFT+ALT+S` and go to `Modules` and the `Dependencies` tab. There you might see some red
jar files, because my Technic Launcher is installed at `D:/.technic`, whereas yours
may be at `C:/Users/<USERNAME>/AppData/Roaming/.technic`.

The simple fix is to use IntelliJ's search and replace, and make sure file mask is enabled (top-right corner) and is set to `*.iml`, and
replace `D:/.technic` with the actual path of your technic folder. 

For most people, you replace `D:/.technic` with `C:/Users/<USERNAME>/AppData/Roaming/.technic` (and obviously replace `<USERNAME>` with your actual username first)

### Compiling
If you followed the mcp download and install instructions, and fixed the server recompile errors if you got any,
and replaced the technic folder locations in the .iml files, you should be able to compile without issue.

Click `Build>Build Artifacts>Witt:jar`, this will create a jar file in `ModMaking/mcp62/jars/mods` ready for debugging.

### Debugging
The `Client` run configuration should be all ready to go to start debugging the mod. Remember to build the Witt:jar artifact of course. 
Beware though, you will be debugging in MCP code mappings, not obfuscated code, therefore you cannot install the 
mod into tekkit classic just yet. That requires the next section

### Creating obfuscated jar ready for tekkit classic
- In IntelliJ, click `Build>Build Artifacts...>All Artifacts`
- Run `ModMaking/mcp62/reobfuscate.bat`
- Open `ModMaking/mcp62/jars/mods/<mod jar>` with an archive program (e.g. 7zip), and delete the folder `reghzy` and the file `mod_Witt.class`, and copy the same folder and file from `ModMaking/mcp62/reobf/minecraft/` into the jar.
- Copy `ModMaking/mcp62/jars/mods/<mod jar>` to your tekkit classic's mods directory and run the game

At some point, once I can figure out how to, I will make maybe an Ant script to do all of this for us, since it's tedious do this over and over again to test things

> #### Explanations:
> Step 1: copies the compilation output into `ModMaking/mcp62/bin/minecraft`, which is where the deobfuscated but compiled minecraft client source code is stored.
> 
> Step 2: obfuscates the minecraft parts that the mod uses, e.g. ItemStack, but will leave the rest of the code alone. This is a required step, because production minecraft is obfuscated
> 
> Step 3: we replace the un-obfuscated code, in the jar, with the obfuscated code, in the reobf folder. Deleting the original folder and file in the jar probably isn't necessary, but I do it anyway.
> 
> Step 4: install the mod ^-^