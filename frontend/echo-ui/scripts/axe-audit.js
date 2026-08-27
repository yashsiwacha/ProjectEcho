/* eslint-disable @typescript-eslint/no-require-imports */
const { chromium } = require('playwright');
const { injectAxe, getViolations } = require('axe-playwright');
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

async function runAxeAudit() {
  const browser = await chromium.launch();
  const page = await browser.newPage();
  const results = {};

  for (const route of ROUTES) {
    const url = `http://localhost:3000${route}`;
    console.log(`Running axe-core on ${url}...`);
    try {
      await page.goto(url, { waitUntil: 'networkidle' });
      await injectAxe(page);
      const violations = await getViolations(page, null, {
        runOnly: { type: 'tag', values: ['wcag2a', 'wcag2aa'] }
      });
      results[route] = violations;
      console.log(`Found ${violations.length} violations on ${route}`);
    } catch (e) {
      console.error(`Failed to audit ${route}:`, e.message);
    }
  }

  fs.writeFileSync('axe-results.json', JSON.stringify(results, null, 2));
  await browser.close();
}

runAxeAudit().catch(console.error);
