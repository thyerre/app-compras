import { LoaderService } from './loader.service';

describe('LoaderService', () => {
  let service: LoaderService;

  beforeEach(() => {
    service = new LoaderService();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with loading set to false', () => {
    expect(service.loading()).toBe(false);
  });

  it('should set loading to true when show is called', () => {
    service.show();
    expect(service.loading()).toBe(true);
  });

  it('should set loading to false when hide is called after show', () => {
    service.show();
    service.hide();
    expect(service.loading()).toBe(false);
  });

  it('should keep loading true when multiple requests are active', () => {
    service.show();
    service.show();
    service.hide();
    expect(service.loading()).toBe(true);
  });

  it('should set loading to false only after all requests complete', () => {
    service.show();
    service.show();
    service.show();
    service.hide();
    service.hide();
    expect(service.loading()).toBe(true);
    service.hide();
    expect(service.loading()).toBe(false);
  });

  it('should not go below zero active requests', () => {
    service.hide();
    service.hide();
    expect(service.loading()).toBe(false);
    service.show();
    expect(service.loading()).toBe(true);
    service.hide();
    expect(service.loading()).toBe(false);
  });
});
