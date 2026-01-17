$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$pidFile = Join-Path $root ".app.pid"

if (-not (Test-Path $pidFile)) {
    Write-Host "No PID file found. App may not be running."
    exit 0
}

$appPid = Get-Content $pidFile -ErrorAction SilentlyContinue
if (-not $appPid) {
    Remove-Item $pidFile -ErrorAction SilentlyContinue
    Write-Host "PID file was empty."
    exit 0
}

$proc = Get-Process -Id $appPid -ErrorAction SilentlyContinue
if (-not $proc) {
    Remove-Item $pidFile -ErrorAction SilentlyContinue
    Write-Host "No running process for PID $appPid. Cleaned PID file."
    exit 0
}

& taskkill /PID $appPid /T /F | Out-Null
Remove-Item $pidFile -ErrorAction SilentlyContinue
Write-Host "Stopped app (PID $appPid)."
