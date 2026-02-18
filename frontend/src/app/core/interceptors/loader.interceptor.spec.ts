import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoaderService } from '../services/loader.service';
import { loaderInterceptor } from './loader.interceptor';

describe('loaderInterceptor', () => {
  let httpClient: HttpClient;
  let httpTesting: HttpTestingController;
  let loaderService: LoaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([loaderInterceptor])),
        provideHttpClientTesting(),
      ],
    });

    httpClient = TestBed.inject(HttpClient);
    httpTesting = TestBed.inject(HttpTestingController);
    loaderService = TestBed.inject(LoaderService);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should not show loader for GET requests', () => {
    httpClient.get('/api/test').subscribe();

    expect(loaderService.loading()).toBe(false);

    httpTesting.expectOne('/api/test').flush({});
  });

  it('should show loader for POST requests', () => {
    httpClient.post('/api/test', {}).subscribe();

    expect(loaderService.loading()).toBe(true);

    httpTesting.expectOne('/api/test').flush({});
    expect(loaderService.loading()).toBe(false);
  });

  it('should show loader for PUT requests', () => {
    httpClient.put('/api/test', {}).subscribe();

    expect(loaderService.loading()).toBe(true);

    httpTesting.expectOne('/api/test').flush({});
    expect(loaderService.loading()).toBe(false);
  });

  it('should show loader for DELETE requests', () => {
    httpClient.delete('/api/test').subscribe();

    expect(loaderService.loading()).toBe(true);

    httpTesting.expectOne('/api/test').flush({});
    expect(loaderService.loading()).toBe(false);
  });

  it('should hide loader when a POST request fails', () => {
    httpClient.post('/api/test', {}).subscribe({
      error: () => {},
    });

    expect(loaderService.loading()).toBe(true);

    httpTesting.expectOne('/api/test').error(new ProgressEvent('error'), { status: 500 });
    expect(loaderService.loading()).toBe(false);
  });
});
