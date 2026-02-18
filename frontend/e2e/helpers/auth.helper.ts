import { test as base, Page } from '@playwright/test';

export const ADMIN_EMAIL = 'admin@compras.gov.br';
export const ADMIN_PASSWORD = 'admin123';

export async function login(page: Page, email = ADMIN_EMAIL, password = ADMIN_PASSWORD): Promise<void> {
  await page.goto('/login');
  await page.getByLabel(/e-?mail/i).fill(email);
  await page.getByLabel(/senha|password/i).fill(password);
  await page.getByRole('button', { name: /entrar|login/i }).click();
  await page.waitForURL('**/dashboard**', { timeout: 10_000 });
}

export const test = base.extend<{ loggedInPage: Page }>({
  loggedInPage: async ({ page }, use) => {
    await login(page);
    await use(page);
  },
});

export { expect } from '@playwright/test';
