// src/store/useUserStore.ts
import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { RouteData } from '@/types/routeData';

interface RouteStore {
  route: RouteData | undefined;
  setRoute: (route: RouteData | undefined) => void;
  clear: () => void;
}

const useUserStore = create<RouteStore>()(
  persist(
    (set) => ({
      route: undefined,
      setRoute: (route) => set({ route }),
      clear: () => set({ route: undefined }),
    }),
    {
      name: 'route-info',
      storage: createJSONStorage(() => localStorage),
    }
  )
);

export default useUserStore;
