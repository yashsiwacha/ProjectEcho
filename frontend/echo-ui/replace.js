const fs = require('fs');
const path = require('path');

function walk(dir, callback) {
  fs.readdirSync(dir).forEach(f => {
    let dirPath = path.join(dir, f);
    let isDirectory = fs.statSync(dirPath).isDirectory();
    isDirectory ? walk(dirPath, callback) : callback(path.join(dir, f));
  });
}

walk('/Users/yash/Yash-Workspace/projects/active/project-echo/frontend/echo-ui/src', function(filePath) {
  if (filePath.endsWith('.tsx') || filePath.endsWith('.ts')) {
    let content = fs.readFileSync(filePath, 'utf8');
    let original = content;

    // Replace color classes
    content = content.replace(/(text|bg|border|shadow)-(amber|emerald|cyan)-\d{3}(\/\d+)?/g, (match, type) => {
        if (type === 'text') return 'text-foreground';
        if (type === 'bg') return 'bg-muted';
        if (type === 'border') return 'border-border';
        if (type === 'shadow') return 'shadow-sm';
        return match;
    });

    // Replace glass panels
    content = content.replace(/glass-panel-glow/g, 'glass-panel');
    content = content.replace(/glass-panel-emerald/g, 'glass-panel');
    
    // Replace text gradients
    content = content.replace(/text-gradient-champagne/g, 'text-foreground font-semibold');
    content = content.replace(/text-gradient-emerald/g, 'text-foreground font-semibold');
    content = content.replace(/text-gradient-cyan/g, 'text-foreground font-semibold');

    if (content !== original) {
      fs.writeFileSync(filePath, content, 'utf8');
      console.log(`Updated: ${filePath}`);
    }
  }
});
