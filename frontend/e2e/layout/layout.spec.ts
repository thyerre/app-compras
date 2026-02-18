import { expect, test } from '../helpers/auth.helper';

test.describe('Layout', () => {
  test('should display header after login', async ({ loggedInPage: page }) => {
    await expect(page.locator('mat-toolbar')).toBeVisible();
    await expect(page.locator('mat-sidenav')).toBeVisible();
  });

  test('should display user name in header', async ({ loggedInPage: page }) => {
    await expect(page.getByText('admin@compras.gov.br').or(page.getByText('Administrador'))).toBeVisible();
  });

  test('should toggle sidenav on menu button click', async ({ loggedInPage: page }) => {
    const sidenav = page.locator('mat-sidenav');
    await expect(sidenav).toBeVisible();

    // Click menu toggle
    await page.locator('mat-toolbar button', { has: page.locator('mat-icon:text("menu")') }).click();
    await page.waitForTimeout(500);
  });

  test('should logout successfully', async ({ loggedInPage: page }) => {
    await page.locator('mat-toolbar button', { has: page.locator('mat-icon:text("account_circle")') }).click();
    await page.getByRole('menuitem', { name: /sair|logout/i }).click();
    await expect(page).toHaveURL(/login/);
  });
});
