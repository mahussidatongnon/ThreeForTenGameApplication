rootProject.name = "ThreeForTengame"
include("common")
include("aiplayer")
include("core")
println("path: ${project(":common").path}")
println("name: ${project(":aiplayer").name}")
println(project(":core").name)