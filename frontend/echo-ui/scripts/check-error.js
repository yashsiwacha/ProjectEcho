const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch();
  const page = await browser.newPage();
  
  const errors = [];
  page.on('console', msg => {
    if (msg.type() === 'error' || msg.type() === 'warning') {
      errors.push(`[${msg.type()}] ${msg.text()}`);
    }
  });
  page.on('pageerror', err => {
    errors.push(`[pageerror] ${err.message}`);
  });

  console.log('Navigating to http://localhost:3000...');
  await page.goto('http://localhost:3000');
  
  // Wait a bit for React to hydrate and throw errors
  await page.waitForTimeout(3000);
  
  if (errors.length > 0) {
    console.log('Found errors/warnings:');
    errors.forEach(e => console.log(e));
  } else {
    console.log('No errors or warnings found.');
  }

  await browser.close();
})();
