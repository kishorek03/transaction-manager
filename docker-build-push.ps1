param(
    [string]$Tag = ""
)

$ErrorActionPreference = "Stop"
$ImageName = "kishorek12/iyarkaivanam-backend"

if (-not $Tag) {
    $Tag = Read-Host "Enter image tag"
}

if (-not $Tag) {
    Write-Error "Tag cannot be empty"
    exit 1
}

$FullImage = "$ImageName`:$Tag"

if (-not (Test-Path "target\transaction-manager-0.0.1-SNAPSHOT.jar")) {
    Write-Host "JAR not found in target/. Packaging with Maven wrapper..."
    .\mvnw.cmd clean package -DskipTests
}

Write-Host "Building $FullImage using target\transaction-manager-0.0.1-SNAPSHOT.jar ..."
docker build -f Dockerfile.local -t $FullImage .

Write-Host "Pushing $FullImage ..."
docker push $FullImage

Write-Host "Done: $FullImage"
