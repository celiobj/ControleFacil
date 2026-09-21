const fs = require('node:fs');
const path = require('node:path');

const projectRoot = path.resolve(__dirname, '..');
fs.rmSync(path.join(projectRoot, 'backend', 'dist'), { recursive: true, force: true });
fs.rmSync(path.join(projectRoot, 'backend', 'public'), { recursive: true, force: true });