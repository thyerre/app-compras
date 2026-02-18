import { expect, test } from '@playwright/test';
import { login } from '../helpers/auth.helper';

test.describe('Login Page', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
  });

  test('should display login form', async ({ page }) => {
    await expect(page.getByRole('button', { name: /entrar|login/i })).toBeVisible();
    await expect(page.getByLabel(/e-?mail/i)).toBeVisible();
    await expect(page.getByLabel(/senha|password/i)).toBeVisible();
  });

  test('should show validation errors for empty fields', async ({ page }) => {
    await page.getByRole('button', { name: /entrar|login/i }).click();
    await expect(page.locator('mat-error')).toHaveCount(2);
  });

  test('should show error for invalid credentials', async ({ page }) => {
    await page.getByLabel(/e-?mail/i).fill('wrong@email.com');
    await page.getByLabel(/senha|password/i).fill('wrongpassword');
    await page.getByRole('button', { name: /entrar|login/i }).click();
    // Wait for error response
    await page.waitForTimeout(2000);
    await expect(page.locator('body')).not.toHaveURL(/dashboard/);
  });

  test('should login successfully with valid credentials', async ({ page }) => {
    await login(page);
    await expect(page).toHaveURL(/dashboard/);
  });

  test('should toggle password visibility', async ({ page }) => {
    const passwordInput = page.getByLabel(/senha|password/i);
    await passwordInput.fill('test123');

    // Initially password should be hidden
    await expect(passwordInput).toHaveAttribute('type', 'password');

    // Click eye icon to show password
    await page.locator('.suffix-icon-clickable').click();
    await expect(passwordInput).toHaveAttribute('type', 'text');

    // Click again to hide
    await page.locator('.suffix-icon-clickable').click();
    await expect(passwordInput).toHaveAttribute('type', 'password');
  });
});
