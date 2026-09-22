$ErrorActionPreference = 'Stop'

$projectRoot = Split-Path -Parent $PSScriptRoot
$startScript = Join-Path $projectRoot 'scripts\start-production.cmd'
$startupFolder = [Environment]::GetFolderPath('Startup')
$shortcutPath = Join-Path $startupFolder 'ControleFacil.lnk'

if (-not (Test-Path $startScript)) {
  throw "Startup script not found: $startScript"
}

$shell = New-Object -ComObject WScript.Shell
$shortcut = $shell.CreateShortcut($shortcutPath)
$shortcut.TargetPath = $startScript
$shortcut.WorkingDirectory = $projectRoot
$shortcut.WindowStyle = 7
$shortcut.Description = 'Updates and starts ControleFacil when Windows user logs in.'
$shortcut.Save()

Write-Host ''
Write-Host 'ControleFacil configurado para iniciar com o Windows.'
Write-Host "Atalho: $shortcutPath"
Write-Host "Script: $startScript"
Write-Host ''
Write-Host 'O startup ira:'
Write-Host '  1. Buscar origin/main'
Write-Host '  2. Atualizar o codigo'
Write-Host '  3. Subir PostgreSQL via Docker'
Write-Host '  4. Instalar dependencias'
Write-Host '  5. Gerar o build'
Write-Host '  6. Iniciar o backend'
