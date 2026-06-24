import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/test-setup.ts',
    coverage: {
      provider: 'v8',
      reporter: ['text', 'html'],
      reportsDirectory: './coverage',
      exclude: [
        'node_modules/**',
        'dist/**',
        '.angular/**',
        'src/main.ts',
        'src/app/app.*',
        '**/*.interface.ts',
        '**/*.model.ts',
        '**/*.config.ts',
        '**/*.routes.ts',
        '**/core/**',           
        '**/*.service.ts',      
      ]
    }
  }
});
