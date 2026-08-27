import { test, expect } from '@playwright/test';

test.describe('Hydration Check', () => {
  test('should load application without React hydration errors in a clean context', async ({ page }) => {
    const errors: string[] = [];
    
    // Capture console errors
    page.on('console', msg => {
      if (msg.type() === 'error') {
        errors.push(msg.text());
      }
    });
    
    // Capture uncaught page errors
    page.on('pageerror', err => {
      errors.push(err.message);
    });

    await page.goto('/');
    
    // Wait for the app to fully load and hydrate
    await page.waitForLoadState('networkidle');
    
    // Filter for hydration specific errors
    const hydrationErrors = errors.filter(err => 
      err.includes('Hydration') || 
      err.includes('Minified React error #418') ||
      err.includes('Minified React error #423') ||
      err.includes('bis_skin_checked') ||
      err.includes('did not match') ||
      err.toLowerCase().includes('hydrate')
    );

    // If we have errors, we log them. If not, the test passes.
    if (hydrationErrors.length > 0) {
      console.log('Detected hydration errors:', hydrationErrors);
    }
    
    expect(hydrationErrors.length).toBe(0);
  });
});
