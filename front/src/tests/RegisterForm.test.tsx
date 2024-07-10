import { render, screen, fireEvent } from '@testing-library/react';
import RegisterForm from '../components/RegisterForm';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import '@testing-library/jest-dom';

const queryClient = new QueryClient();

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
    expect(screen.getByText('회원가입')).toBeInTheDocument();
  });

  it('submits the form', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <RegisterForm />
      </QueryClientProvider>
    );
    fireEvent.change(screen.getByPlaceholderText('아이디'), { target: { value: 'testuser' } });
    fireEvent.change(screen.getByPlaceholderText('비밀번호'), { target: { value: 'password123' } });
    fireEvent.change(screen.getByPlaceholderText('이름'), { target: { value: 'John Doe' } });
    fireEvent.change(screen.getByPlaceholderText('전화번호 (예: 010-1111-1111)'), { target: { value: '010-1234-5678' } });
    fireEvent.click(screen.getByText('회원가입'));
  });
});
