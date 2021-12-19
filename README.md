# mod-scaffolding
The idea here is you can clone this repo, run the commands in the README and be ready to go. This is just an implementation of Shadowfacts' 1.12 modding tutorial.

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
