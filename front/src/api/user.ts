// src/api/user.ts
export interface FormData {
    userId: string;
    userPassword: string;
    userName: string;
    phoneNum: string;
    userSex: 'M' | 'F';
}

export const user = async (formData: FormData): Promise<FormData> => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    });

    if (!response.ok) {
        throw new Error('Network response was not ok');
    }

    return response.json();
};
