$ErrorActionPreference = 'Stop'

$projectRoot = Split-Path -Parent $PSScriptRoot
$startScript = Join-Path $projectRoot 'scripts\start-production.cmd'
$startupFolder = [Environment]::GetFolderPath('Startup')
$shortcutPath = Join-Path $startupFolder 'ControleFacil.lnk'

if (-not (Test-Path $startScript)) {
  throw "Production build not found. Run 'npm run build' first."
}

Unregister-ScheduledTask -TaskName 'ControleFacil' -Confirm:$false -ErrorAction SilentlyContinue

$shell = New-Object -ComObject WScript.Shell
$shortcut = $shell.CreateShortcut($shortcutPath)
$shortcut.TargetPath = $startScript
$shortcut.WorkingDirectory = $projectRoot
$shortcut.WindowStyle = 7
$shortcut.Description = 'Starts ControleFacil when the Windows user logs in.'
$shortcut.Save()

Write-Host "Startup shortcut created at $shortcutPath."