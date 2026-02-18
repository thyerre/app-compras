import { expect, test } from '../helpers/auth.helper';

test.describe('Layout - Mobile', () => {
  test.use({ viewport: { width: 375, height: 812 } }); // iPhone X size

  test('should open sidenav overlay on mobile', async ({ loggedInPage: page }) => {
    const sidenav = page.locator('mat-sidenav');

    // Sidenav should be closed on mobile initially
    await expect(sidenav).not.toBeVisible();

    // Open sidenav
    await page.locator('mat-toolbar button', { has: page.locator('mat-icon:text("menu")') }).click();
    await page.waitForTimeout(500);
    await expect(sidenav).toBeVisible();
  });

  test('sidenav should appear ABOVE header on mobile', async ({ loggedInPage: page }) => {
    // Open sidenav
    await page.locator('mat-toolbar button', { has: page.locator('mat-icon:text("menu")') }).click();
    await page.waitForTimeout(500);

    const sidenav = page.locator('mat-sidenav');
    await expect(sidenav).toBeVisible();

    // Verify z-index: sidenav must be higher than header
    const sidenavZIndex = await sidenav.evaluate(el => {
      return window.getComputedStyle(el).zIndex;
    });
    const headerZIndex = await page.locator('mat-toolbar').evaluate(el => {
      return window.getComputedStyle(el).zIndex;
    });

    const sidenavZ = parseInt(sidenavZIndex, 10) || 0;
    const headerZ = parseInt(headerZIndex, 10) || 0;

    expect(sidenavZ).toBeGreaterThan(headerZ);
  });

  test('should close sidenav on backdrop click', async ({ loggedInPage: page }) => {
    // Open sidenav
    await page.locator('mat-toolbar button', { has: page.locator('mat-icon:text("menu")') }).click();
    await page.waitForTimeout(500);
    await expect(page.locator('mat-sidenav')).toBeVisible();

    // Click backdrop
    await page.locator('.mat-drawer-backdrop').click();
    await page.waitForTimeout(500);
    await expect(page.locator('mat-sidenav')).not.toBeVisible();
  });

  test('should close sidenav on menu item click', async ({ loggedInPage: page }) => {
    // Open sidenav
    await page.locator('mat-toolbar button', { has: page.locator('mat-icon:text("menu")') }).click();
    await page.waitForTimeout(500);

    // Click a menu item if visible
    const menuItem = page.locator('mat-sidenav a[mat-list-item]').first();
    if (await menuItem.isVisible()) {
      await menuItem.click();
      await page.waitForTimeout(500);
      await expect(page.locator('mat-sidenav')).not.toBeVisible();
    }
  });
});
