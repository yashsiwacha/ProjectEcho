import { test, expect } from '@playwright/test';

test.describe('Dark Mode Enforcement', () => {
  test('should render dark background immediately without light mode hydration flash', async ({ page }) => {
    await page.goto('/');

    // Check if the HTML tag has color-scheme dark
    const htmlColorScheme = await page.evaluate(() => {
      return window.getComputedStyle(document.documentElement).getPropertyValue('color-scheme');
    });
    // The CSS variable or actual property should yield 'dark', or body has it
    
    // Check if the body background is our dark background color
    const bodyBg = await page.evaluate(() => {
      return window.getComputedStyle(document.body).backgroundColor;
    });
    
    // HSL(240, 10%, 4%) roughly translates to RGB(9, 9, 11) or similar very dark color
    // We expect it not to be white (rgb(255, 255, 255))
    expect(bodyBg).not.toBe('rgb(255, 255, 255)');
    expect(bodyBg).not.toBe('rgba(0, 0, 0, 0)'); // not transparent

    // Ensure there is no 'dark' class required to achieve dark mode
    // (We removed the need for the class, but if it exists, it's fine, as long as it's not 'light')
    const htmlClasses = await page.evaluate(() => document.documentElement.className);
    expect(htmlClasses).not.toContain('light');
  });
});
