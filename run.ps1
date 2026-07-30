$ErrorActionPreference="Stop"
New-Item -ItemType Directory -Force build\classes | Out-Null
$src=Get-ChildItem -Recurse src\main\java\*.java | % FullName
javac -encoding UTF-8 -d build\classes $src
java -cp build\classes physicsml.Main
