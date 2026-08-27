import { test, expect } from '@playwright/test';

test.describe('Theme System', () => {
  test('should default to dark mode', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const htmlClasses = await page.evaluate(() => document.documentElement.className);
    expect(htmlClasses).toContain('dark');
  });

  test('should toggle theme when button is clicked', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    // Click theme toggle button (should exist in navbar or page)
    const toggle = page.locator('button[aria-label="Toggle theme"]').first();
    await toggle.click({ force: true });

    // Now it should be light mode
    const htmlClassesLight = await page.evaluate(() => document.documentElement.className);
    expect(htmlClassesLight).toContain('light');
    expect(htmlClassesLight).not.toContain('dark');

    // Toggle back to dark
    await toggle.click({ force: true });
    const htmlClassesDark = await page.evaluate(() => document.documentElement.className);
    expect(htmlClassesDark).toContain('dark');
    expect(htmlClassesDark).not.toContain('light');
  });

  test('should persist theme preference across reloads', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    // Toggle to light mode
    const toggle = page.locator('button[aria-label="Toggle theme"]').first();
    await toggle.click({ force: true });

    // Reload page
    await page.reload();
    await page.waitForLoadState('networkidle');

    // Verify it remained light mode
    const htmlClasses = await page.evaluate(() => document.documentElement.className);
    expect(htmlClasses).toContain('light');
  });
});
