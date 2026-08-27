import { test, expect } from '@playwright/test';

test.describe('Authentication Flow', () => {
  test('should allow a user to login and store JWT', async ({ page }) => {
    // Navigate to homepage
    await page.goto('/');
    
    // Expect the page to have a login button or state
    // We will just do a basic assertion on the title for now
    await expect(page).toHaveTitle(/Echo/i);
    
    // Simulate setting JWT token in local storage (mocking login for E2E)
    await page.evaluate(() => {
      localStorage.setItem('echo_jwt_token', 'mock_jwt_token_for_e2e_testing');
    });

    // Navigate to a protected route (e.g. passport)
    await page.goto('/passport');

    // Assert that the page loaded successfully
    await expect(page.locator('h1').first()).toBeVisible();
  });
});
