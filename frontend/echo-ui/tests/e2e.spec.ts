import { test, expect } from '@playwright/test';

test.describe('ProjectEcho End-to-End Career Passport Flow', () => {
  test('should navigate through the entire primary user journey', async ({ page }) => {
    // 1. Landing Page
    await page.goto('/');
    await expect(page.getByText('ProjectEcho')).toBeVisible();
    await expect(page.getByText('The Executive Career Operating System.')).toBeVisible();

    // Navigate to Dashboard
    await page.getByRole('button', { name: 'Explore Executive Dashboard' }).click();
    await page.waitForURL('**/dashboard');
    await expect(page.getByText('Executive Dashboard')).toBeVisible();

    // 2. Career Passport Page
    await page.getByRole('link', { name: 'Career Passport' }).click();
    await page.waitForURL('**/passport');
    await expect(page.getByText('Manage your immutable career identity')).toBeVisible();

    // 3. Evidence Upload Page
    await page.getByRole('link', { name: 'Evidence Upload' }).click();
    await page.waitForURL('**/evidence');
    await expect(page.getByText('Evidence Upload & Verification')).toBeVisible();

    // 4. Mission Explorer Page
    await page.getByRole('link', { name: 'Mission Explorer' }).click();
    await page.waitForURL('**/missions');
    await expect(page.getByText('Mission Explorer')).toBeVisible();

    // 5. Readiness Assessment Page
    await page.getByRole('link', { name: 'Readiness Assessment' }).click();
    await page.waitForURL('**/assessment');
    await expect(page.getByText('Readiness Assessment')).toBeVisible();

    // 6. Reasoning Card Page
    await page.getByRole('link', { name: 'Reasoning Card' }).click();
    await page.waitForURL('**/reasoning');
    await expect(page.getByText('Reasoning Cards')).toBeVisible();

    // 7. Decision Graph Page
    await page.getByRole('link', { name: 'Decision Graph' }).click();
    await page.waitForURL('**/graph');
    await expect(page.getByText('Decision Graph Traceability')).toBeVisible();

    // 8. Profile Page
    await page.getByRole('link', { name: 'Profile' }).click();
    await page.waitForURL('**/profile');
    await expect(page.getByText('Executive Profile')).toBeVisible();
  });
});
