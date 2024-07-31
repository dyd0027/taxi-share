"use client";

import { useEffect, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { FormData } from '../../api/user';

export default function SamplePage() {
    // sample에도 user정보 잘 가져오는지 테스트용 코드
    const queryClient = useQueryClient();
    const [user, setUser] = useState<FormData | undefined>(undefined);

    useEffect(() => {
        const fetchUser = async () => {
        const userData = queryClient.getQueryData<FormData>(['user']);
        console.log("user >>>>> ", userData);
        setUser(userData);
        };

        fetchUser();
    }, [queryClient]); // queryClient가 변경될 때마다 fetchUser를 호출


    return <h1>This is the sample pagddde</h1>;
}