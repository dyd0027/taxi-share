import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import RegisterForm from '../components/RegisterForm';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';

const queryClient = new QueryClient();

// Mock the router
jest.mock('next/navigation', () => ({
  useRouter: jest.fn(),
}));

describe('RegisterForm', () => {
  it('renders the form fields', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <RegisterForm />
      </QueryClientProvider>
    );
    expect(screen.getByPlaceholderText('아이디')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('비밀번호')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('이름')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('전화번호 (예: 010-1111-1111)')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: '회원가입' })).toBeInTheDocument();
  });

  it('submits the form', async () => {
    const push = jest.fn();
    (useRouter as jest.Mock).mockReturnValue({ push });

    render(
      <QueryClientProvider client={queryClient}>
        <RegisterForm />
      </QueryClientProvider>
    );

    fireEvent.change(screen.getByPlaceholderText('아이디'), { target: { value: 'testuser' } });
    fireEvent.change(screen.getByPlaceholderText('비밀번호'), { target: { value: 'password123' } });
    fireEvent.change(screen.getByPlaceholderText('이름'), { target: { value: 'John Doe' } });
    fireEvent.change(screen.getByPlaceholderText('전화번호 (예: 010-1111-1111)'), { target: { value: '010-1234-5678' } });
    fireEvent.change(screen.getByRole('combobox', { name: 'userSex' }), { target: { value: 'F' } });
    fireEvent.click(screen.getByRole('button', { name: '회원가입' }));

    await waitFor(() => {
      expect(push).toHaveBeenCalledWith('/sample');
    });
  });
});
