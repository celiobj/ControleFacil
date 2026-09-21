const fs = require('node:fs');
const path = require('node:path');

const projectRoot = path.resolve(__dirname, '..');
const source = path.join(projectRoot, 'frontend', 'dist');
const destination = path.join(projectRoot, 'backend', 'public');

fs.rmSync(destination, { recursive: true, force: true });
fs.cpSync(source, destination, { recursive: true });
console.log(`Frontend copied to ${destination}`);