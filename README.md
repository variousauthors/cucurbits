# mod-scaffolding
The idea here is you can clone this repo, run the commands in the README and be ready to go. This is just an implementation of [Shadowfacts' 1.12 modding tutorial][0]. The steps described below are basically cribbed from [the forge docs][1]. This scaffold is for minecraft version 1.12.2 and forge version 14.23.5.2847 because those are the versions I am using in the modpack I maintain. So watch out for that log4j is all I'm saying!

## Setup - IntelliJ IDEA

```
./gradlew setupDecompWorkspace idea
```

This command will generate a .ipr file. Open this file with IDEA. In the event log there will be a link indicating that you can load a gradle project. Click it. Once the gradle project has loaded, run the `mod-scaffolding -> Tasks -> forgegradle -> genIntellijRuns` gradle task.

This task will add two run configurations to your project, but they will not work. Click on Edit Configurations in the run configuration dropdown menu. Edit the client and server run configs to by setting the module to `mod-scaffolding.main`. Apply your changes and click OK.

If you run the client now it will load, but forge will fail to find your resources and all your modded stuff will be textureless. In the gradle sidebar click the wrench icon and open Gradle Settings. By default the Build and Run section will be set to `Gradle (Default)`. Change it to `IntelliJ IDEA`.

If you run the client now it will fail to start because you haven't agreed to the eula.txt file. Find that in the /run folder and change `eula=false` to `eula=true`.

Finally, run the client and make sure everything worked.

## Conclusion

That's it! Now point your git remote origin at another repo and go learn modding.

[0]: https://shadowfacts.net/tutorials/forge-modding-112/
[1]: https://mcforge.readthedocs.io/en/1.12.x/gettingstarted/
