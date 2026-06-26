import { TopicListComponent } from './topic-list.component';
import { of } from 'rxjs';
import { describe, it, expect, vi } from 'vitest';

describe('TopicListComponent (logic only)', () => {

  function createComponent(userTopics: any[] = []) {

    const mockTopicService = {
      getAll: vi.fn().mockReturnValue(of([])),
      subscribe: vi.fn()
    };

    const mockSessionService = {
      user: {
        id: 1,
        topics: userTopics
      },
      refreshUser$: vi.fn()
    };

    const component = new TopicListComponent(
      mockTopicService as any,
      mockSessionService as any
    );

    return {
      component,
      mockTopicService,
      mockSessionService
    };
  }

  const topicsMock = [
    { id: 1, name: 'Angular', description: '' },
    { id: 2, name: 'Java', description: '' }
  ];

  // ✅ création
  it('should create', () => {
    const { component } = createComponent();
    expect(component).toBeTruthy();
  });

  // ✅ init → updateSubscriptions
  it('should initialize subscribed topic ids', () => {
    const { component } = createComponent([
      { id: 1 }
    ]);

    component.ngOnInit();

    expect(component.subscribedTopicIds.has(1)).toBe(true);
    expect(component.subscribedTopicIds.has(2)).toBe(false);
  });

  // ✅ isSubscribed
  it('should return correct subscription status', () => {
    const { component } = createComponent([{ id: 1 }]);

    component.ngOnInit();

    expect(component.isSubscribed(1)).toBe(true);
    expect(component.isSubscribed(2)).toBe(false);
  });

  // ✅ subscribe flow
  it('should subscribe and refresh user then update subscriptions', () => {
    const {
      component,
      mockTopicService,
      mockSessionService
    } = createComponent([{ id: 1 }]);

    mockTopicService.subscribe.mockReturnValue(of({}));
    mockSessionService.refreshUser$.mockReturnValue(of({
      id: 1,
      topics: [{ id: 1 }, { id: 2 }]
    }));

    component.ngOnInit();

    component.subscribe(2);

    expect(mockTopicService.subscribe).toHaveBeenCalledWith(2);
    expect(mockSessionService.refreshUser$).toHaveBeenCalled();
  });

  // ✅ update after refresh
  it('should update subscriptions after refresh', () => {
    const {
      component,
      mockTopicService,
      mockSessionService
    } = createComponent([{ id: 1 }]);

    mockTopicService.subscribe.mockReturnValue(of({}));

    // simulate updated user AFTER refresh
    mockSessionService.refreshUser$.mockImplementation(() => {
      mockSessionService.user = {
        id: 1,
        topics: [{ id: 1 }, { id: 2 }]
      };
      return of({});
    });

    component.ngOnInit();

    component.subscribe(2);

    expect(component.subscribedTopicIds.has(2)).toBe(true);
  });

});