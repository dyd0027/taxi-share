import Image from "next/image";
import Link from "next/link";

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div className="min-h-screen flex flex-col items-center justify-center">
            <h1 className="text-3xl font-bold mb-4">Welcome to the Home Page</h1>
            <Link href="/signup">
                <span className="text-blue-500 underline hover:text-blue-700">
                    Go to Signup
                </span>
            </Link>
            <Link href="/login">
                <span className="text-blue-500 underline hover:text-blue-700">
                    Go to Login
                </span>
            </Link>
        </div>
    </main>
  );
}
