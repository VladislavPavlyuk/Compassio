$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$pidFile = Join-Path $root ".app.pid"

if (Test-Path $pidFile) {
    $existingPid = Get-Content $pidFile -ErrorAction SilentlyContinue
    if ($existingPid) {
        $proc = Get-Process -Id $existingPid -ErrorAction SilentlyContinue
        if ($proc) {
            Write-Host "App already running (PID $existingPid)."
            exit 0
        }
    }
}

$javaHome = "C:\Program Files\Java\jdk-25"
if (Test-Path $javaHome) {
    $env:JAVA_HOME = $javaHome
    $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
}

$process = Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/c", ".\mvnw.cmd spring-boot:run" `
    -WorkingDirectory $root `
    -PassThru `
    -WindowStyle Hidden

$process.Id | Set-Content -NoNewline $pidFile
Write-Host "Started app (PID $($process.Id))."
