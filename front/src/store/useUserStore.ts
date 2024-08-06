// src/store/useUserStore.ts
import create, { StateCreator } from 'zustand';
import { persist, PersistOptions } from 'zustand/middleware';
import { FormData } from '@/types/formData';

interface UserStore {
  user: FormData | undefined;
  setUser: (user: FormData | undefined) => void;
  logout: () => void;
}

type MyPersist = (
  config: StateCreator<UserStore>,
  options: PersistOptions<UserStore>
) => StateCreator<UserStore>;

const useUserStore = create<UserStore>(
  (persist as MyPersist)(
    (set) => ({
      user: undefined,
      setUser: (user) => set({ user }),
      logout: () => set({ user: undefined }),
    }),
    {
      name: 'user-info',
      getStorage: () => localStorage,
    }
  )
);

export default useUserStore;
