// src/store/useUserStore.ts
import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { UserFormData } from '@/types/userFormData';

interface UserStore {
  user: UserFormData | undefined;
  setUser: (user: UserFormData | undefined) => void;
  logout: () => void;
}

const useUserStore = create<UserStore>()(
  persist(
    (set) => ({
      user: undefined,
      setUser: (user) => set({ user }),
      logout: () => set({ user: undefined }),
    }),
    {
      name: 'user-info',
      storage: createJSONStorage(() => localStorage),
    }
  )
);

export default useUserStore;
