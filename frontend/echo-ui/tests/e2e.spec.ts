import { test, expect } from '@playwright/test';

test.describe('ProjectEcho End-to-End Career Passport Flow', () => {
  test('should navigate through the entire primary user journey', async ({ page }) => {
    // 1. Landing Page
    await page.goto('/');
    await expect(page.getByRole('heading', { name: 'ProjectEcho' }).first()).toBeVisible();
    await expect(page.getByText('Evidence-Driven Career Intelligence OS')).toBeVisible();

    // Navigate to Dashboard
    await page.getByRole('button', { name: 'Enter Executive Dashboard' }).click();
    await page.waitForURL('**/dashboard');
    await expect(page.getByRole('heading', { name: 'Executive Command Center' })).toBeVisible();

    // 2. Career Passport Page
    await page.getByRole('link', { name: 'Career Passport' }).click();
    await page.waitForURL('**/passport');
    await expect(page.getByText('Manage your immutable career identity')).toBeVisible();

    // 3. Evidence Upload Page
    await page.getByRole('link', { name: 'Evidence Sandbox' }).click();
    await page.waitForURL('**/evidence');
    await expect(page.getByRole('heading', { name: 'Evidence Verification Sandbox' })).toBeVisible();

    // 4. Mission Explorer Page
    await page.getByRole('link', { name: 'Mission Explorer' }).click();
    await page.waitForURL('**/missions');
    await expect(page.getByRole('heading', { name: 'Mission Explorer' })).toBeVisible();

    // 5. Readiness Assessment Page
    await page.getByRole('link', { name: 'Readiness Engine' }).click();
    await page.waitForURL('**/assessment');
    await expect(page.getByRole('heading', { name: 'Readiness Assessment Engine' })).toBeVisible();

    // 6. Reasoning Card Page
    await page.getByRole('link', { name: 'Reasoning Cards' }).click();
    await page.waitForURL('**/reasoning');
    await expect(page.getByRole('heading', { name: 'Explainable Reasoning Cards' })).toBeVisible();

    // 7. Decision Graph Page
    await page.getByRole('link', { name: 'Decision Graph' }).click();
    await page.waitForURL('**/graph');
    await expect(page.getByRole('heading', { name: 'Decision Graph Traceability' })).toBeVisible();

    // 8. Profile Page
    await page.getByRole('link', { name: 'Executive Profile' }).click();
    await page.waitForURL('**/profile');
    await expect(page.getByRole('heading', { name: 'Executive Profile Showcase' })).toBeVisible();
  });
});
