import { FeedComponent } from './feed.component';
import { firstValueFrom, of, skip, take } from 'rxjs';
import { describe, it, expect, vi } from 'vitest';

describe('FeedComponent (logic only)', () => {

  function createComponent(mockPosts: any[]) {

    const mockPostService = {
      getAll: vi.fn().mockReturnValue(of(mockPosts))
    };

    const component = new FeedComponent(mockPostService as any);

    return {
      component,
      mockPostService
    };
  }

  const postsMock = [
    {
      id: 1,
      title: 'Post old',
      content: '...',
      author: 'user1',
      createdAt: '2023-01-01'
    },
    {
      id: 2,
      title: 'Post recent',
      content: '...',
      author: 'user2',
      createdAt: '2024-01-01'
    }
  ];

  // ✅ création
  it('should create', () => {
    const { component } = createComponent(postsMock);
    expect(component).toBeTruthy();
  });

  // ✅ doit appeler le service
  it('should call getAll', () => {
    const mockPostService = {
      getAll: vi.fn().mockReturnValue(of(postsMock))
    };

    new FeedComponent(mockPostService as any);

    expect(mockPostService.getAll).toHaveBeenCalled();
  });

  // ✅ tri par défaut (date-desc)
  it('should sort posts by date descending by default', async () => {
    const { component } = createComponent(postsMock);

    const result = await firstValueFrom(
      component.posts$.pipe(take(1))
    );

    expect(result[0].id).toBe(2);
    expect(result[1].id).toBe(1);
  });

  // ✅ tri ascendant après onSort()
  it('should sort posts by date ascending after toggle', async () => {
    const { component } = createComponent(postsMock);

    component.onSort();

    const result = await firstValueFrom(
      component.posts$.pipe(skip(1), take(1))
    );

    expect(result[0].id).toBe(1);
    expect(result[1].id).toBe(2);
  });
  ``

  // ✅ toggle tri
  it('should toggle sort mode', () => {
    const { component } = createComponent(postsMock);

    expect(component.sortMode$.value).toBe('date-desc');

    component.onSort();

    expect(component.sortMode$.value).toBe('date-asc');

    component.onSort();

    expect(component.sortMode$.value).toBe('date-desc');
  });

});