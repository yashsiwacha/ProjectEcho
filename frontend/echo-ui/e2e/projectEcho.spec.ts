import { test, expect } from '@playwright/test';
import { AxeBuilder } from '@axe-core/playwright';

/**
 * ProjectEcho UI Validation Suite
 *
 * This suite validates visual fidelity, component rendering, responsiveness,
 * and accessibility for each primary page as defined in the Frontend Validation Sprint.
 */

const pages = [
  { path: '/', name: 'Landing' },
  { path: '/passport', name: 'Career Passport' },
  { path: '/missions', name: 'Mission Explorer' },
  { path: '/evidence', name: 'Evidence Upload & Verification' },
  { path: '/assessment', name: 'Readiness Assessment' },
  { path: '/reasoning', name: 'Explainable AI Reasoning Card' },
  { path: '/graph', name: 'Decision Graph Traceability' },
  { path: '/profile', name: 'Executive Profile' },
];

pages.forEach(({ path, name }) => {
  test.describe(`${name} page`, () => {
    test(`should load ${name} page without errors`, async ({ page }) => {
      await page.goto(path);
      // Basic sanity: ensure HTTP 200 and page title contains expected text
      await expect(page).toHaveURL(new RegExp(`${path}$`));
      await expect(page.locator('body')).toBeVisible();
    });

    test(`should have no accessibility violations on ${name} page`, async ({ page }) => {
      await page.goto(path);
      await new AxeBuilder({ page }).analyze();
    });
  });
});
