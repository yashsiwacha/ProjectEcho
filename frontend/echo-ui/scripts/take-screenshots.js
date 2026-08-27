/* eslint-disable @typescript-eslint/no-require-imports */
const { chromium } = require('playwright');
const fs = require('fs');

const ROUTES = [
  '/',
  '/dashboard',
  '/passport',
  '/skills',
  '/evidence',
  '/missions',
  '/reasoning',
  '/profile'
];

async function captureScreenshots() {
  const browser = await chromium.launch();
  const page = await browser.newPage({
    viewport: { width: 1440, height: 900 }
  });

  if (!fs.existsSync('screenshots')) {
    fs.mkdirSync('screenshots');
  }

  for (const route of ROUTES) {
    const url = `http://localhost:3000${route}`;
    console.log(`Navigating to ${url}...`);
    try {
      await page.goto(url, { waitUntil: 'networkidle', timeout: 15000 });
      // Wait a bit for animations
      await page.waitForTimeout(1000);
      const filename = route === '/' ? 'home' : route.replace('/', '');
      const filepath = `screenshots/${filename}.png`;
      await page.screenshot({ path: filepath, fullPage: true });
      console.log(`Saved screenshot to ${filepath}`);
    } catch (e) {
      console.error(`Failed to capture ${route}:`, e.message);
    }
  }

  await browser.close();
}

captureScreenshots().catch(console.error);
